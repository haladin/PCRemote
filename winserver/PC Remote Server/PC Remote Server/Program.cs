using Fleck;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.RegularExpressions;


namespace PC_Remote_Server
{
    class Program
    {
        static void Main(string[] args)
        {
            var sockets = new List<IWebSocketConnection>();
            var server = new WebSocketServer("ws://192.168.0.162:8181");
            var serverImplemntation = new Server();
            server.Start(socket =>
            {
                socket.OnOpen = () => serverImplemntation.OnOpen(socket);
                socket.OnClose = () => serverImplemntation.OnClose(socket);
                socket.OnMessage = m => serverImplemntation.OnMessage(socket, m);
            });

            string input = Console.ReadLine();
            while (input != "exit")
            {
                sockets.ToList().ForEach(s => s.Send(input));
                input = Console.ReadLine();
            }

        }
        
    }
}
