using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media.Imaging;
using static Kerberos_Client.MyStruct;
using WinForm = System.Windows.Forms;

namespace Kerberos_Client.UI
{
    /// <summary>
    /// User_Window.xaml 的交互逻辑
    /// </summary>
    public partial class User_Window : Window
    {
        public User My_user;
        public User_Window(User u, Image i)
        {
            InitializeComponent();
            My_user = u;
            head_Image.Source = i.Source;
            Uname_TextBox.Text = My_user.Uname;
            Uid_TextBlock.Text += My_user.Uid;
            Email_TextBlock.Text += My_user.Email;
        }
        /// <summary>
        /// 更改头像
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void Button_Click(object sender, RoutedEventArgs e)
        {
            //对话框打开文件，获取文件名
            WinForm.OpenFileDialog dlg = new WinForm.OpenFileDialog();
            //dlg.InitialDirectory =;    //打开对话框后的初始目录
            dlg.Filter = "jpg文件|*.jpg|png文件|*.png";
            dlg.ShowDialog();
            string path = dlg.FileName;
            if (path == "")
            {
                return;
            }
            else
            {
                BitmapImage bi = new BitmapImage();
                using (MemoryStream ms = new MemoryStream(File.ReadAllBytes(path)))
                {
                    bi = new BitmapImage();
                    bi.BeginInit();
                    bi.CacheOption = BitmapCacheOption.OnLoad;//设置缓存模式
                    bi.StreamSource = ms;//通过StreamSource加载图片
                    bi.EndInit();
                    bi.Freeze();

                }
                head_Image.Source = bi;
            }
        }
        /// <summary>
        /// 保存修改信息
        /// </summary>
        /// <param name="sender">事件</param>
        /// <param name="e">时间路由</param>
        /// <returns></returns>
        private void Button_Click_1(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}