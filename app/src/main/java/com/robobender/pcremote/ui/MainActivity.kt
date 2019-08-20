package com.robobender.pcremote.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.view.VelocityTracker
import com.robobender.pcremote.data.Message
import com.robobender.pcremote.data.MessageTypes

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.robobender.pcremote.R


class MainActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {

    private val TAG = "HUI"

    var velocityTracker: VelocityTracker = VelocityTracker.obtain()
    var startTime = 0L
    var sharedPreferences:SharedPreferences? = null

    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cl_main.setOnClickListener(this)
        cl_main.setOnTouchListener(this)

        savedInstanceState?.run {
            tv_x.text = getString("tv_x.text")
            tv_y.text = getString("tv_y.text")
            tv_pres.text = getString("tv_pres.text")
        }

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val serverURL = sharedPreferences?.getString(getString(R.string.pref_server), "").toString()

        viewModel?.connect(serverURL)
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            tv_x.text = getString("tv_x.text")
            tv_y.text = getString("tv_y.text")
            tv_pres.text = getString("tv_pres.text")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("tv_x.text", tv_x.text.toString())
            putString("tv_y.text", tv_y.text.toString())
            putString("tv_pres.text", tv_pres.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {

        val action = event.actionMasked
        tv_x.text = getString(R.string.x,  event.rawX)
        tv_y.text = getString(R.string.y, event.rawY)
        tv_pres.text = getString(R.string.pressure, event.pressure)

        when(action) {
            MotionEvent.ACTION_DOWN -> {

                velocityTracker.clear()
                velocityTracker.addMovement(event)

                tv_vel_x.text =  getString(R.string.vel_x, 0.toFloat())
                tv_vel_y.text =  getString(R.string.vel_y, 0.toFloat())
            }
            MotionEvent.ACTION_MOVE -> {
                if (System.currentTimeMillis() - startTime > 200){
                    velocityTracker.addMovement(event)
                    velocityTracker.computeCurrentVelocity(1000)

                    //1000 provides pixels per second
                    val xVelocity = velocityTracker.xVelocity
                    val yVelocity = velocityTracker.yVelocity

                    // tv_vel_x.text = "Vel X: $xVelocity"
                    tv_vel_x.text = getString(R.string.vel_x, xVelocity)
                    tv_vel_y.text = getString(R.string.vel_y, yVelocity)

                    val type = if (event.pointerCount == 1 ) MessageTypes.MOVE.value else MessageTypes.SCROLL.value

                    viewModel?.send(Message(
                        messageType = type,
                        velX = xVelocity,
                        velY = yVelocity,
                        x = event.rawX,
                        y = event.rawY,
                        pressure = event.pressure
                    ))
                }
            }
            MotionEvent.ACTION_UP or MotionEvent.ACTION_CANCEL -> {
                velocityTracker.recycle()
                view.performClick()
            }
        }

        Log.d("TTT", "Touch")
        return false
    }

    override fun onClick(view: View?) {

        if (view?.id == btn_settings.id) {
            openSettings()
        } else if (System.currentTimeMillis() - startTime < 200){
            viewModel?.send(Message(
                messageType = MessageTypes.CLICK.value,
                velX = 0.toFloat(),
                velY = 0.toFloat(),
                x = 0.toFloat(),
                y = 0.toFloat(),
                pressure = 0.toFloat()
            ))
        }
        startTime = System.currentTimeMillis()
    }

    override fun onPause() {
        viewModel?.disconnect()
        super.onPause()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}
