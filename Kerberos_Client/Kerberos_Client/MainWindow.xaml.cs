#define test
//#define kerberos
#define DES
#define RSA
using Kerberos_Client.UI;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using static Kerberos_Client.MyStruct;
using System.Management;

namespace Kerberos_Client
{

    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        string ASIP = "127.0.0.1";
        string TGSIP = "127.0.0.1";
        string SERVERIP = "127.0.0.1";
        private static string Kc = "20002018";
        public static List<Login_User> User_Item;//数据源
        private bool flag = true;
        public static string localName;
        //public delegate void MyDelegate(string str);
        //private MyDelegate md;
        #region UI
        public MainWindow()
        {
            InitializeComponent();
#if kerberos
            certificateInit();
            while (!flag) ;
            KerberosInit();
            while (flag) ;
#endif
            init(true);
        }
        public MainWindow(bool login)
        {
            InitializeComponent();
            init(login);
        }

        /// <summary>
        /// 界面初始化
        /// </summary>
        private void init(bool b)
        {
            ManagementClass mc = new ManagementClass("Win32_Processor");
            ManagementObjectCollection moc = mc.GetInstances();
            foreach (ManagementObject mo in moc)
            {
               localName = mo.Properties["ProcessorId"].Value.ToString();
            }
            User_Item = new List<Login_User>();
            //绑定数据
            ID.ItemsSource = User_Item;
            ID.DisplayMemberPath = "Uid";
            StreamReader sr=null;
            if (File.Exists(@"../../conf/Login.conf"))
                sr = new StreamReader(@"../../conf/Login.conf");//读取登录界面配置文件
            else
            {
                string path = @"../../Image_Source\未登录头象.png";
                BitmapImage bi=img.GetBitmap(path);
                head_Image.Source = bi;
                return;
            }
            //恢复列表
            string json = string.Empty;
            while ((json = sr.ReadLine()) != null)
            {
                json = DESLibrary.DecryptDES(json, Kc);
                Login_User U = JsonHelper.FromJson<Login_User>(json);
                User_Item.Add(U);
            }
            ID.SelectedIndex =0;
            sr.Close();
            //判断是否需要自动登录
            Login_User u = ID.SelectedItem as Login_User;
            if (u.Automatic == true&&b)
                Login_Click(new object(), new RoutedEventArgs());
        }
        /// <summary>
        /// 保存界面信息
        /// </summary>
        private void Save_conf()
        {
            StreamWriter sw = new StreamWriter(@"../../conf/Login.conf");//写入登录界面配置文件
            foreach (object i in ID.Items)
            {
                //DES加密并写入配置文件
                Login_User U = new Login_User(i as Login_User);
                string json = JsonHelper.ToJson(U);
                json = DESLibrary.EncryptDES(json, Kc);
                sw.WriteLine(json);
            }
            sw.Close();
        }
        /// <summary>
        /// 超链接
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        private void Hyperlink_Click(object sender, RoutedEventArgs e)
        {
            // Hyperlink link = sender as Hyperlink;
            //Process.Start(new ProcessStartInfo(link.NavigateUri.AbsoluteUri));
            Process.Start(new ProcessStartInfo(@"../../../../\Web-Server\index.html"));
        }
        /// <summary>
        /// 关闭按钮
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        private void Close_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
        /// <summary>
        /// 用户选项改变，更改数据
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        private void ID_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            password.Password = string.Empty;
            Login_User u = ID.SelectedItem as Login_User;
            if (ID.SelectedItem == null)
            {
                key_Check.IsChecked = false;
                login_Check.IsChecked = false;
            }
            else
            {
                key_Check.IsChecked = u.Remember;
                login_Check.IsChecked = u.Automatic;
            }
            if (key_Check.IsChecked == true)
                password.Password = u.Psswd;

