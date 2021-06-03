using Client;
using Kerberos_Client;
using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Security.Cryptography;
using System.Text;
using System.Threading;
using static Client.DESLibrary;
using static Kerberos_Client.MyStruct;

namespace Client_test
{
    class Program
    {
        static void Main(string[] args)
        {
            init();
            while (true) ;
            //DES_test();
            //RSA_test();

        }
        static Thread threadWatch = null; // 负责监听客户端连接请求的 线程；
        static Socket socketWatch = null;

        static Dictionary<string, Socket> dict = new Dictionary<string, Socket>();//ip和socket
        static Dictionary<string, string> dictID = new Dictionary<string, string>();//id和ip
        static Dictionary<string, string> dictID_reverse = new Dictionary<string, string>();//ip和id
        static Dictionary<string, Thread> dictThread = new Dictionary<string, Thread>();//ip和线程

        static private void init()
        {
            // 创建负责监听的套接字，注意其中的参数；
            socketWatch = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            // 获得文本框中的IP对象；
            IPAddress address = IPAddress.Parse("127.0.0.1");
            // 创建包含ip和端口号的网络节点对象；
            IPEndPoint endPoint = new IPEndPoint(address, 50810);
            try
            {
                // 将负责监听的套接字绑定到唯一的ip和端口上；
                socketWatch.Bind(endPoint);
            }
            catch (SocketException se)
            {
                Console.WriteLine("异常：" + se.Message);
                return;
            }
            // 设置监听队列的长度；
            socketWatch.Listen(10);
            // 创建负责监听的线程；
            threadWatch = new Thread(WatchConnecting);
            threadWatch.IsBackground = true;
            threadWatch.Start();
            ShowMsg("服务器启动监听成功！");
            //}
        }

        /// <summary>
        /// 监听客户端请求的方法；
        /// </summary>
        static void WatchConnecting()
        {
            while (true)  // 持续不断的监听客户端的连接请求；
            {
                // 开始监听客户端连接请求，Accept方法会阻断当前的线程；
                Socket sokConnection = socketWatch.Accept(); // 一旦监听到一个客户端的请求，就返回一个与该客户端通信的 套接字；
                // 想列表控件中添加客户端的IP信息；
                Console.WriteLine("IP信息:"+sokConnection.RemoteEndPoint.ToString());
                // 将与客户端连接的 套接字 对象添加到集合中；
                dict.Add(sokConnection.RemoteEndPoint.ToString(), sokConnection);
                ShowMsg("客户端连接成功！");
                Thread thr = new Thread(RecMsg);
                thr.IsBackground = true;
                thr.Start(sokConnection);
                dictThread.Add(sokConnection.RemoteEndPoint.ToString(), thr);  //  将新建的线程 添加 到线程的集合中去。
            }
        }

        static void RecMsg(object sokConnectionparn)
        {
            Socket sokClient = sokConnectionparn as Socket;
            while (true)
            {
                bool run_normal = true;
                // 定义一个2M的缓存区；
                byte[] arrMsgRec = new byte[1024 * 1024 * 2];
                // 将接受到的数据存入到输入  arrMsgRec中；
                int length = -1;
                try
                {
                    length = sokClient.Receive(arrMsgRec); // 接收数据，并返回数据的长度；
                }
                catch (SocketException se)
                {
                    ShowMsg("异常：" + se.Message);
                    // 从 通信套接字 集合中删除被中断连接的通信套接字；
                    dict.Remove(sokClient.RemoteEndPoint.ToString());
                    // 从通信线程集合中删除被中断连接的通信线程对象；
                    dictThread.Remove(sokClient.RemoteEndPoint.ToString());
                    // 从列表中移除被中断的连接IP
                    Console.WriteLine(sokClient.RemoteEndPoint.ToString()+"xxxx");
                    //String s = dictID_reverse[sokClient.RemoteEndPoint.ToString()];
                    //dictID_reverse.Remove(sokClient.RemoteEndPoint.ToString());
                    //dictID.Remove(s);
                    run_normal = false;
                    break;
                }
                catch (Exception e)
                {
                    ShowMsg("异常：" + e.Message);
                    // 从 通信套接字 集合中删除被中断连接的通信套接字；
                    dict.Remove(sokClient.RemoteEndPoint.ToString());
                    // 从通信线程集合中删除被中断连接的通信线程对象；
                    dictThread.Remove(sokClient.RemoteEndPoint.ToString());
                    // 从列表中移除被中断的连接IP
                    Console.WriteLine(sokClient.RemoteEndPoint.ToString() + "xxxx");
                    string s = dictID_reverse[sokClient.RemoteEndPoint.ToString()];
                    dictID_reverse.Remove(sokClient.RemoteEndPoint.ToString());
                    dictID.Remove(s);
                    run_normal = false;
                    break;
                }
                if (run_normal)  // 表示接收到的是数据；
                {
                    string strMsg = System.Text.Encoding.UTF8.GetString(arrMsgRec, 0, length);// 将接受到的字节数据转化成字符串；
                    //ShowMsg(strMsg);
                    Order order= JsonHelper.FromJson<Order>(strMsg);
                    MyStruct myStruct ;
                    switch (order.MsgType)
                    {
                        case "1002":
                            order.Extend = DecryptDES(order.Extend, "12345678");
                            myStruct = JsonHelper.FromJson<MyStruct>(order.Extend);
                            User user = myStruct.user;
                            order.StatusReport = true;
                            break;
                        case "0001":
                            myStruct = JsonHelper.FromJson<MyStruct>(order.Extend);
                            Certificate certificate = myStruct.certificate;
                            StreamReader sw1 = new StreamReader(@"F:\Cryption\PKB.txt");
                            myStruct.certificate.Pk = sw1.ReadLine();
                            sw1.Close();
                            break;                
                    }
                    sendMsg("127.0.0.1", JsonHelper.ToJson(order));
                }   
            }
        }
        static public void sendMsg(string ip,string msg)
        {
            ShowMsg("sendLoginMsg");
            foreach (KeyValuePair<string, Socket> keyValue in dict)
            {
                string s = keyValue.Key;
                //if (s.Equals(ip))
                //{
                    ShowMsg(msg);
                    byte[] bmsg = System.Text.Encoding.UTF8.GetBytes(msg);
                    dict[s].Send(bmsg);
               // }
                //keyValue.Value.Send(by);
                //MessageBox.Show(msg);
            }
        }
        static void ShowMsg(string str)
        {
            Console.WriteLine(str);
        }

