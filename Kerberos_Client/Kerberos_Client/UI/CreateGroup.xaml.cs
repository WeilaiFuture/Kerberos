using System;
using System.Collections.Generic;
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
using System.Windows.Shapes;
using static Kerberos_Client.MyStruct;

namespace Kerberos_Client.UI
{
    /// <summary>
    /// CreateGroup.xaml 的交互逻辑
    /// </summary>
    public partial class CreateGroup : Window
    {
        public static Dictionary<string, List<Friend>> Group_Dic = new Dictionary<string, List<Friend>>();
        public static bool live = false;
        public static List<Expander> ExpList = new List<Expander>();
        public User user;
        public CreateGroup(User u)
        {
            user = u;
            InitializeComponent();
            init();
        }
        void init()
        {
            foreach (object i in Main_Window.static_friends)
            {
                Friend friend = i as Friend;
                if (!Group_Dic.ContainsKey(friend.Tid))
                {
                    Group_Dic[friend.Tid] = new List<Friend>();
                    ListView listView = new ListView();
                    listView.HorizontalAlignment = HorizontalAlignment.Stretch;
                    listView.Style = FindResource("AddGroupView") as Style;
                    listView.Width = 280;
                    Expander my_Exp1 = new Expander();
                    my_Exp1.HorizontalAlignment = HorizontalAlignment.Stretch;
                    my_Exp1.Header = friend.Tid;
                    my_Exp1.Content = listView;
                    ExpList.Add(my_Exp1);
                }
                if (!Group_Dic[friend.Tid].Exists(delegate (Friend fri) { return fri.U.Uid.Equals(friend.U.Uid); }))
                    Group_Dic[friend.Tid].Add(friend);
            }
            foreach (var v in ExpList)
            {
                ListView listView = v.Content as ListView;
                listView.ItemsSource = null;
                listView.ItemsSource = Group_Dic[v.Header.ToString()];
            }
            Group_View.ItemsSource = null;
            Group_View.ItemsSource = ExpList;

        }


        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Group group = new Group();
            group.list = new List<User>();
            foreach (var v in ExpList)
            {
                ListView listView = v.Content as ListView;
                foreach (var t in listView.SelectedItems)
                {
                    group.list.Add(((Friend)t).U);
                }
            }
            group.list.Add(user);
            group.Photo = user.Photo;
            group.Leader = user.Uid;

            //发送报文
            Order order = new Order();
            order.MsgType = "2002";
            order.ContentType = "9003";
            order.Src = MainWindow.localName;
            order.Dst = "Server";
            MyStruct myStruct = new MyStruct();
            myStruct.group = group;
            myStruct.user = user;
            order.Extend = JsonHelper.ToJson(myStruct);
            order.Extend = DESLibrary.EncryptDES(order.Extend,Main_Window.Keys["server"]);
            ConnectServer.sendMessage(order);
        }
    }
}