            string path = string.Empty;
            if (ID.SelectedItem == null)
            {
                path = @"../../Image_Source\未登录头象.png";
            }
            else
            {
                path = (ID.SelectedItem as Login_User).Photo;
                if (!File.Exists(path))
                    path = @"../../Image_Source\未登录头象.png";
                u.Photo = path;
            }
            BitmapImage bi= img.GetBitmap(path);
            head_Image.Source = bi;
        }
        /// <summary>
        /// 移动窗口
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        private void Window_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            DragMove();
        }
        /// <summary>
        /// 自动登录被选中时，一定会记住密码
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        private void login_Check_Checked(object sender, RoutedEventArgs e)
        {
            key_Check.IsChecked = true;
        }

        private void Cancle_Click(object sender, RoutedEventArgs e)
        {
            Main_Tab.IsSelected = true;
        }
        #endregion
        #region Kerberos
        private void certificateInit()
        {
            flag = false;

            //生成证书
            string PKB, PKI;
            RSALibrary.RSAKey(out PKI, out PKB);
            Main_Window.Keys["public"] = PKB;
            Main_Window.Keys["private"] = PKI;
            Certificate certificate = new Certificate();
            DateTime dt = new DateTime();
            certificate.Deadline= dt.AddDays(1).ToString();   //加n天
            certificate.Pk = RSALibrary.RSAPublicKeyDotNet2Java(PKB);
            certificate.Name = localName;
            certificate.Serial = "0";
            certificate.Version = "1.0";



            //发送报文
            string extend = JsonHelper.ToJson(certificate);
            Order order = new Order();
            order.MsgType = "0001";
            order.Extend = extend;
            order.Src = localName;
            order.Dst = "AS";
            string json = JsonHelper.ToJson(order);
#if test
            ConnectServer.connectserver(this, ASIP);
#endif
            ConnectServer.sendMessage(json);

            order.Dst = "Server";
            json = JsonHelper.ToJson(order);
#if test
            ConnectServer.connectserver(this, SERVERIP);
#endif
            ConnectServer.sendMessage(json);
        }
        /// <summary>
        /// 登录
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        private void Login_Click(object sender, RoutedEventArgs e)
        {
            if (string.Empty.Equals(ID.Text) || string.Empty.Equals(password.Password))
                MessageBox.Show("账号或密码为空，请重新输入");
            else
            {
                //发送报文
                User user = new User(ID.Text, password.Password);
                string extend = JsonHelper.ToJson(user);
                Order order = new Order();
                order.MsgType = "1002";
                order.Src = localName;
                order.Dst = "Server";
#if DES
                extend = DESLibrary.EncryptDES(extend, Main_Window.Keys["server"]);
#endif
                order.Extend = extend;
                string json = JsonHelper.ToJson(order);
#if test
                ConnectServer.connectserver(this, SERVERIP);
#endif
                ConnectServer.sendMessage(json);
                Load_Tab.IsSelected = true;
            }
        }
        private void KerberosInit()
        {
            flag = false;
            //发送报文
            Message1 msg = new Message1();
            msg.IDC = localName;
            msg.IDT = "TGS";
            msg.TS= DateTime.Now.ToString();
            string extend = JsonHelper.ToJson(msg);
#if DES
            extend = DESLibrary.EncryptDES(extend, Main_Window.Keys["AS"]);
#endif
            Order order = new Order();
            order.Dst = "AS";
            order.Src = localName;
            order.MsgType = "0003";
            order.Extend = extend;
            string json = JsonHelper.ToJson(order);
#if test
            ConnectServer.connectserver(this, ASIP);
#endif
            ConnectServer.sendMessage(json);
        }
        internal void Call_certificate(Order o)
        {
            Order order = o;
            Certificate certificate = JsonHelper.FromJson<Certificate>(order.Extend);
            string kc = certificate.Pk;
            kc = RSALibrary.RSAPublicKeyJava2DotNet(kc);
            Main_Window.Keys["server_pk"] = kc;
            flag = true;
        }
        internal void Call_Key(Order o)
        {
            Order order = o;
#if RSA
            order.Extend = RSALibrary.RSADecrypt(order.Extend, Main_Window.Keys["private"]);
#endif
            Main_Window.Keys["as"] = order.Extend;
            flag = true;
        }
        internal void Call_AS(Order o)
        {
#if DES
            o.Extend = DESLibrary.DecryptDES(o.Extend, Main_Window.Keys["as"]);
#endif
            Message2 message2 = JsonHelper.FromJson<Message2>(o.Extend);

            Main_Window.Keys["tgs"] = message2.Key;

            Message3 msg = new Message3();
            msg.T = message2.T;
            msg.IDV = "Server";
            Authenticator au = new Authenticator();
            au.IDC = localName;
            au.ADC = DoGetHostEntry();
            au.TS= DateTime.Now.ToString();
            msg.AC = JsonHelper.ToJson(au);
#if DES
            msg.AC = DESLibrary.DecryptDES(o.Extend, Main_Window.Keys["tgs"]);
#endif
            string extend = JsonHelper.ToJson(msg);
            Order order = new Order();
            order.Dst = "TGS";
            order.Src = localName;
            order.MsgType = "0005";
            order.Extend = extend;
            string json = JsonHelper.ToJson(order);
#if test
            ConnectServer.connectserver(this, TGSIP);
#endif
            ConnectServer.sendMessage(json);
        }
        internal void Call_TGS(Order o)
        {
#if DES
            o.Extend= DESLibrary.DecryptDES(o.Extend, Main_Window.Keys["tgs"]);
#endif
            Message4 message4 = JsonHelper.FromJson<Message4>(o.Extend);

            Main_Window.Keys["server"] = message4.Key;

            Message5 msg = new Message5();
            msg.T = message4.T;
            Authenticator au = new Authenticator();
            au.IDC = localName;
            au.ADC = DoGetHostEntry();
            au.TS = DateTime.Now.ToString();
            msg.AC = JsonHelper.ToJson(au);

            string extend = JsonHelper.ToJson(msg);
            Order order = new Order();
            order.Dst = "SERVER";
            order.Src = localName;
            order.MsgType = "0007";
            order.Extend = extend;
            string json = JsonHelper.ToJson(order);
#if test
            ConnectServer.connectserver(this, SERVERIP);
#endif
            ConnectServer.sendMessage(json);
        }
        internal void Call_Server(Order o)
        {
            Message6 message6 = JsonHelper.FromJson<Message6>(o.Extend);
            flag = true;
        }
        #endregion
        internal void Call_check_User(Order o)
        {
#if DES
            o.Extend = DESLibrary.DecryptDES(o.Extend, Main_Window.Keys["server"]);
#endif
            Thread.Sleep(3 * 1000);
            if (o.StatusReport == false)
            {
                MessageBox.Show("登录信息有误，请重新输入！");
                return;
            }
            Dispatcher.Invoke(new Action(delegate
            {
                //登录时，重新配置当前用户
                set_User();
                //保存配置文件
                Save_conf();
                User user = JsonHelper.FromJson<User>(o.Extend);
                Window U = new Main_Window(user);
                U.Show();
                //回收系统内存，否则多次切换后，内存爆了
                GC.Collect();
                this.Close();
            }));
        }
        public void set_User()
        {
            Login_User temp = ID.SelectedItem as Login_User;
            Login_User u;
            if (temp == null)
                u = new Login_User(ID.Text, password.Password, @"../../Image_Source\未登录头象.png", key_Check.IsChecked, login_Check.IsChecked);
            else
                u = new Login_User(ID.Text, password.Password, temp.Photo, key_Check.IsChecked, login_Check.IsChecked);
            if (key_Check.IsChecked == false)
                u.Psswd = string.Empty;
            User_Item.Remove(User_Item.Find(delegate (Login_User user) { return user.Uid.Equals(ID.Text); }));
            User_Item.Insert(0, u);
        }
        public static string DoGetHostEntry()
        {
            System.Net.IPHostEntry IpEntry = System.Net.Dns.GetHostEntry(System.Net.Dns.GetHostName());
            string localhostipv4Address = "";

            for (int i = 0; i != IpEntry.AddressList.Length; i++)
            {
                if (!IpEntry.AddressList[i].IsIPv6LinkLocal)
                {
                    localhostipv4Address = IpEntry.AddressList[i].ToString();
                    break;
                }
            }

            return localhostipv4Address;
        }
    }
}
