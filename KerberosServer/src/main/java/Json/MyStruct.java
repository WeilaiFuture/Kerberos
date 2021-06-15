package Json;


import java.util.*;

public class MyStruct {

    public Certificate certificate; //证书
    public My_k my_k;
    public Message1 message1;
    public Message2 message2;
    public Ticket ticket;
    public Message3 message3;
    public Message4 message4;
    public Message5 message5;
    public Message6 message6;
    public User user;
    public Friend friend;
    public Chat_Message chat_message;
    public Group group;
    public LinkedList<Group> groups;
    public Record_Message record_message;
    public LinkedList<Friend> friendlist;
/*
    static public class Friendlist{
        LinkedList<Friend> Friends;

        public LinkedList<Friend> getFriends() {
            return Friends;
        }

        public void setFriends(LinkedList<Friend> friends) {
            for(int i=0;i<friends.size();i++){
                Friends.addLast(friends.get(i));
            }
        }
    }

 */
    static public class Certificate {
         String version;//版本号
         String serial;//序列号
         String deadline;//有效日期
         String name;//主体名
         String pk;//公钥

        public String getVersion() {
            return version;
        }

        public String getSerial() {
            return serial;
        }

        public String getDeadline() {
            return deadline;
        }

        public String getName() {
            return name;
        }

        public String getPk() {
            return pk;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPk(String pk) {
            this.pk = pk;
        }
    }
    //对称钥
    static public class My_k {
            String key; //对称钥

         public String getKey() {
             return key;
         }

         public  void setKey(String key) {
             this.key = key;
         }
     }
    //认证数据报文
    static public class Message1 {
         String idc; //用户号
         String idt; //tgs
         String ts;  //时间戳

         public String getIdc() {
             return idc;
         }

         public String getIdt() {
             return idt;
         }

         public String getTs() {
             return ts;
         }

         public void setIdc(String idc) {
             this.idc = idc;
         }

         public void setIdt(String idt) {
             this.idt = idt;
         }

         public void setTs(String ts) {
             this.ts = ts;
         }
     }
    static public class Message2 {
        String key; //session key
        String idt; //tgs
        String ts;  //签发时间
        String lifetime; //有效期
        String t;   //票据

        public String getKey() {
            return key;
        }

        public String getIdt() {
            return idt;
        }

        public String getTs() {
            return ts;
        }

        public String getLifetime() {
            return lifetime;
        }

