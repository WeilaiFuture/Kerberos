package Json;

import com.sun.tools.javac.util.List;

import java.util.Dictionary;

public class MyStruct {
    //证书
     public class Certificate {
         String version;//版本号
         String serial;//序列号
         String deadline;//有效日期
         String name;//主体名
         String pk;//公钥
     }
     //对称钥
     public class My_k {
            String key; //对称钥
     }
     //认证数据报文
     public class Message1 {
         String idc; //用户号
         String idt; //tgs
         String ts;  //时间戳
     }
    public class Message2 {
        String key; //session key
        String idt; //tgs
        String ts;  //签发时间
        String lifetime; //有效期
        Ticket t;   //票据
    }
    public class Ticket {
        String key;//Session key
        String idc;//用户标识符
        String adc;//client 地址
        String idt;//tgs
        String ts;//时间戳
        String lifetime;//有效期
    }
    public class Message3 {
        String idv;//v的id
        Ticket t;//票据
        Authenticator ac;//认证
    }
    public class Message4 {
        String key;//session key
        String idv;//v的id
        String ts;//时间戳
        Ticket t;//票据
    }
    public class Authenticator {
        String idc;//Ticket主人id
        String adc;//Ticket主人地址
        String ts;//签发时间
    }
    public class Message5 {
        Ticket t;//票据
        Authenticator ac;//认证
    }
    public class Message6 {
        String ts;//时间戳
    }
    public class User
    {
        public String Photo ; //图片在本机的地址
        public String Uname ;//用户昵称
        public String Uid ;//用户账号
        public String Psswd ;//用户密码
        public String Name;//用户真实姓名
        public String Sign ;//个性签名
        public String Email;//邮箱
        public int Gender ;//性别
        public int Status ;//状态
        public int StartTime;//注册时间
    }
    public class Friend {
        User u;//用户信息
        String remark;//备注
        int startTime;//添加好友时间
        String tid;//分组信息
    }
    public class chat_Message {
        int head;//信息种类
        String content;//信息内容
        User u;//发送方
        int time;//时间戳
    }
    public class Group {
        List<Dictionary<User, String>> list;//用户列表
        String gid;//群账号
        String photo;//群头像
        String leader;//群主账号
        String sign;//群介绍
        int startTime;//创建时间
    }
    public class record_Message {
        List<chat_Message> messages_list;
    }
}
