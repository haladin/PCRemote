using System;
using Fleck;
using Newtonsoft.Json;
using PC_Remote_Server.Data;
using PC_Remote_Server.Utils;

namespace PC_Remote_Server
{
    public class Server
    {
        internal void OnOpen(IWebSocketConnection socket)
        {
            Console.WriteLine($"Client connected...");
        }

        internal void OnClose(IWebSocketConnection socket)
        {
            //throw new NotImplementedException();
            Console.WriteLine($"Client disconnected.");
        }

        internal void OnMessage(IWebSocketConnection socket, string json)
        {
            try
            {
                var message = JsonConvert.DeserializeObject<Message>(json);

                Console.WriteLine($"Message recieved: '{message}'");

                switch((MessageTypes)message.MessageType)
                {
                    case MessageTypes.MOVE:
                        Mouse.MoveMouse(message);
                        break;
                    case MessageTypes.CLICK:
                        Mouse.DoMouseClick();
                        break;
                    case MessageTypes.SCROLL:
                        Mouse.DoMouseScroll(message);
                        break;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }



    }
}