        public String getT() {
            return t;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setIdt(String idt) {
            this.idt = idt;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public void setLifetime(String lifetime) {
            this.lifetime = lifetime;
        }

        public void setT(String t) {
            this.t = t;
        }
    }
    static public class Ticket {
        String key;//Session key
        String idc;//用户标识符
        String adc;//client 地址
        String idt;//tgs
        String ts;//时间戳
        String lifetime;//有效期

        public String getKey() {
            return key;
        }

        public String getIdc() {
            return idc;
        }

        public String getAdc() {
            return adc;
        }

        public String getIdt() {
            return idt;
        }

        public String getTs() {
            return ts;
        }

        public String getLifetime() {
            return lifetime;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setIdc(String idc) {
            this.idc = idc;
        }

        public void setAdc(String adc) {
            this.adc = adc;
        }

        public void setIdt(String idt) {
            this.idt = idt;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public void setLifetime(String lifetime) {
            this.lifetime = lifetime;
        }
    }
    static public class Message3 {
        String idv;//v的id
        String t;//票据
        String ac;//认证

        public String getIdv() {
            return idv;
        }

        public String getT() {
            return t;
        }

        public String getAc() {
            return ac;
        }

        public void setIdv(String idv) {
            this.idv = idv;
        }

        public void setT(String t) {
            this.t = t;
        }

        public void setAc(String ac) {
            this.ac = ac;
        }
    }
    static public class Message4 {
        String key;//session key
        String idv;//v的id
        String ts;//时间戳
        String t;//票据

        public String getKey() {
            return key;
        }

        public String getIdv() {
            return idv;
        }

        public String getTs() {
            return ts;
        }

        public String getT() {
            return t;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setIdv(String idv) {
            this.idv = idv;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public void setT(String t) {
            this.t = t;
        }
    }
    static public class Authenticator {
        String idc;//Ticket主人id
        String adc;//Ticket主人地址
        String ts;//签发时间

        public String getIdc() {
            return idc;
        }

        public String getAdc() {
            return adc;
        }

        public String getTs() {
            return ts;
        }

        public void setIdc(String idc) {
            this.idc = idc;
        }

        public void setAdc(String adc) {
            this.adc = adc;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }
    }
    static public class Message5 {
        String t;//票据
        String ac;//认证

        public String getT() {
            return t;
        }

        public String getAc() {
            return ac;
        }

        public void setT(String t) {
            this.t = t;
        }

        public void setAc(String ac) {
            this.ac = ac;
        }
    }
    static public class Message6 {
        String ts;//时间戳

        public String getTs() {
            return ts;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }
    }
    static public class User
    {
        String Photo ; //图片在本机的地址
        String Uname ;//用户昵称
        String Uid ;//用户账号
        String Psswd ;//用户密码
        String Name;//用户真实姓名
        String Sign ;//个性签名
        String Email;//邮箱
        int Gender ;//性别
        int Status ;//状态
        long StartTime;//注册时间

        public String getPhoto() {
            return Photo;
        }

        public String getUname() {
            return Uname;
        }

        public String getUid() {
            return Uid;
        }

        public String getPsswd() {
            return Psswd;
        }

        public String getName() {
            return Name;
        }

        public String getSign() {
            return Sign;
        }

        public String getEmail() {
            return Email;
        }

        public int getGender() {
            return Gender;
        }

        public int getStatus() {
            return Status;
        }

        public long getStartTime() {
            return StartTime;
        }

        public void setPhoto(String photo) {
            Photo = photo;
        }

        public void setUname(String uname) {
            Uname = uname;
        }

        public void setUid(String uid) {
            Uid = uid;
        }

        public void setPsswd(String psswd) {
            Psswd = psswd;
        }

        public void setName(String name) {
            Name = name;
        }

        public void setSign(String sign) {
            Sign = sign;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public void setGender(int gender) {
            Gender = gender;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public void setStartTime(long startTime) {
            StartTime = startTime;
        }
    }
    static public class Friend {
        User u;//用户信息
        String remark;//备注
        int startTime;//添加好友时间
        String tid;//分组信息

        public User getU() {
            return u;
        }

        public void setU(User u) {
            this.u= new MyStruct.User();
            this.u.setUname(u.getUname());
            this.u.setUid(u.getUid());
            this.u.setStatus(u.getStatus());
            this.u.setStartTime(u.getStartTime());
            this.u.setSign(u.getSign());
            this.u.setPhoto(u.getPhoto());
            this.u.setName(u.getName());
            this.u.setGender(u.getGender());
            this.u.setEmail(u.getEmail());
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }
    }
    static public class Chat_Message {
        String content;//信息内容
        User u;//发送方
        long time;//时间戳

        

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public User getU() {
            return u;
        }

        public void setU(User u) {
            this.u = u;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
    static public class Group {
        List<User> list=new LinkedList<User>();//用户列表
        String gid;//群账号
        String gname;//群名称
        String photo;//群头像
        String leader;//群主账号
        String sign;//群介绍
        long startTime;//创建时间

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public List<User> getList() {
            return list;
        }

        public void setList(List<User> list) {
            this.list = list;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getLeader() {
            return leader;
        }

        public void setLeader(String leader) {
            this.leader = leader;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
    }
    static public class Record_Message {
        List<Chat_Message> messages_list;

        public List<Chat_Message> getMessages_list() {
            return messages_list;
        }

        public void setMessages_list(List<Chat_Message> messages_list) {
            this.messages_list = messages_list;
        }
    }
}
