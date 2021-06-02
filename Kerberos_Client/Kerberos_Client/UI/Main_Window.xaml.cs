using System;
using System.Collections.Generic;
using System.ComponentModel;
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
using System.Windows.Shapes;
using static Kerberos_Client.MyStruct;

namespace Kerberos_Client.UI
{
    /// <summary>
    /// Main_Window.xaml 的交互逻辑
    /// </summary>
    public partial class Main_Window : Window
    {
        public User My_user;
        private static List<User> Friend_List = new List<User>();
        private static List<User> Message_List = new List<User>();
        private static List<User> Search_List = new List<User>();
        private static Dictionary<string, Window> Chat_Window = new Dictionary<string, Window>();
        public Main_Window(User u, Image i)
        {
            InitializeComponent();
            init(u,i);
        }
        #region 界面
        /// <summary>
        /// 界面初始化
        /// </summary>
        /// <param name="u">用户信息</param>
        /// <param name="i">用户头像</param>
        /// <returns></returns>
        private void init(User u,Image i)
        {
            for (int j = 0; j < 10; j++)
            {
                User uu = new User();
                uu.Uname = "test" + j.ToString();
                Friend_List.Add(uu);
                Message_List.Add(uu);
            }

            My_user = u;
            friend_List.ItemsSource = Friend_List;
            message_List.ItemsSource = Message_List;
            search_List.ItemsSource = Search_List;
            name_Block.Text = u.Name;
            sign_TextBox.Text = u.Sign;
            head_Image.Source = i.Source;
            my_Exp1.Header += " " + Friend_List.Count();
        }
        /// <summary>
        /// 关闭界面
        /// </summary>
        private void Close_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
        /// <summary>
        /// 切换账号回到主界面
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void TransID_Click(object sender, RoutedEventArgs e)
        {
            Window w = new MainWindow(false);
            w.Show();
            this.Close();
        }
        /// <summary>
        /// 个性签名失去焦点
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void Tb_LostFocus(object sender, RoutedEventArgs e)
        {
            TextBox tb = sender as TextBox;
            tb.BorderThickness = new Thickness(0);
            tb.Background = Brushes.Transparent;
            tb.IsReadOnly = true;
        }
        /// <summary>
        /// 个性签名获得焦点
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void Tb_GotFocus(object sender, RoutedEventArgs e)
        {
            TextBox tb = sender as TextBox;
            tb.BorderThickness = new Thickness(1);
            tb.Background = Brushes.White;
            tb.IsReadOnly = false;
        }

        /// <summary>
        /// 双击还有聊天
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void friend_List_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            Dispatcher.Invoke(new Action(delegate
            {
                User temp = (sender as DataGrid).SelectedItem as User;
                Image image = new Image();
                BitmapImage bi = new BitmapImage();
                string path = temp.Photo;

                if (!File.Exists(path))
                    path = @"../../Image_Source\未登录头象.png";
                using (MemoryStream ms = new MemoryStream(File.ReadAllBytes(path)))
                {
                    bi = new BitmapImage();
                    bi.BeginInit();
                    bi.CacheOption = BitmapCacheOption.OnLoad;//设置缓存模式
                    bi.StreamSource = ms;//通过StreamSource加载图片
                    bi.EndInit();
                    bi.Freeze();

                }
                image.Source = bi;
                Chat_Window u = new Chat_Window(temp, image);
                Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                newWindowThread.SetApartmentState(ApartmentState.STA);
                newWindowThread.IsBackground = true;
                newWindowThread.Start();
            }));
        }
        /// <summary>
        /// 线程启动界面
        /// </summary>
        /// <param name="w">界面</param>
        /// <returns></returns>
        private void ThreadStartingPoint(Window w)
        {
            Dispatcher.Invoke(new Action(delegate
            {
                w.Show();
            }));
            System.Windows.Threading.Dispatcher.Run();
        }
        /// <summary>
        /// 双击头像查看个人界面
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void head_Image_MouseDown(object sender, MouseButtonEventArgs e)
        {
            if (e.ChangedButton == MouseButton.Left && e.ClickCount == 2)
            {
                Dispatcher.Invoke(new Action(delegate
                {
                    User_Window u = new User_Window(My_user, head_Image);
                    Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                    newWindowThread.SetApartmentState(ApartmentState.STA);
                    newWindowThread.IsBackground = true;
                    newWindowThread.Start();
                }));
            }
        }
        /// <summary>
        /// 切换到信息列表
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void message_Button_Click(object sender, RoutedEventArgs e)
        {
            Dispatcher.Invoke(new Action(delegate
            {
                message_Button.Background = Brushes.SkyBlue;
                friend_Button.Background = Brushes.Transparent;
                message_Button.BorderThickness = new Thickness(0, 0, 0, 4);
                friend_Button.BorderThickness = new Thickness(0, 0, 0, 0);
                Message_Tab.IsSelected = true;
            }));
        }
        /// <summary>
        /// 切换到好友列表
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void friend_Button_Click(object sender, RoutedEventArgs e)
        {
            Dispatcher.Invoke(new Action(delegate
            {
                friend_Button.Background = Brushes.SkyBlue;
                message_Button.Background = Brushes.Transparent;
                message_Button.BorderThickness = new Thickness(0, 0, 0, 0);
                friend_Button.BorderThickness = new Thickness(0, 0, 0, 4);
                Friend_Tab.IsSelected = true;
            }));
        }
        /// <summary>
        /// 鼠标按住拖动界面，以及单击时间
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void Window_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            if (sign_TextBox.IsFocused)
            {
                My_grid.Focus();
            }
            DragMove();
        }

        private void Add_Click(object sender, RoutedEventArgs e)
        {
            Dispatcher.Invoke(new Action(delegate
            {
                Add_Window u = new Add_Window(My_user);
                Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                newWindowThread.SetApartmentState(ApartmentState.STA);
                newWindowThread.IsBackground = true;
                newWindowThread.Start();
            }));
        }
        /// <summary>
        /// 搜索框
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void search_TextBox_KeyUp(object sender, KeyEventArgs e)
        {
            if(e.Key ==Key.Enter)
            {
                search_TextBox_TextChanged(sender, null);
            }
        }
        /// <summary>
        /// 搜索框
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void search_TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            string temp = search_TextBox.Text;
            if (string.Empty.Equals(temp))
            {
                Message_Tab.IsSelected = true;
            }
            else
            {
                Search_List.Clear();
                foreach (object i in Friend_List)
                {
                    if ((i as User).Uname.Contains(temp) || (i as User).Uid.Contains(temp) || (i as User).Sign.Contains(temp))
                    {
                        Search_List.Add(i as User);
                    }
                }
                search_TabItem.IsSelected = true;
                search_List.ItemsSource = null;
                search_List.ItemsSource=Search_List;
            }
           
        }
        #endregion


    }
}
