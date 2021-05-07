using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client
{
    class MyStruct
    {
        class Certificate
        {
            string version;
            string serial;
            string deadline;
            string name;
            long pk;
        }

        class My_k
        {
            string key;
        }

        class Message1
        {
            string IDc;
            string IDt;
            string TS;
        }

        class Message2
        {
            string key;
            string IDt;
            string TS;
            string Lifetime;
            Ticket t;
        }

        class Ticket
        {
            string key;
            string IDc;
            string ADc;
            string IDt;
            string TS;
            string Lifetime;
        }

        class Message3
        {
            string IDv;
            Ticket t;
            Authenticator Ac;
        }

        class Message4
        {
            string Key;
            string IDv;
            string TS;
            Ticket t;
        }

        class Authenticator
        {
            string IDc;
            string ADc;
            string TS;
        }

        class Message5
        {
            Ticket t;
            Authenticator Ac;
        }

        class Message6
        {
            string TS;
        }

        class User
        {
            string uid;
            string uname;
            string name;
            string psswd;
            string sign;
            string photo;
            string email;
            int gender;
            int status;
            int startTime;
        }

        class Friend
        {
            User u;
            string remark;
            int startTime;
            string tid;
        }

        class chat_Message
        {
            int head;
            string content;
            User u;
            int time;
        }

        class Group
        {
            List<Dictionary<User, string>> list;
            string gid;
            string photo;
            string leader;
            string sign;
            int startTime;
        }

        class record_Message
        {
            List<chat_Message> messages_list;
        }
    }
}
