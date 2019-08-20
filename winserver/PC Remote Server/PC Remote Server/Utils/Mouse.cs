using PC_Remote_Server.Data;
using System;
using System.Collections.Generic;
using System.Text;
using static PC_Remote_Server.Utils.Win32;

namespace PC_Remote_Server.Utils
{
    class Mouse
    {
        public static POINT GetCursorPosition()
        {
            POINT lpPoint;
            GetCursorPos(out lpPoint);
            //bool success = User32.GetCursorPos(out lpPoint);
            // if (!success)

            return lpPoint;
        }

        public static void MoveMouse(Message message)
        {
            Win32.POINT p = new Win32.POINT();
            var currentMousePosition = GetCursorPosition();
            p.x = currentMousePosition.x + (int)message.VelX/10;
            p.y = currentMousePosition.y + (int)message.VelY/10;

            Win32.ClientToScreen(IntPtr.Zero, ref p);
            Win32.SetCursorPos(p.x, p.y);
        }

        public static void DoMouseClick()
        {
            //Call the imported function with the cursor's current position
            var currentMousePosition = GetCursorPosition();
            uint X = (uint)currentMousePosition.x;
            uint Y = (uint)currentMousePosition.y;
            Win32.mouse_event(((uint)Win32.MouseEventFlags.MOUSEEVENTF_LEFTDOWN | (uint)Win32.MouseEventFlags.MOUSEEVENTF_LEFTUP), X, Y, 0, 0);
        }

        public static void DoMouseScroll(Message message)
        {
            //mouse_event(MOUSEEVENTF_WHEEL, 0, 0, 120, 0);

            int cButtons = message.VelY > 0 ? 120 : -120;
            
            Win32.mouse_event((uint)Win32.MouseEventFlags.MOUSEEVENTF_WHEEL, 0, 0, (uint)cButtons, 0);
        }
    }
}
