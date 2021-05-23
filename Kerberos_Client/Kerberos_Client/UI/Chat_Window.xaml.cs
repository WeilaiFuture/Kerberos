using System;
using System.Collections.Generic;
using System.ComponentModel;
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
    /// Chat_Window.xaml 的交互逻辑
    /// </summary>
    public partial class Chat_Window : Window
    {
        public User My_user;
        public Chat_Window(User u, Image i)
        {
            InitializeComponent();
            My_user = u;
            head_Image = i;
            Uname_TextBlock.Text = "昵称:" + My_user.Uname;
            Uid_TX.Text = "账号:" + My_user.Uid;
            Email_TX.Text = "邮箱:" + My_user.Email;
        }
        private void Button_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        private void Button_Click_1(object sender, RoutedEventArgs e)
        {

        }

    }
}
