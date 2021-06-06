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
            Order o = new Order(order);
            Dispatcher.Invoke(new Action(delegate
            {
                orderList.Add(o);
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
            string msgType = "MsgType: " + order.MsgType + "\n";
            string extend = "[Extend]: " + order.Extend+"\n";
            string sign = "Sign: " + order.Sign + "\n";
            string statusReport = "StatusReport: " + order.StatusReport + "\n";
            string contentType = "ConetentType: " + order.ContentType + "\n";
            this.txtBlk.Text =  msgType + extend + sign + statusReport +contentType;
        }

        void DataGrid_LoadingRow(object sender, DataGridRowEventArgs e)
        {
            e.Row.Header = (e.Row.GetIndex()).ToString();
        }
    }
}
