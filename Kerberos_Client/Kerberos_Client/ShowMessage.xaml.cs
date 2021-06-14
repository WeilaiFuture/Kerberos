using Kerberos_Client.UI;
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
            string extend = "[Extend]: " + order.Extend + "\n";
            string sign = "Sign: " + order.Sign + "\n";
            this.txtBlk.Text =extend + sign;
            switch (order.Src)
            {
                case "Server":
                    this.txtBlk.Text += "Encryption mode: " + "DES\n";
                    this.txtBlk.Text += "Session Key:" + Main_Window.Keys["server"] + "\n";
                    this.txtBlk.Text += "Decryption Str: " + DESLibrary.DecryptDES(order.Extend, Main_Window.Keys["server"]) + "\n";
                    break;
                case "AS":
                    if (order.MsgType.Equals("0002"))
                    {
                        this.txtBlk.Text += "Encryption mode: " + "RSA\n";
                        this.txtBlk.Text += "Public Key:" + Main_Window.Keys["public"] + "\n";
                        this.txtBlk.Text += "Private Key:" + Main_Window.Keys["private"] + "\n";
                        MyStruct myStruct = JsonHelper.FromJson<MyStruct>(order.Extend);
                        this.txtBlk.Text += "Decryption key: " + RSALibrary.RSADecrypt(Main_Window.Keys["private"], myStruct.my_k.Key) + "\n";
                    }
                    else
                    {
                        this.txtBlk.Text += "Encryption mode: " + "DES\n";
                        this.txtBlk.Text += "Session Key:" + Main_Window.Keys["as"] + "\n";
                        this.txtBlk.Text += "Decryption Str: " + DESLibrary.DecryptDES(order.Extend, Main_Window.Keys["as"]) + "\n";
                    }
                    break;
                case "TGS":
                    this.txtBlk.Text += "Encryption mode: " + "DES\n";
                    this.txtBlk.Text += "Session Key:" + Main_Window.Keys["tgs"] + "\n";
                    this.txtBlk.Text += "Decryption Str: " + DESLibrary.DecryptDES(order.Extend, Main_Window.Keys["tgs"]) + "\n";
                    break;
                default:
                    {
                        switch (order.Dst)
                        {
                            case "Server":
                                this.txtBlk.Text += "Encryption mode: " + "DES\n";
                                this.txtBlk.Text += "Session Key:" + Main_Window.Keys["server"] + "\n";
                                this.txtBlk.Text += "Decryption Str: " + DESLibrary.DecryptDES(order.Extend, Main_Window.Keys["server"]) + "\n";
                                break;
                            case "AS":         
                                this.txtBlk.Text += "Encryption mode: " + "NULL\n";
                                break;
                            case "TGS":
                                this.txtBlk.Text += "Encryption mode: " + "DES\n";
                                this.txtBlk.Text += "Session Key:" + Main_Window.Keys["tgs"] + "\n";
                                MyStruct myStruct = JsonHelper.FromJson<MyStruct>(order.Extend);
                                this.txtBlk.Text += "Decryption Ac: " + DESLibrary.DecryptDES(myStruct.message3.AC, Main_Window.Keys["tgs"]) + "\n";
                                break;
                            default:
                                this.txtBlk.Text += "Encryption mode: " + "DES\n";
                                this.txtBlk.Text += "Session Key:" + Main_Window.Keys["server"] + "\n";
                                this.txtBlk.Text += "Decryption Str: " + DESLibrary.DecryptDES(order.Extend, Main_Window.Keys["server"]) + "\n";
                                break;
                        }
                    }
                    break;
            }
        }

        void DataGrid_LoadingRow(object sender, DataGridRowEventArgs e)
        {
            e.Row.Header = (e.Row.GetIndex()).ToString();
        }
    }
}
