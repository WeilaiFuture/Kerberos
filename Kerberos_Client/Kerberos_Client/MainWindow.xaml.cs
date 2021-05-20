using Kerberos_Client.UI;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
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

namespace Kerberos_Client
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        private string Kc = "20002018";
        public List<Login_User> User_Item;//数据源

        public MainWindow()
        {
            InitializeComponent();
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
            User_Item = new List<Login_User>();
            //绑定数据
            ID.ItemsSource = User_Item;
            ID.DisplayMemberPath = "Uid";
            StreamReader sr = new StreamReader(@"../../conf/Login.conf");//读取登录界面配置文件
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
        /// 登录
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        private void Login_Click(object sender, RoutedEventArgs e)
        {
            //登录时，重新配置当前用户
            Login_User u;
            u = new Login_User(ID.Text, password.Password, head_Image.Source.ToString(), key_Check.IsChecked, login_Check.IsChecked);
            if (key_Check.IsChecked == false)
                u.Psswd=string.Empty;
            User_Item.Remove(User_Item.Find(delegate (Login_User user) { return user.Uid.Equals(ID.Text); }));
            User_Item.Insert(0, u);
            //保存配置文件
            Save_conf();
            //接收报文
            User user1 = new User();
            Window U = new Main_Window(user1, head_Image);
            U.Show();
            //回收系统内存，否则多次切换后，内存爆了
            GC.Collect();
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
            key_Check.IsChecked = u.Remember;
            login_Check.IsChecked = u.Automatic;
            if (key_Check.IsChecked == true)
                password.Password = u.Psswd;

            BitmapImage bi = new BitmapImage();
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
            }
            using (MemoryStream ms = new MemoryStream(File.ReadAllBytes(path)))
            {
                bi = new BitmapImage();
                bi.BeginInit();
                bi.CacheOption = BitmapCacheOption.OnLoad;//设置缓存模式
                bi.StreamSource = ms;//通过StreamSource加载图片
                bi.EndInit();
                bi.Freeze();

            }
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
    }
}
