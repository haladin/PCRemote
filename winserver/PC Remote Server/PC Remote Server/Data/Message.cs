using System;
using System.Collections.Generic;
using System.Text;

namespace PC_Remote_Server.Data
{
    public class Message
    {
        // val velX: Float, val velY: Float, val x: Float, val y: Float, val pressure: Float
        public int MessageType { get; set; }
        public float VelX { get; set; }
        public float VelY { get; set; }
        public float X { get; set; }
        public float Y { get; set; }
        public float Pressure { get; set; }

        public override string ToString()
        {
            return $"Type: {MessageType}, VelX: {VelX}, VelY: {VelY}, X: {X}, Y: {Y}, Pressure: {Pressure}, ";
        }
    }
}
