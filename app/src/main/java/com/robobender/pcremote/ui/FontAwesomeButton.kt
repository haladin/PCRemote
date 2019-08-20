package com.robobender.pcremote.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.Button

class FontAwesomeButton : Button {


    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {

        //Font name should not contain "/".
        val tf = Typeface.createFromAsset(
            context.assets,
            "fontawesome.otf"
        )
        typeface = tf
    }

}