using System;
using System.Collections.Generic;
using System.Diagnostics;
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

namespace Client
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
            Hyperlink link = sender as Hyperlink;
            Process.Start(new ProcessStartInfo("https://www.baidu.com/s?wd=wpf%20%E8%83%8C%E6%99%AF%E5%8A%A8%E5%9B%BE&pn=10&oq=wpf%20%E8%83%8C%E6%99%AF%E5%8A%A8%E5%9B%BE&tn=18029102_2_dg&ie=utf-8&usm=1&fenlei=256&rsv_idx=1&rsv_pq=a4f0792d0092be12&rsv_t=b08cE%2F2uEk1KDuLu9VX9x%2B7lKsdG5JmSd0RZvGA8qiNL5ibU6tEvE1fKuykVOAQ2nYQr%2Bw&rsv_page=1"));
        }
    }
}
