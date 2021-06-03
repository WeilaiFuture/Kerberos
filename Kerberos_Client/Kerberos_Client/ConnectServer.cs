//#define lianji
using Kerberos_Client.UI;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Forms;
using System.Windows.Threading;
using static Kerberos_Client.MyStruct;
using MessageBox = System.Windows.Forms.MessageBox;

namespace Kerberos_Client
{
    public class ConnectServer
    {
        public static bool isExit = false;
        public static Socket client = null;
        private static Thread threadReceive = null;
        private static MainWindow w;
        //连接服务器
        internal static void connectserver(Window window, string str="127.0.0.1",int p=50810)
        {
            w = window as MainWindow;
            //连接远端的服务器IP
            string remoteIP = str;
            int port = p;
#if lianji
            remoteIP = "192.168.43.64";
            port = 1122;
#endif

            IPAddress ip = IPAddress.Parse(remoteIP);
            //IPEndPoint endPoint = new IPEndPoint(ip, 59000);
            client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            client.Connect(ip, port);
            if (threadReceive == null)
            {
                threadReceive = new Thread(ReceiveData);
                threadReceive.IsBackground = true;
                threadReceive.Start();
            }
        }
        /// <summary>
        /// 发送消息
        /// </summary>
        /// <param 要发送的消息="text"></param>
        public static void sendMessage(string strMsg)
        {
            if (!string.Empty.Equals(strMsg))
                if (client != null && client.Connected)
                    client.Send(Encoding.UTF8.GetBytes(strMsg));
                else
                    System.Windows.MessageBox.Show("与服务器断开连接");
            Thread.Sleep(500);
        }
        public static void HeartStart()
        {
            Thread heartSend_ = new Thread(heartSend);
            heartSend_.IsBackground = true;
            heartSend_.Start();
        }
        /// <summary>
        /// 发送心跳包
        /// </summary>
        private static void heartSend()
        {
            while(Main_Window.live)
            {
                Thread.Sleep(30 * 1000);
                sendMessage("heart");
            }
            return;
        }

        /// <summary>处理接收的服务器端数据</summary>
        public static void ReceiveData()
        {
            int num = -1;
            while (true)
            {
                byte[] result = new byte[1024 * 1024];
                if (client != null && client.Connected)
                {
                    //通过clientSocket接受数据
                    try
                    {
                        num = client.Receive(result);
                        //MessageBox.Show(Encoding.UTF8.GetString(result, 0, num));
                    }
                    catch (SocketException se)
                    {
                        System.Windows.MessageBox.Show("异常；" + se.Message);
                        return;
                    }
                    catch (Exception e)
                    {
                        System.Windows.MessageBox.Show("异常：" + e.Message);
                        return;
                    }
                }
                if (num <= 0)
                    break;
                //MessageBox.Show("ERROR");
                string strMsg = System.Text.Encoding.UTF8.GetString(result, 0, num);
                Order order =JsonHelper.FromJson<Order>(strMsg);
                string command = order.MsgType;
                switch (command)
                {
                    case "0001":
                        w.Call_certificate(order);
                        break;
                    case "0002":
                        w.Call_Key(order);
                        break;
                    case "0004":
                        w.Call_AS(order);
                        break;
                    case "0006":
                        w.Call_TGS(order);
                        break;
                    case "0008":
                        w.Call_Server(order);
                        break;
                    case "1001":
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "1001");
                        break;
                    case "1002":
                        w.Call_check_User(order);
                        break;
                    case "1003": //好友请求
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                        break;
                    case "1004"://好友界面
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                        break;
                    case "1005": 
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                        break;
                    case "1006": 
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                        break;
                    case "1007":
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                        break;
                    case "1008": 
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                        break;
                    case "1009": 
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                        break;
                    case "1010":
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                        break;
                    case "2001": 
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                        break;
                    case "2002": 
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                        break;
                    default:
                        MessageBox.Show(Encoding.UTF8.GetString(result, 0, num));
                        break;
                }
            }
        }

    }
}
