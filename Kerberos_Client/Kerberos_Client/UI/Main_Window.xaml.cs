//#define RSA
#define des
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
        public static List<Chat_information> Message_List = new List<Chat_information>();
        public Add_Window Add_;
        public static Dictionary<string, string> Keys = new Dictionary<string, string>();
        private static List<Friend> Search_List = new List<Friend>();
        public static Dictionary<string, Window> Chat_Dic = new Dictionary<string, Window>();
        public static Dictionary<string, List<Friend>> Group_Dic = new Dictionary<string, List<Friend>>();
        public static bool live = false;
        public static List<Expander> ExpList = new List<Expander>();
        public static List<Friend> static_friends;
        public static List<Group> static_groups;
        public static Friend static_friend;
        public static Group static_group;
        public Main_Window(User u)
        {
            InitializeComponent();
            init(u);
            Request();
            Greet(); 
        }
        /// <summary>
        /// 界面初始化
        /// </summary>
        /// <param name="u">用户信息</param>
        /// <returns></returns>
        private void init(User u)
        {
            live = true;
            ConnectServer.HeartStart();
            My_user = u;
            message_List.ItemsSource = Message_List;
            search_List.ItemsSource = Search_List;
            name_Block.Text = u.Uname;
            sign_TextBox.Text = u.Sign;
            head_Image.Source = img.GetBitmap(u.Photo);

            FileStream sr = null;
            if (!Directory.Exists(@My_user.Uid))
                Directory.CreateDirectory(@My_user.Uid);
            string path = @My_user.Uid+"\\" + My_user.Uid +".txt";
            sr = new FileStream(path, FileMode.OpenOrCreate, FileAccess.ReadWrite);
            StreamReader streamReader = new StreamReader(sr);
            string json = string.Empty;
            while ((json = streamReader.ReadLine()) != null)
            {
                Chat_information U = JsonHelper.FromJson1<Chat_information>(json);
                if (!Message_List.Exists(delegate (Chat_information information)
                {
                    return information.Id.Equals(U.Id);
                })) 
                    Message_List.Add(U);
            }
            message_List.ItemsSource = null;
            message_List.ItemsSource = Message_List;
            streamReader.Close();
            sr.Close();
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
            this.Close();
            Window w = new MainWindow(false);
            w.Show();
            live = false;
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
            Chat_information chat = (sender as DataGrid).SelectedItem as Chat_information;
            Friend u = static_friends.Find(delegate (Friend friend) { return friend.U.Uid.Equals(chat.Id); });
            if (u != null)
            {
                User temp = u.U;
                Dispatcher.Invoke(new Action(delegate
                {

                    Window u;
                    if (Chat_Dic.ContainsKey(temp.Uid))
                        u = Chat_Dic[temp.Uid];
                    else
                    {
                        u = new friend_Chat(temp, My_user, this);
                        Chat_Dic[temp.Uid] = u;
                    }
                    Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                    newWindowThread.SetApartmentState(ApartmentState.STA);
                    newWindowThread.IsBackground = true;
                    newWindowThread.Start();
                }));
            }
            else
            {
                Group group=static_groups.Find(delegate (Group g) { return g.Gid.Equals(chat.Id); });
                Dispatcher.Invoke(new Action(delegate
                {

                    Window u;
                    if (Chat_Dic.ContainsKey(group.Gid))
                        u = Chat_Dic[group.Gid];
                    else
                    {
                        u = new group_Chat(group,My_user, this);
                        Chat_Dic[group.Gid] = u;
                    }
                    Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                    newWindowThread.SetApartmentState(ApartmentState.STA);
                    newWindowThread.IsBackground = true;
                    newWindowThread.Start();
                }));
            }
        }

        /// <summary>
        /// 双击还有聊天
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void friend_List_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if ((sender as ListView).SelectedItem == null)
                return;
            Friend friend = (sender as ListView).SelectedItem as Friend;
            User temp = friend.U;
            Dispatcher.Invoke(new Action(delegate
            {

                Window u;
                if (Chat_Dic.ContainsKey(temp.Uid))
                    u = Chat_Dic[temp.Uid];
                else
                {
                    u = new friend_Chat(temp, My_user,this);
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
        private void group_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if ((sender as ListView).SelectedItem == null)
                return;
            Group group = (sender as ListView).SelectedItem as Group;
            Dispatcher.Invoke(new Action(delegate
            {

                Window u;
                if (Chat_Dic.ContainsKey(group.Gid))
                    u = Chat_Dic[group.Gid];
                else
                {
                    u = new group_Chat(group, My_user, this);
                    Chat_Dic[group.Gid] = u;
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
                foreach (var o in Group_Dic)
                {
                    List<Friend> friends = o.Value;
                    foreach (object i in friends)
                    {
                        Friend friend = i as Friend;
                        User user = friend.U;
                        if (user.Uname.Contains(temp) || user.Uid.Contains(temp) || user.Sign.Contains(temp))
                        {
                            Search_List.Add(friend);
                        }
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
            MyStruct myStruct = new MyStruct();
            order.Extend = JsonHelper.ToJson(myStruct);
#if des
            order.Extend = DESLibrary.EncryptDES(order.Extend, Keys["server"]);
#endif
            ConnectServer.sendMessage(order);
        }

        internal void Call_Friend(Order o)
        {
#if des
            o.Extend = DESLibrary.DecryptDES(o.Extend, Keys["server"]);
#endif
            MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
            static_friends = myStruct.friendlist;
            static_groups = myStruct.groups;
            ExpList.Clear();
            Group_Dic.Clear();

            Dispatcher.Invoke(new Action(delegate
            {
                foreach (object i in static_friends)
                {
                    Friend friend = i as Friend;
                    if (!Group_Dic.ContainsKey(friend.Tid))
                    {
                        Group_Dic[friend.Tid] = new List<Friend>();
                        ListView listView = new ListView();
                        listView.HorizontalAlignment = HorizontalAlignment.Stretch;
                        listView.Style = FindResource("FriendListView") as Style;
                        listView.Width = 280;
                        listView.ContextMenu = GetContext();

                        listView.SelectionChanged += friend_select_change;
                        listView.MouseDoubleClick += friend_List_MouseDoubleClick;
                        Expander my_Exp1 = new Expander();
                        my_Exp1.HorizontalAlignment= HorizontalAlignment.Stretch;
                        my_Exp1.Header = friend.Tid;
                        my_Exp1.Content = listView;
                        ExpList.Add(my_Exp1);
                    }
                    if(!Group_Dic[friend.Tid].Exists(delegate (Friend fri) { return fri.U.Uid.Equals(friend.U.Uid); }))
                        Group_Dic[friend.Tid].Add(friend);
                }
                ListView View = new ListView();
                View.HorizontalAlignment = HorizontalAlignment.Stretch;
                View.Style = FindResource("GroupView") as Style;
                View.Width = 280;
                View.ContextMenu = GetgroupContext();
                View.MouseDoubleClick += group_MouseDoubleClick;
                View.SelectionChanged += group_select_change;
                View.ItemsSource = null;
                View.ItemsSource = static_groups;
                Expander my_Exp = new Expander();
                my_Exp.HorizontalAlignment = HorizontalAlignment.Stretch;
                if(static_groups!=null)
                    my_Exp.Header = "我的群聊("+ static_groups.Count.ToString()+")";
                else
                    my_Exp.Header = "我的群聊(0)";
                my_Exp.Content = View;
                ExpList.Add(my_Exp);
                foreach (var v in ExpList)
                {
                    if (v.Header.ToString().Contains("我的群聊"))
                        continue;
                    ListView listView = v.Content as ListView;
                    listView.ItemsSource = null;
                    listView.ItemsSource = Group_Dic[v.Header.ToString()];
                }
                Group_View.ItemsSource = null;
                Group_View.ItemsSource = ExpList;
            }));
            //Dispatcher.Invoke(new Action(delegate
            //{

            //    ListView friend_List = my_Exp1.Content as ListView;
            //    friend_List.ItemsSource = null;
            //    friend_List.ItemsSource = Friend_List;
            //    my_Exp1.Header = "我的好友 " + Friend_List.Count();
            //}));
        }

        internal void Greet()
        {
            //发送报文
            Order order = new Order();
            order.MsgType = "1005";
            order.Src = MainWindow.localName;
            order.Dst = "Server";
            MyStruct myStruct = new MyStruct();
            order.Extend = JsonHelper.ToJson(myStruct);

#if des
            order.Extend = DESLibrary.EncryptDES(order.Extend, Keys["server"]);
#endif
            ConnectServer.sendMessage(order);
        }
        internal void Call_Greet(Order o)
        {
#if DES
            o.Extend = DESLibrary.DecryptDES(o.Extend, Keys["server"]);
#endif
            Request();
        }
        internal void Call_Message(Order o)
        {
            Dispatcher.Invoke(new Action(delegate
            {
                Friend user = new Friend();
                foreach (var v in Group_Dic)
                {
                    List<Friend> friends = v.Value;
                    if (friends.Exists(delegate (Friend friend) { return friend.U.Uid.Equals(o.Src); }))
                    {
                        user = friends.Find(delegate (Friend friend) { return friend.U.Uid.Equals(o.Src); });
                        break;
                    }
                }
                friend_Chat u;
                if (Chat_Dic.ContainsKey(o.Src))
                    u =(friend_Chat)Chat_Dic[o.Src];
                else
                {
                    u = new friend_Chat(user.U, My_user,this);
                    Chat_Dic[o.Src] = u;
                    Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                    newWindowThread.SetApartmentState(ApartmentState.STA);
                    newWindowThread.IsBackground = true;
                    newWindowThread.Start();
                }
                o.Extend = DESLibrary.DecryptDES(o.Extend, Main_Window.Keys["server"]);
                MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
                Chat_Message chat_Message = myStruct.chat_message;
                Chat_information record = Message_List.Find(delegate (Chat_information record_) { return record_.Id.Equals(chat_Message.U.Uid); });
                if (record != null)
                {
                    record.Add(chat_Message);
                }
                else
                {
                    record = new Person_Chat(chat_Message.U.Photo, chat_Message.U.Uname, chat_Message.U.Uid);
                    record.Add(chat_Message);
                    Main_Window.Message_List.Add(record);
                }
                message_List.ItemsSource = null;
                message_List.ItemsSource = Message_List;
                u.chatMessage.Add(new ChatMessage()
                {
                    Name = user.U.Uname,

                    Time =new DateTime(chat_Message.Time).ToString(),
                    Photo =user.U.Photo ,
                    Message = myStruct.chat_message.Content,
                    MessageLocation = TypeLocalMessageLocation.chatRecv
                }); ;
                u.ListBoxChat.ScrollIntoView(u.ListBoxChat.Items[u.ListBoxChat.Items.Count - 1]);
            }));
        }
        internal void Call_groupMessage(Order o)
        {

            Dispatcher.Invoke(new Action(delegate
            {
                o.Extend = DESLibrary.DecryptDES(o.Extend, Main_Window.Keys["server"]);
                MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
                Group group = static_groups.Find(delegate (Group g) { return g.Gid.Equals(myStruct.group.Gid); });

                group_Chat u;
                if (Chat_Dic.ContainsKey(myStruct.group.Gid))
                    u =(group_Chat) Chat_Dic[myStruct.group.Gid];
                else
                {
                    u = new group_Chat(group,My_user,this);
                    Chat_Dic[o.Src] = u;
                    Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                    newWindowThread.SetApartmentState(ApartmentState.STA);
                    newWindowThread.IsBackground = true;
                    newWindowThread.Start();
                }
                Chat_Message chat_Message = myStruct.chat_message;
                Chat_information record = Message_List.Find(delegate (Chat_information record_) { return record_.Id.Equals(chat_Message.U.Uid); });
                if (record != null)
                {
                    record.Add(chat_Message);
                }
                else
                {
                    record = new Person_Chat(chat_Message.U.Photo, chat_Message.U.Uname, chat_Message.U.Uid);
                    record.Add(chat_Message);
                    Main_Window.Message_List.Add(record);
                }
                message_List.ItemsSource = null;
                message_List.ItemsSource = Message_List;
                u.chatMessage.Add(new ChatMessage()
                {
                    Name =myStruct.user.Uname,
                    Time = new DateTime(chat_Message.Time).ToString(),
                    Photo = myStruct.chat_message.U.Photo,
                    Message = myStruct.chat_message.Content,
                    MessageLocation = TypeLocalMessageLocation.chatRecv
                }); ;
                u.ListBoxChat.ScrollIntoView(u.ListBoxChat.Items[u.ListBoxChat.Items.Count - 1]);
            }));
        }

        internal void Update(MyStruct myStruct)
        {
            Dispatcher.Invoke(new Action(delegate
            {
                Chat_Message chat_Message = myStruct.chat_message;
                Chat_information record = Main_Window.Message_List.Find
                (delegate (Chat_information record_)
                { return record_.Id.Equals(chat_Message.U.Uid); });
                if (record != null)
                {
                    record.Add(chat_Message);
                }
                else
                {
                    record = new Person_Chat(chat_Message.U.Photo, chat_Message.U.Uname, chat_Message.U.Uid);
                    record.Add(chat_Message);
                    Message_List.Add(record);
                }
                message_List.ItemsSource = null;
                message_List.ItemsSource = Message_List;
            }));
        }
        internal void ReCall_Friend(Order o)
        {
#if des
            o.Extend = DESLibrary.DecryptDES(o.Extend, Keys["server"]);
#endif
            Order order = new Order();
            MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
            if (MessageBox.Show("ID:"+myStruct.user.Uid+"\n"+ "昵称:" + myStruct.user.Uname + "\n"+"请求添加好友", "请求", MessageBoxButton.YesNo, MessageBoxImage.Information) == MessageBoxResult.Yes)
            {
                order.ContentType = "9006";
            }
            else
                order.ContentType = "9007";

            order.MsgType = "2001";
            order.Src = MainWindow.localName;
            order.Dst = o.Src;
            MyStruct myStruct_ = new MyStruct();
            myStruct_.friend = new Friend();
            myStruct_.friend.Tid = "1";
            myStruct_.user = My_user;
            myStruct_.friend.U = myStruct.user;
            order.Extend = JsonHelper.ToJson(myStruct_);

#if des
            order.Extend = DESLibrary.EncryptDES(order.Extend, Keys["server"]);
#endif
            Request();
            ConnectServer.sendMessage(order);
        }
        internal void Call_Result(Order o)
        {
#if des
            o.Extend = DESLibrary.DecryptDES(o.Extend, Keys["server"]);
#endif
            MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
            if (o.ContentType == "9006")
                MessageBox.Show("ID:" + myStruct.user.Uid + "\n" + "昵称:" + myStruct.user.Uname + "\n" + "同意了添加好友", "回复");
            else
                MessageBox.Show("ID:" + myStruct.user.Uid + "\n" + "昵称:" + myStruct.user.Uname + "\n" + "拒绝了添加好友", "回复");
            Request();
        }


        internal void ReCall_Add_Group(Order o)
        {
#if des
            o.Extend = DESLibrary.DecryptDES(o.Extend, Keys["server"]);
#endif
            Order order = new Order();
            MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
            if (MessageBox.Show("ID:" + myStruct.user.Uid + "\n" + "昵称:" + myStruct.user.Uname + "\n" + "请求加入群聊："+ myStruct.group.Gid, "请求", MessageBoxButton.YesNo, MessageBoxImage.Information) == MessageBoxResult.Yes)
            {
                order.ContentType = "9008";
            }
            else
                order.ContentType = "9009";

            order.MsgType = "2002";
            order.Src = MainWindow.localName;
            order.Dst = o.Src;
            MyStruct myStruct_ = new MyStruct();
            myStruct_.friend = new Friend();
            myStruct_.friend.Tid = "1";
            myStruct_.user = My_user;
            myStruct_.friend.U = myStruct.user;
            order.Extend = JsonHelper.ToJson(myStruct_);
#if des
            order.Extend = DESLibrary.EncryptDES(order.Extend, Keys["server"]);
#endif
            Request();
            ConnectServer.sendMessage(order);
        }
        internal void Call_Result_Group(Order o)
        {
#if des
            o.Extend = DESLibrary.DecryptDES(o.Extend, Keys["server"]);
#endif
            MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
            if (o.ContentType == "9008")
                MessageBox.Show("ID:" + myStruct.user.Uid + "\n" + "昵称:" + myStruct.user.Uname + "\n" + "同意了加群:"+myStruct.group.Gid, "回复");
            else
                MessageBox.Show("ID:" + myStruct.user.Uid + "\n" + "昵称:" + myStruct.user.Uname + "\n" + "拒绝了加群:" + myStruct.group.Gid, "回复");
            Request();
        }
        internal void Call_Request_Group(Order o)
        {
#if des
            o.Extend = DESLibrary.DecryptDES(o.Extend, Keys["server"]);
#endif
            Order order = new Order();
            MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
            if (MessageBox.Show("ID:" + myStruct.user.Uid + "\n" + "昵称:" + myStruct.user.Uname + "\n" + "邀请你加入群聊：" + myStruct.group.Gid, "请求", MessageBoxButton.YesNo, MessageBoxImage.Information) == MessageBoxResult.Yes)
            {
                order.ContentType = "9008";
            }
            else
                order.ContentType = "9009";

            order.MsgType = "2002";
            order.Src = MainWindow.localName;
            order.Dst = o.Src;
            MyStruct myStruct_ = new MyStruct();
            myStruct_.user = My_user;
            myStruct_.group = myStruct.group;
            order.Extend = JsonHelper.ToJson(myStruct_);

#if des
            order.Extend = DESLibrary.EncryptDES(order.Extend, Keys["server"]);
#endif
            Request();
            ConnectServer.sendMessage(order);
        }

        protected override void OnClosing(CancelEventArgs e)
        {
            //发送报文
            Order order = new Order();
            order.MsgType = "1008";
            order.Src = MainWindow.localName;
            order.Dst = "Server";
            MyStruct myStruct = new MyStruct();
            myStruct.user = My_user;
            order.Extend = JsonHelper.ToJson(myStruct);

#if des
            order.Extend = DESLibrary.EncryptDES(order.Extend, Keys["server"]);
#endif
            ConnectServer.sendMessage(order);
            FileStream sr = null;
            string path = @My_user.Uid + "\\" + My_user.Uid + ".txt";
            sr = new FileStream(path, FileMode.Truncate, FileAccess.ReadWrite);
            StreamWriter sw = new StreamWriter(sr);
            string json = string.Empty;
            foreach (object i in Message_List)
            {
                Chat_information m = i as Chat_information;
                json = JsonHelper.ToJson1(m);
                sw.WriteLine(json);
            }
            sw.Close();
            sr.Close();
            base.OnClosing(e);
        }

        private void Delete_Click(object sender, RoutedEventArgs e)
        {
            //发送报文
            Order order = new Order();
            order.MsgType = "2001";
            order.Src = MainWindow.localName;
            order.Dst = "Server";
            order.ContentType = "9005";
            MyStruct myStruct = new MyStruct();



            Friend friend = static_friend;
            myStruct.friend = friend;
            order.Extend = JsonHelper.ToJson(myStruct);
#if des
            order.Extend = DESLibrary.EncryptDES(order.Extend, Keys["server"]);
#endif
            ConnectServer.sendMessage(order);
        }

        private void Refresh_Click(object sender, RoutedEventArgs e)
        {
            Request();
        }
        private void Move_Click(object sender, RoutedEventArgs e)
        {
            //发送报文
            Order order = new Order();
            order.MsgType = "2001";
            order.Src = MainWindow.localName;
            order.Dst = "Server";
            order.ContentType = "9010";
            MyStruct myStruct = new MyStruct();

            Friend friend = static_friend;
            friend.Tid = (sender as MenuItem).Header.ToString();

            myStruct.friend = friend;
            order.Extend = JsonHelper.ToJson(myStruct);
#if des
            order.Extend = DESLibrary.EncryptDES(order.Extend, Keys["server"]);
#endif
            ConnectServer.sendMessage(order);
        }
        private void AddMove_Click(object sender, RoutedEventArgs e)
        {
            //发送报文
            Order order = new Order();
            order.MsgType = "2001";
            order.Src = MainWindow.localName;
            order.Dst = "Server";
            order.ContentType = "9010";
            MyStruct myStruct = new MyStruct();
            Friend friend = static_friend;
            friend.Tid =(((sender as Button).Parent as Grid).Children[0] as TextBox).Text;

            myStruct.friend = friend;
            myStruct.user = My_user;
            order.Extend = JsonHelper.ToJson(myStruct);
#if des
            order.Extend = DESLibrary.EncryptDES(order.Extend, Keys["server"]);
#endif
            ConnectServer.sendMessage(order);
        }
        private ContextMenu GetContext()
        {
            ContextMenu cm = new ContextMenu();
            MenuItem menu = new MenuItem();
            menu.Header = "删除好友";
            menu.Click += Delete_Click;
            Separator separator = new Separator();
            cm.Items.Add(menu);
            cm.Items.Add(new Separator());
            MenuItem menu1 = new MenuItem();
            menu1.Header = "刷新";
            menu1.Click += Refresh_Click;
            cm.Items.Add(menu1);
            cm.Items.Add(new Separator());
            MenuItem menu2 = new MenuItem();
            menu2.Header = "移动至分组";
            menu2.MouseEnter += MenuMouseEnter;
            //menu2.MouseLeave += MenuMouseLeave;
            cm.Items.Add(menu2);
            return cm;
        }

        private ContextMenu GetgroupContext()
        {
            ContextMenu cm = new ContextMenu();
            MenuItem menu = new MenuItem();
            menu.Header = "退出群聊";
            menu.Click += Delete_groupClick;
            Separator separator = new Separator();
            cm.Items.Add(menu);
            cm.Items.Add(new Separator());
            MenuItem menu1 = new MenuItem();
            menu1.Header = "刷新";
            menu1.Click += Refresh_Click;
            cm.Items.Add(menu1);
            return cm;
        }
        private void Delete_groupClick(object sender, RoutedEventArgs e)
        {
            //发送报文
            Order order = new Order();
            order.MsgType = "2002";
            order.Src = MainWindow.localName;
            order.Dst = "Server";
            order.ContentType = "9005";
            MyStruct myStruct = new MyStruct();
            Group group = static_group;
            myStruct.group = group;
            order.Extend = JsonHelper.ToJson(myStruct);
#if des
            order.Extend = DESLibrary.EncryptDES(order.Extend, Keys["server"]);
#endif
            ConnectServer.sendMessage(order);
        }
        private void MenuMouseEnter(object sender, MouseEventArgs e)
        {
            MenuItem menuItem = sender as MenuItem;
            menuItem.Items.Clear();
            MenuItem m1 = new MenuItem();
            m1.Header = "新建分组";
            MenuItem menuItem1 = new MenuItem();

            Grid grid = new Grid();
            grid.ColumnDefinitions.Add(new ColumnDefinition());
            grid.ColumnDefinitions.Add(new ColumnDefinition());
            TextBox textBox = new TextBox();
            textBox.Name = "TB";
            textBox.Text = "新建分组";
            textBox.MinWidth = 80;
            Button button = new Button();
            button.Content = "确认";
            button.Click += AddMove_Click;
            grid.Children.Add(textBox);
            Grid.SetColumn(textBox, 0);
            grid.Children.Add(button);
            Grid.SetColumn(button, 1);

            m1.Items.Add(grid);
            menuItem.Items.Add(m1);
            //menuItem.Items.Add(new Separator());
            foreach (var v in ExpList)
            {
                MenuItem m = new MenuItem();
                m.Header = v.Header;
                m.Click += Move_Click;
                menuItem.Items.Add(m);
                //menuItem.Items.Add(new Separator());
            }
        }
        private void friend_select_change(object sender, RoutedEventArgs e)
        {       
            static_friend =(sender as ListView).SelectedItem as Friend;
            foreach (var v in ExpList)
            {
                if ((sender as ListView)!= v.Content as ListView)
                {
                    ListView listView = v.Content as ListView;
                    listView.SelectedItem = null;
                }
            }
        }
        private void group_select_change(object sender, RoutedEventArgs e)
        {
            static_group = (sender as ListView).SelectedItem as Group;
            foreach (var v in ExpList)
            {
                if ((sender as ListView) != v.Content as ListView)
                {
                    ListView listView = v.Content as ListView;
                    listView.SelectedItem = null;
                }
            }
        }

    }
}