using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
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

        public ObservableCollection<ChatMessage> chatMessage = new ObservableCollection<ChatMessage>()
        {
            new ChatMessage()
            {
                Photo=@"E:\Kerberos\Kerberos_Client\Kerberos_Client\Image_Source\test.jpg",
                Message="你好",
                MessageLocation=TypeLocalMessageLocation.chatRecv
            },
            new ChatMessage()
            {
                Photo=@"E:\Kerberos\Kerberos_Client\Kerberos_Client\Image_Source\test.jpg",
                Message="好久不见,老铁",
                MessageLocation=TypeLocalMessageLocation.chatSend
            },
        };
        public User My_user;
        public User Chat_user;
        public Chat_Window(User u,User user)
        {
            InitializeComponent();
            Chat_user = u;
            My_user = user;
            head_Image.Source = img.GetBitmap(u.Photo);
            Uname_TextBlock.Text = "昵称:" + Chat_user.Uname;
            Uid_TX.Text = "账号:" + Chat_user.Uid;
            Email_TX.Text = "邮箱:" + Chat_user.Email;
            ListBoxChat.ItemsSource = chatMessage;
        }
        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Chat_Message chat = new Chat_Message();
            chat.U = My_user;
            chat.Head = 101;
            chat.Content = send_text.Text;
            MyStruct myStruct = new MyStruct();
            myStruct.chat_message = chat;
            Order order = new Order();
            order.Dst = Chat_user.Uid;
            order.Src = My_user.Uid;
            order.MsgType = "2001";
            order.ContentType = "101";
            order.Extend = JsonHelper.ToJson(myStruct);
            order.Extend = DESLibrary.EncryptDES(JsonHelper.ToJson(myStruct), Main_Window.Keys["server"]);
            ConnectServer.sendMessage(order);

            chatMessage.Add(new ChatMessage()
            {
                Photo = My_user.Photo,
                Message = send_text.Text,
                MessageLocation = TypeLocalMessageLocation.chatSend
            }); ;
            send_text.Text = "";
            ListBoxChat.ScrollIntoView(ListBoxChat.Items[ListBoxChat.Items.Count - 1]);
        }
        protected override void OnClosing(CancelEventArgs e)
        {
            Main_Window.Chat_Dic.Remove(Chat_user.Uid);
            base.OnClosing(e);
        }
    }
}
