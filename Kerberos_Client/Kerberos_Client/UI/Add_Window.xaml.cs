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
    /// Add_Window.xaml 的交互逻辑
    /// </summary>
    public partial class Add_Window : Window
    {
        User my_User;
        static List<Friend> Search_List = new List<Friend>();
        public Add_Window(User u)
        {
            my_User = u;
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Order order = new Order();
            order.Dst = "Server";
            order.MsgType = "1007";
            order.Src = my_User.Uid;
            MyStruct myStruct = new MyStruct();
            myStruct.user = new User();
            myStruct.user.Uid = Account.Text;
            order.Extend =DESLibrary.EncryptDES( JsonHelper.ToJson(myStruct),Main_Window.Keys["server"]);
            ConnectServer.sendMessage(order);
        }
        private void Add_Click(object sender, RoutedEventArgs e)
        {
            Order order = new Order();
            order.Dst = "Server";
            order.MsgType = "2001";
            order.ContentType = "9001";
            order.Src = my_User.Uid;
            MyStruct myStruct = new MyStruct();
            myStruct.friend = search_List.SelectedItem as Friend;
            order.Extend = JsonHelper.ToJson(myStruct);
            ConnectServer.sendMessage(order);
        }
        internal void Call_Search(Order o)
        {
            MyStruct myStruct = JsonHelper.FromJson<MyStruct>(o.Extend);
            Search_List = myStruct.friendlist;
            search_List.ItemsSource = null;
            search_List.ItemsSource = Search_List;
        }
    }
}
