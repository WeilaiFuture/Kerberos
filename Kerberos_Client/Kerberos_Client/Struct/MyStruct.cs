using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Kerberos_Client
{
    public class MyStruct
    {
        #region 证书
        public class Certificate
        {
            string version;//版本号
            string serial;//序列号
            string deadline;//有效日期
            string name;//主体名
            string pk;//公钥
            public string Version
            {
                get { return version; }
                set { version = value; }
            }
            public string Serial
            {
                get { return serial; }
                set { serial = value; }
            }
            public string Deadline
            {
                get { return deadline; }
                set { deadline = value; }
            }
            public string Name
            {
                get { return name; }
                set { name = value; }
            }
            public string Pk
            {
                get { return pk; }
                set { pk = value; }
            }
        }
        #endregion
        #region 对称钥
        public class My_k
        {
            string key; //对称钥
            public string Key 
            {
                get { return key; }
                set { key = value; }
            }
        }
        #endregion
        #region 认证数据结构
        #region 报文1
        public class Message1
        {
            string idc; //用户号
            string idt; //tgs
            string ts;  //时间戳
            public string IDC
            {
                get { return idc; }
                set { idc = value; }
            }
            public string IDT
            {
                get { return idt; }
                set { idt = value; }
            }
            public string TS
            {
                get { return ts; }
                set { ts = value; }
            }
        }
        #endregion
        #region 报文2
        public class Message2
        {
            string key; //session key
            string idt; //tgs
            string ts;  //签发时间
            string lifetime; //有效期
            Ticket t;   //票据
            public string Key
            {
                get { return key; }
                set { key = value; }
            }

            public string IDT
            {
                get { return idt; }
                set { idt = value; }
            }

            public string TS
            {
                get { return ts; }
                set { ts = value; }
            }

            public string Lifetime
            {
                get { return lifetime; }
                set { lifetime = value; }
            }

            public Ticket T
            {
                get { return t; }
                set { t = value; }
            }
        }
        #endregion
        #region 票据
        public class Ticket
        {
            string key;//Session key
            string idc;//用户标识符
            string adc;//client 地址
            string idt;//tgs
            string ts;//时间戳
            string lifetime;//有效期
            public string Key
            {
                get { return key; }
                set { key = value; }
            }
            public string IDC
            {
                get { return idc; }
                set { idc = value; }
            }
            public string ADC
            {
                get { return adc; }
                set { adc = value; }
            }
            public string IDT
            {
                get { return idt; }
                set { idt = value; }
            }
            public string TS
            {
                get { return ts; }
                set { ts = value; }
            }
            public string Lifetime
            {
                get { return lifetime; }
                set { lifetime = value; }
            }
        }
        #endregion
        #region 报文3
        public class Message3
        {
            string idv;//v的id
            Ticket t;//票据
            Authenticator ac;//认证
            public string IDV
            {
                get { return idv; }
                set { idv = value; }
            }
            public Ticket T
            {
                get { return t; }
                set { t = value; }
            }
            public Authenticator AC
            {
                get { return ac; }
                set { ac = value; }
            }
        }
        #endregion
        #region 报文4
        public class Message4
        {
            string key;//session key
            string idv;//v的id
            string ts;//时间戳
            Ticket t;//票据
            public string Key
            {
                get { return key; }
                set { key = value; }
            }
            public string IDv
            {
                get { return idv; }
                set { idv = value; }
            }
            public string TS
            {
                get { return ts; }
                set { ts = value; }
            }
            public Ticket T
            {
                get { return t; }
                set { t = value; }
            }
        }
        #endregion
        #region 认证
        public class Authenticator
        {
            string idc;//Ticket主人id
            string adc;//Ticket主人地址
            string ts;//签发时间
            public string IDC
            {
                get { return idc; }
                set { idc = value; }
            }
            public string ADC
            {
                get { return adc; }
                set { adc = value; }
            }
            public string TS
            {
                get { return ts; }
                set { ts = value; }
            }
        }
        #endregion
        #region 报文5
        public class Message5
        {
            Ticket t;//票据
            Authenticator ac;//认证
            public Ticket T
            {
                get { return t; }
                set { t = value; }
            }
            public Authenticator AC
            {
                get { return ac; }
                set { ac = value; }
            }
        }
        #endregion
        #region 报文6
        public class Message6
        {
            string ts;//时间戳
            public string TS
            {
                get { return ts; }
                set { ts = value; }
            }
        }
        #endregion
        #endregion
        #region 用户
        public class User
        {
            public string Photo { get; set; } //图片在本机的地址
            public string Uname { get; set;}//用户昵称
            public string Uid { get; set; }//用户账号
            public string Psswd { get; set; }//用户密码
            public string Name { get; set; }//用户真实姓名
            public string Sign { get; set; }//个性签名
            public string Email { get; set; }//邮箱
            public int Gender { get; set; }//性别
            public int Status { get; set; }//状态
            public int StartTime { get; set; }//注册时间
            public User()
            {
                Uid = "123456789";
                Uname = "test";
                Name = "test";
                Psswd = "0";
                Sign = "0";
                Photo = "../Image_Source/test.jpg";
                Email = Uid + "@123.com";
                Gender = 0;
                Status = 0;
                StartTime = 0;
            }

        }
        #endregion
        #region 好友
        public class Friend
        {
            User u;//用户信息
            string remark;//备注
            int startTime;//添加好友时间
            string tid;//分组信息
            public User U
            {
                get { return u; }
                set { u = value; }
            }
            public string Remark
            {
                get { return remark; }
                set { remark = value; }
            }
            public int StartTime
            {
                get { return startTime; }
                set { startTime = value; }
            }
        }
        #endregion
        #region 聊天信息
        public class chat_Message
        {
            int head;//信息种类
            string content;//信息内容
            User u;//发送方
            int time;//时间戳
            public int Head
            {
                get { return head; }
                set { head = value; }
            }
            public string Content
            {
                get { return content; }
                set { content = value; }
            }
            public User U
            {
                get { return u; }
                set { u = value; }
            }
            public int Time
            {
                get { return Time; }
                set { time = value; }
            }
        }
        #endregion
        #region 群信息
        public class Group
        {
            List<Dictionary<User, string>> list;//用户列表
            string gid;//群账号
            string photo;//群头像
            string leader;//群主账号
            string sign;//群介绍
            int startTime;//创建时间
            public List<Dictionary<User, string>> LIST
            {
                get { return list; }
                set { list = new List<Dictionary<User, string>>(value); }
            }
            public string Gid
            {
                get { return gid; }
                set { gid = value; }
            }
            public string Photo
            {
                get { return photo; }
                set { photo = value; }
            }
            public string Leader
            {
                get { return leader; }
                set { leader = value; }
            }
            public string Sign
            {
                get { return sign; }
                set { sign = value; }
            }
            public int StartTime
            {
                get { return startTime; }
                set { startTime = value; }
            }
        }
        #endregion
        #region 消息记录
        public class record_Message
        {
            List<chat_Message> messages_list;
            public List<chat_Message> Messages_list
            { 
                get { return messages_list; }
                set { messages_list = new List<chat_Message>(value); }
            }
        }
        #endregion
    }
}
