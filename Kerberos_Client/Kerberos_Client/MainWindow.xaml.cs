using Kerberos_Client.UI;
using System;
using System.Collections.Generic;
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
        public MainWindow()
        {
            InitializeComponent();
        }
        private void Hyperlink_Click(object sender, RoutedEventArgs e)
        {
            // Hyperlink link = sender as Hyperlink;
            //Process.Start(new ProcessStartInfo(link.NavigateUri.AbsoluteUri));
            Process.Start(new ProcessStartInfo(@"E:\Kerberos\Web-Server\index.html"));
        }
        private void Close_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
        private void Login_Click(object sender, RoutedEventArgs e)
        {
            User u = new User();
            u.Uname = ID.Text;
            Window U = new Main_Window(u, head_Image);
            U.Show();
            this.Close();
        }

        private void ID_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            string path = @"../../Image_Source\" + ID.SelectedItem.ToString() + ".jpg";
            BitmapImage bi = new BitmapImage();
            bi.BeginInit();
            if (File.Exists(path))
            {
                bi.UriSource = new Uri(path, UriKind.RelativeOrAbsolute);
            }
            else
            {
                bi.UriSource = new Uri(@"../../Image_Source\未登录头象.png", UriKind.RelativeOrAbsolute);
            }
            bi.EndInit();
            bi.Freeze();
            head_Image.Source = bi;
        }

        private void Window_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            DragMove();
        }
    }
}