        public static void my_init()
        {
            string line = "";
            StreamReader sr = new StreamReader("Plaintext.txt");
            StreamWriter sw = new StreamWriter("Kc.txt");
            while ((line = sr.ReadLine()) != null)
            {
                string StrRandom = random_str(8);
                sw.WriteLine(StrRandom);
            }
            sw.Close();
            sr.Close();
        }
        static void DES_test()
        {
            string line;
            StreamReader sr_P = new StreamReader("Plaintext.txt");
            StreamReader sr_K = new StreamReader("Kc.txt");
            StreamWriter sw_EN = new StreamWriter("DESENout.txt");
            StreamWriter sw_DE = new StreamWriter("DESDEout.txt");
            while ((line = sr_P.ReadLine()) != null)
            {
                string kc = sr_K.ReadLine();
                string Enc_str = EncryptDES(line, kc);
                string Dec_str = DecryptDES(Enc_str, kc);
                sw_EN.WriteLine(Dec_str);
                sw_DE.WriteLine(Enc_str);

            }
            sr_P.Close();
            sr_K.Close();
            sw_EN.Close();
            sw_DE.Close();
        }

        static void RSA_test()
        {
            string line;
            StreamReader sr_P = new StreamReader(@"F:\Cryption\RSAENout.txt");
            StreamReader sr_K = new StreamReader("Kc.txt");
            StreamWriter sw_EN = new StreamWriter("RSAENout.txt");
            StreamWriter sw_DE = new StreamWriter("RSADEout.txt");
            //StreamWriter sw_dig = new StreamWriter("Digitial.txt");
            //StreamWriter sw_di = new StreamWriter("Dig.txt");
            StreamReader sw1 = new StreamReader("PKB1.txt");
            StreamReader sw2 = new StreamReader("PKI1.txt");
            StreamReader sr_dig = new StreamReader(@"F:\Cryption\Digitial.txt");
            string PKB, PKI;
            PKB = sw1.ReadLine();
            PKI = sw2.ReadLine();
            while ((line = sr_P.ReadLine()) != null)
            {
                //RSALibrary.RSAKey(out PKI, out PKB);
                string Enc_str = RSALibrary.RSAEncrypt(PKB, line);
                string Dec_str = RSALibrary.RSADecrypt(PKI, line) ;
                string dig_str=string.Empty;
                //RSALibrary.SignatureFormatter(PKI, line, ref dig_str);
                dig_str = sr_dig.ReadLine();
                sw_EN.WriteLine(Enc_str);
                sw_DE.WriteLine(Dec_str);
                //sw_dig.WriteLine(dig_str);
                string s=null;
                RSALibrary.SignatureFormatter(PKI, Dec_str, ref s);
                if (RSALibrary.SignatureDeformatter(PKB, dig_str, Dec_str))
                    Console.WriteLine("1");
                //sw_di.WriteLine(dig_str);

            }
            sr_P.Close();
            sr_K.Close();
            sw_EN.Close();
            sw_DE.Close();
            //sw_di.Close();
            //sw_dig.Close();
            sw1.Close();
            sw2.Close();

        }
        //生成字母和数字随机数
        public static string random_str(int length, bool sleep)
        {
            if (sleep)
            {
                System.Threading.Thread.Sleep(3);
            }
            char[] Pattern = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o','p','q','r','s','t','u','v','w','x','y','z' };
            string result = "";
            int n = Pattern.Length;
            System.Random random = new Random(~unchecked((int)DateTime.Now.Ticks));
            for (int i = 0; i < length; i++)
            {
                int rnd = random.Next(0, n);
                result += Pattern[rnd];
            }
            return result;
        }

        public static string random_str(int length)
        {
            return random_str(length, false);
        }
    }
}
