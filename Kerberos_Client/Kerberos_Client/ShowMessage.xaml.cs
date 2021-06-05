using System;
using System.Collections.Generic;
using System.Data;
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

namespace Kerberos_Client
{
    /// <summary>
    /// ShowMessage.xaml 的交互逻辑
    /// </summary>
    public partial class ShowMessage : Window
    {
        private List<Order> orderList = new List<Order>();
        public ShowMessage()
        {
            InitializeComponent();
        }
        public void add(Order order)
        {
            Dispatcher.Invoke(new Action(delegate
            {
                orderList.Add(order);
                ShowOrder.ItemsSource = null;
                ShowOrder.ItemsSource = orderList;
            }));
        }
        private void choose(object sender, SelectedCellsChangedEventArgs e)
        {
            int index = this.ShowOrder.SelectedIndex;
            if (index < 0)
            {
                this.txtBlk.Text = "";
                return;
            }
            Order order = ShowOrder.SelectedItem as Order;
            string msgID = "MsgID: " + order.MsgId ;
            string src = "Src: " + order.Src;
            string dst = "Dst: " + order.Dst;
            string msgType = "MsgType: " + order.MsgType;
            string extend = "[Extend]: " + order.Extend;
            string sign = "Sign: " + order.Sign;
            string statusReport = "StatusReport: " + order.StatusReport;
            string sTS = "STS: " + order.STS;
            string rTS = "RTS: " + order.RTS;
            string contentType = "ConetentType: " + order.ContentType;
            this.txtBlk.Text = msgID + src + dst + msgType + extend + sign + statusReport +
                sTS + rTS + contentType;
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {

        }
    }
}
