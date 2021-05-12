using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client
{
    public class MyStruct
    {
        public class Certificate
        {
            string version;
            string serial;
            string deadline;
            string name;
            long pk;
        }

        public class My_k
        {
            string key;
        }

        public class Message1
        {
            string IDc;
            string IDt;
            string TS;
        }

        public class Message2
        {
            string key;
            string IDt;
            string TS;
            string Lifetime;
            Ticket t;
        }

        public class Ticket
        {
            string key;
            string IDc;
            string ADc;
            string IDt;
            string TS;
            string Lifetime;
        }

        public class Message3
        {
            string IDv;
            Ticket t;
            Authenticator Ac;
        }

        public class Message4
        {
            string Key;
            string IDv;
            string TS;
            Ticket t;
        }

        public class Authenticator
        {
            string IDc;
            string ADc;
            string TS;
        }

        public class Message5
        {
            Ticket t;
            Authenticator Ac;
        }

        public class Message6
        {
            string TS;
        }

        public class User
        {
            public string Photo { get; set; }
            public string Uname { get; set;}
            public string Uid { get; set; }
            public string Psswd { get; set; }
            public string Name { get; set; }
            public string Sign { get; set; }
            public string Email { get; set; }
            public int Gender { get; set; }
            public int Status { get; set; }
            public int StartTime { get; set; }
            public User()
            {
                Uid = "123456789";
                Uname = "test";
                Name = "test";
                Psswd = "0";
                Sign = "0";
                Photo = "image/test.jpg";
                Email = Uid + "@123.com";
                Gender = 0;
                Status = 0;
                StartTime = 0;
            }

        }

        public class Friend
        {
            User u;
            string remark;
            int startTime;
            string tid;
        }

        public class chat_Message
        {
            int head;
            string content;
            User u;
            int time;
        }

        public class Group
        {
            List<Dictionary<User, string>> list;
            string gid;
            string photo;
            string leader;
            string sign;
            int startTime;
        }

        public class record_Message
        {
            List<chat_Message> messages_list;
        }
    }
}
