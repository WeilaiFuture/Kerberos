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
using static Client.MyStruct;
namespace Client
{
    /// <summary>
    /// User.xaml 的交互逻辑
    /// </summary>
    public partial class User_Window : Window
    {
        private string name;
        public string Name
        {
            get
            {
                return name;
            }
            set
            {
                if (name_Block.Text != value)
                {
                    name_Block.Text = value;
                    PropertyChanged(this, new PropertyChangedEventArgs("name_Block.Text"));
                }
            }
        }

        public event PropertyChangedEventHandler PropertyChanged = delegate { };

    public User_Window(User u,Image i)
        {
            InitializeComponent();
            Name = u.Uname;
            head_Image = i;
        }
        private void Move_MouseMove(object sender, MouseEventArgs e)
        {
            if (Sign.IsFocused)
                return;
            if (e.LeftButton == MouseButtonState.Pressed)
            {
                this.DragMove();
            }
        }
        private void Close_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        private void Sign_LostFocus(object sender, RoutedEventArgs e)
        {
            TextBox tb = Sign;
            tb.BorderThickness = new Thickness(0);
            tb.Background = Brushes.Transparent;
            tb.IsReadOnly = true;
        }

    }
}
