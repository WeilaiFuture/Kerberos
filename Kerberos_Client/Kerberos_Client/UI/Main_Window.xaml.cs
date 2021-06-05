//#define RSA
//#define DES
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
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
        public static List<Friend> Friend_List = new List<Friend>();
        public static List<Chat_Message> Message_List = new List<Chat_Message>();
        public Add_Window Add_;
        public static Dictionary<string, string> Keys = new Dictionary<string, string>();
        private static List<User> Search_List = new List<User>();
        public static Dictionary<string, Chat_Window> Chat_Dic = new Dictionary<string, Chat_Window>();
        public static bool live = false;
        public Main_Window(User u)
        {
            Request();
            InitializeComponent();
            init(u);
        }
        /// <summary>
        /// 界面初始化
        /// </summary>
        /// <param name="u">用户信息</param>
        /// <returns></returns>
        private void init(User u)
        {
            live = true;
            for (int j = 0; j < 10; j++)
            {
                Friend uu = new Friend();
                uu.U = new User();
                uu.U.Uname = "LOCAL" + j.ToString();
                Friend_List.Add(uu);
                //Message_List.Add(uu);
            }
            ConnectServer.HeartStart();
            My_user = u;
            friend_List.ItemsSource = Friend_List;
            message_List.ItemsSource = Message_List;
            search_List.ItemsSource = Search_List;
            name_Block.Text = u.Uname;
            sign_TextBox.Text = u.Sign;
            head_Image.Source = img.GetBitmap(u.Photo);
            my_Exp1.Header += " " + Friend_List.Count();
        }

        #region 界面
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
            live = false;
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
        private void message_List_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if ((sender as DataGrid).SelectedItem == null)
                return;
            Chat_Message chat = (sender as DataGrid).SelectedItem as Chat_Message;
            User temp = chat.U;
            Dispatcher.Invoke(new Action(delegate
            {

                Chat_Window u;
                if (Chat_Dic.ContainsKey(temp.Uid))
                    u = Chat_Dic[temp.Uid];
                else
                {
                    u = new Chat_Window(temp,My_user);
                    Chat_Dic[temp.Uid] = u;
                }
                Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                newWindowThread.SetApartmentState(ApartmentState.STA);
                newWindowThread.IsBackground = true;
                newWindowThread.Start();
            }));
        }

        /// <summary>
        /// 双击还有聊天
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void friend_List_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if ((sender as DataGrid).SelectedItem == null)
                return;
            Friend friend = (sender as DataGrid).SelectedItem as Friend;
            User temp = friend.U;
            Dispatcher.Invoke(new Action(delegate
            {

                Chat_Window u;
                if (Chat_Dic.ContainsKey(temp.Uid))
                    u = Chat_Dic[temp.Uid];
                else
                {
                    u = new Chat_Window(temp, My_user);
                    Chat_Dic[temp.Uid] = u;
                }
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
                Add_= new Add_Window(My_user);
                Thread newWindowThread = new Thread(() => ThreadStartingPoint(Add_));
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
            if (e.Key == Key.Enter)
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
                search_List.ItemsSource = Search_List;
            }

        }
        #endregion
        internal void Request()
        {
            //发送报文
            Order order = new Order();
            order.MsgType = "1003";
            order.Src = MainWindow.localName;
            order.Dst = "Server";
            string extend = "Request";
#if RSA
            string sig = string.Empty;
            RSALibrary.SignatureFormatter(extend, Keys["private"], ref sig);
            order.Sign = sig;
#endif
#if DES
            extend = DESLibrary.EncryptDES(extend, Keys["server"]);
#endif
            order.Extend = extend;
            string json = JsonHelper.ToJson(order);
            ConnectServer.sendMessage(order);
        }

        internal void Call_Friend(Order o)
        {
#if DES
            o.Extend = DESLibrary.DecryptDES(o.Extend, Keys["server"]);
#endif
            MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
            List<Friend> users = myStruct.friendlist;
            Friend_List = users;
            Dispatcher.Invoke(new Action(delegate
            {
                friend_List.ItemsSource = null;
                friend_List.ItemsSource = Friend_List;
            }));
            Greet();
        }

        internal void Greet()
        {
            //发送报文
            Order order = new Order();
            order.MsgType = "1005";
            order.Src = MainWindow.localName;
            order.Dst = "Server";
            string extend = "Greet";
#if RSA
            string sig = string.Empty;
            RSALibrary.SignatureFormatter(extend, Keys["private"], ref sig);
            order.Sign = sig;
#endif
#if DES
            extend = DESLibrary.EncryptDES(extend, Keys["server"]);
#endif
            order.Extend = extend;
            string json = JsonHelper.ToJson(order);
            ConnectServer.sendMessage(order);
        }
        internal void Call_Greet(Order o)
        {
#if DES
            o.Extend = DESLibrary.DecryptDES(o.Extend, Keys["server"]);
#endif
            MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
            List<Chat_Message> chat = myStruct.record_message.Messages_list;
            Message_List = chat;
            Dispatcher.Invoke(new Action(delegate
            {
                message_List.ItemsSource = null;
                message_List.ItemsSource = Message_List;
            }));
        }
        internal void Call_Message(Order o)
        {
#if DES
            o.Extend = DESLibrary.DecryptDES(o.Extend, Keys["server"]);
#endif

            Dispatcher.Invoke(new Action(delegate
            {
                Friend user = Friend_List.Find(delegate (Friend friend)
                {
                    return friend.U.Uid.Equals(o.Src);
                });
                Chat_Window u;
                if (Chat_Dic.ContainsKey(o.Src))
                    u = Chat_Dic[o.Src];
                else
                {
                    u = new Chat_Window(user.U, My_user);
                    Chat_Dic[o.Src] = u;
                    Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                    newWindowThread.SetApartmentState(ApartmentState.STA);
                    newWindowThread.IsBackground = true;
                    newWindowThread.Start();
                }
                u.chatMessage.Add(new ChatMessage()
                {
                    Photo = @"E:\Kerberos\Kerberos_Client\Kerberos_Client\Image_Source\test.jpg",
                    Message = "send_test",
                    MessageLocation = TypeLocalMessageLocation.chatRecv
                }); ;
                u.ListBoxChat.ScrollIntoView(u.ListBoxChat.Items[u.ListBoxChat.Items.Count - 1]);
            }));
        }
    }
}