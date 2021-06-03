using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

namespace Kerberos_Client.UI
{
    public class ChatMessage : INotifyPropertyChanged
    {
        private string photo;
        public string Photo
        {
            get { return photo; }
            set
            {
                if (photo != value)
                {
                    photo = value;
                    Notify();
                }
            }
        }
        private string message;
        public string Message
        {
            get { return message; }
            set
            {
                if (message != value)
                {
                    message = value;
                    Notify();
                }
            }
        }
        private TypeLocalMessageLocation messageLocation;
        public TypeLocalMessageLocation MessageLocation
        {
            get { return messageLocation; }
            set
            {
                if (messageLocation != value)
                {
                    messageLocation = value;
                    Notify();
                }
            }
        }


        public event PropertyChangedEventHandler PropertyChanged;

        protected void Notify([CallerMemberName]string obj = "")
        {
            if (PropertyChanged != null)
            {
                this.PropertyChanged(this, new PropertyChangedEventArgs(obj));
            }
        }
    }
}
