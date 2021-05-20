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
    /// User_Window.xaml 的交互逻辑
    /// </summary>
    public partial class User_Window : Window
    {
        private string uname;
        public string Uname
        {
            get
            {
                return uname;
            }
            set
            {
                if (Uname_TX.Text != value)
                {
                    Uid_TX.Text = string.Format("昵称: {0}", value);
                    PropertyChanged(this, new PropertyChangedEventArgs("Uname_TX.Text"));
                }
            }
        }
        private string uid;
        public string Uid
        {
            get
            {
                return uid;
            }
            set
            {
                if (Uid_TX.Text != value)
                {
                    Uid_TX.Text = string.Format("账号: {0}", value);
                    PropertyChanged(this, new PropertyChangedEventArgs("Uid_TX.Text"));
                }
            }
        }
        private string email;
        public string Email
        {
            get
            {
                return email;
            }
            set
            {
                if (Email_TX.Text != value)
                {
                    Uid_TX.Text = string.Format("邮箱: {0}", value);
                    PropertyChanged(this, new PropertyChangedEventArgs("Email_TX.Text"));
                }
            }
        }
        public event PropertyChangedEventHandler PropertyChanged = delegate { };
        public User_Window(User u, Image i)
        {
            InitializeComponent();
            Uname = u.Uname;
            Uid = u.Uid;
            Email = u.Email;
            head_Image = i;
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