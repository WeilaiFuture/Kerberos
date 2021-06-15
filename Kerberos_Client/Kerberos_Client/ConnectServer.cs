//#define lianji
using Kerberos_Client.Log;
using Kerberos_Client.UI;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Text.RegularExpressions;
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
        public static ShowMessage show;
        static string recv = string.Empty;
        //连接服务器
        internal static void connectserver(Window window, string str = "127.0.0.1", int p = 50810)
        {
            w = window as MainWindow;
            //连接远端的服务器IP
            string remoteIP = str;
            int port = p;

            IPAddress ip = IPAddress.Parse(remoteIP);
            //IPEndPoint endPoint = new IPEndPoint(ip, 59000)
            if (client != null)
            {
                if (client.Connected)
                {
                    client.Shutdown(SocketShutdown.Both);
                    client.Disconnect(true);
                    Logger.Instance.WriteLog(remoteIP + "/" + port.ToString(), LogType.DisConnect);
                }
            }
            if (!w.iscancle)
            {
                client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                try
                {
                    client.Connect(ip, port);
                }
                catch
                {
                    MessageBox.Show("连接超时！");
                }
                Logger.Instance.WriteLog(remoteIP + "/" + port.ToString(), LogType.Connect);

                    threadReceive = new Thread(ReceiveData);
                    threadReceive.IsBackground = true;
                    threadReceive.Start();
            }
            else
            {
                if (threadReceive != null)
                    threadReceive.Abort();
            }
        }
        /// <summary>
        /// 发送消息
        /// </summary>
        /// <param 要发送的消息="text"></param>
        public static void sendMessage(Order order)
        {
            string sig = string.Empty;
            RSALibrary.SignatureFormatter(Main_Window.Keys["private"],order.Extend,  ref sig);
            order.Sign = sig;
            show.add(order);
            string strMsg = JsonHelper.ToJson(order);
            Logger.Instance.WriteLog(strMsg, LogType.Send);
            if (!string.Empty.Equals(strMsg))
                if (client != null && client.Connected)
                    client.Send(Encoding.UTF8.GetBytes(strMsg));
                else
                    System.Windows.MessageBox.Show("与服务器断开连接");
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
                //发送报文
                Order order = new Order();
                order.Dst = "Server";
                order.Src = MainWindow.localName;
                order.MsgType = "1006";
                MyStruct myStruct = new MyStruct();
                order.Extend = JsonHelper.ToJson(myStruct);
                order.Extend = DESLibrary.EncryptDES(order.Extend, Main_Window.Keys["server"]);
                sendMessage(order);
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
                if (strMsg[strMsg.Length - 1] != '}')
                {
                    if (recv == string.Empty)
                        recv = strMsg;
                    else
                        recv += strMsg;
                    return;
                }
                else
                    recv += strMsg;
                strMsg = recv;
                string pattern = "\"{*}\"";
                string[] ans = Regex.Split(strMsg,pattern);
                foreach (object o in ans)
                {
                    strMsg = o as string;
                    Logger.Instance.WriteLog(strMsg, LogType.Recv);
                    Order order = JsonHelper.FromJson<Order>(strMsg);
                    recv = string.Empty;
                    if (order == null)
                        return;
                    show.add(order);
                    string command = order.MsgType;
                    if (order.Src.Equals("Server"))
                    {
                        if (!order.MsgType.Equals("0001") && !order.MsgType.Equals("0008"))
                        {

                        }
                        //MessageBox.Show(RSALibrary.SignatureDeformatter(Main_Window.Keys["server_pk"], order.Sign, order.Extend.ToString()).ToString());
                    }
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
                            w.main_Window.Call_Friend(order);
                            break;
                        case "1005":
                            //MyStruct myStruct1 = JsonHelper.FromJson<MyStruct>(DESLibrary.DecryptDES(order.Extend, Main_Window.Keys["server"]));
                            //MessageBox.Show(myStruct1.user.Uname + "上线了");
                            w.main_Window.Call_Greet(order);
                            break;
                        case "1006":
                            MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                            break;
                        case "1007":
                            w.main_Window.Add_.Call_Search(order);
                            break;
                        case "1008":
                            MyStruct myStruct = JsonHelper.FromJson<MyStruct>(DESLibrary.DecryptDES(order.Extend, Main_Window.Keys["server"]));
                            MessageBox.Show(myStruct.user.Uname + "下线了");
                            break;
                        case "1009":
                            MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                            break;
                        case "1010":
                            MessageBox.Show(Encoding.UTF8.GetString(result, 0, num) + "0001");
                            break;
                        case "2001":
                            #region 信息报文
                            switch (order.ContentType)
                            {
                                case "101":
                                    w.main_Window.Call_Message(order);
                                    break;
                                case "9001":
                                    w.main_Window.ReCall_Friend(order);
                                    break;
                                case "9005":
                                    w.main_Window.Request();
                                    break;
                                case "9006":
                                    w.main_Window.Call_Result(order);
                                    break;
                                case "9007":
                                    w.main_Window.Call_Result(order);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        #endregion
                        case "2002":
                            #region 信息报文
                            switch (order.ContentType)
                            {
                                case "101":
                                    w.main_Window.Call_groupMessage(order);
                                    break;
                                case "9002":
                                    w.main_Window.ReCall_Add_Group(order);
                                    break;
                                case "9003":
                                    w.main_Window.Call_Request_Group(order);
                                    break;
                                case "9004":
                                    w.main_Window.Request();
                                    break;
                                case "9008":
                                    w.main_Window.Call_Result_Group(order);
                                    break;
                                case "9009":
                                    w.main_Window.Call_Result_Group(order);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        #endregion
                        default:
                            MessageBox.Show(Encoding.UTF8.GetString(result, 0, num));
                            break;
                    }
                }
            }
        }
        public static void disConnet()
        {
            if (client != null)
            {
                client.Shutdown(SocketShutdown.Both);
                client.Disconnect(true);
                Logger.Instance.WriteLog("断开连接", LogType.DisConnect);
            }
        }

    }
}
