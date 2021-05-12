using System;
using System.Collections.Generic;
using System.ComponentModel;
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
using static Client.MyStruct;
namespace Client
{
    /// <summary>
    /// User.xaml 的交互逻辑
    /// </summary>
    public partial class User_Window : Window
    {
        public User my_user;
        private List<User> f_List=new List<User>();
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
            for (int j = 0; j < 10; j++)
            {
                User uu = new User();
                uu.Uname ="test"+ j.ToString();
                f_List.Add(uu);
            }
            //Expander expander = new Expander();
            //expander.Content = "123";
            //expander.HorizontalContentAlignment = HorizontalAlignment.Stretch;
            //Grid.SetColumn(expander, 1);
            //grid3.Children.Add(expander);
            my_user = u;
            friend_List.ItemsSource = f_List;
            Name = u.Uname;
            head_Image = i;
        }

        private void Close_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        private void Tb_LostFocus(object sender, RoutedEventArgs e)
        {
            TextBox tb = sender as TextBox;
            tb.BorderThickness = new Thickness(0);
            if(tb.Name.Equals("sign_Tb"))
              tb.Background = Brushes.Transparent;   
            tb.IsReadOnly = true;
        }
        private void Tb_GotFocus(object sender, RoutedEventArgs e)
        {
            TextBox tb = sender as TextBox;
            tb.BorderThickness = new Thickness(1);
            tb.Background = Brushes.White;
            tb.IsReadOnly = false;
        }

        private void Window_MouseLeftButtonUp(object sender, MouseButtonEventArgs e)
        {
          my_Grid.Focus().ToString();
        }
        private void Window_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            DragMove();
        }

        private void TextBox_TextChanged(object sender, TextChangedEventArgs e)
        {

        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            B_1.BorderThickness = new Thickness(0, 0, 0, 4);
            B_2.BorderThickness = new Thickness(0, 0, 0, 0);
            my_Exp.Header = "我的信息";
            my_Exp.IsExpanded = false;
        }

        private void Button_Click_1(object sender, RoutedEventArgs e)
        {
            B_1.BorderThickness = new Thickness(0, 0, 0, 0);
            B_2.BorderThickness = new Thickness(0, 0, 0, 4);
            my_Exp.Header = "我的好友  " + f_List.Count().ToString();
            my_Exp.IsExpanded = false;
        }

        private void friend_List_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            Dispatcher.Invoke(new Action(delegate
            {
                ChatUI u = new ChatUI(my_user,head_Image);
                Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                newWindowThread.SetApartmentState(ApartmentState.STA);
                newWindowThread.IsBackground = true;
                newWindowThread.Start();
            }));
        }
        private void ThreadStartingPoint(Window w)
        {
            Dispatcher.Invoke(new Action(delegate
            {
                w.Show();
            }));
            System.Windows.Threading.Dispatcher.Run();
        }

        private void head_Image_MouseDown(object sender, MouseButtonEventArgs e)
        {
            if (e.ChangedButton == MouseButton.Left && e.ClickCount == 2)
            {
                Dispatcher.Invoke(new Action(delegate
                {
                    UserUI u = new UserUI(my_user, head_Image);
                    Thread newWindowThread = new Thread(() => ThreadStartingPoint(u));
                    newWindowThread.SetApartmentState(ApartmentState.STA);
                    newWindowThread.IsBackground = true;
                    newWindowThread.Start();
                }));
            }
        }
    }
}
