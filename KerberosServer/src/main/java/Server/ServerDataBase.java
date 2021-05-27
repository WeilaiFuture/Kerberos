package Server;

import Json.MyJson;
import Json.MyStruct;
import StateMachine.Application;
import org.springframework.boot.SpringApplication;

import java.sql.*;
import java.util.LinkedList;

import static Json.MyJson.OrderToString;

public class ServerDataBase {
    /*
    包含所有数据库操作的封装函数；
     */
    static private Connection con=null; // 声明Connection对象
    public static void main(String []args) {

        MyStruct struct=new MyStruct();
        MyJson.Order order=new MyJson.Order();
        struct.certificate=new MyStruct.Certificate();
        struct.certificate.setDeadline("1");
        struct.certificate.setName("3");
        struct.certificate.setPk("3");
        struct.certificate.setSerial("4");
        struct.certificate.setVersion("5");
        if(connectData()!=null){
      /*      wCertif(struct);
            rSearchID("1");
            rSearchID("2");
            wLogin("1",0);
            wLogin("2",0);

            rSearchID("2");
            rFriendList("1");
            rFriendList("2");

            rGroupUser("1");

        MyStruct.User user=rSearchID("1");
        user.setEmail("234");
        user.setGender(1);
        user.setPhoto("111");
        wInfo(user);
        rSearchID("1");
   */

        }
    }
    static public Connection connectData() {
        /*
        连接数据库；
         */
        Connection conn = null;
        // no ssl certificate:
        String url = "jdbc:mysql://" +
                "rm-uf6t4cbyfz681x569jo.mysql.rds.aliyuncs.com:3306/kerbors" +
                "?sslmode=require" +
                "&connectTimeout=3000" +
                "&socketTimeout=60000";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, "w123", "Wl123456");
            System.out.println("Database connected");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("连接数据库错误");
            return null;
        }
        con=conn;
        return conn;
    }

    static public boolean wCertif(MyStruct.Certificate certificate) {
        /*
        向数据库存入证书；
         */
        try {
            String sql="insert into `Certificate` (`version` ,`serial` ,`deadline` ,`name` ,`pk`) values(\""
                    +certificate.getVersion()+"\",\""
                    +certificate.getSerial()+"\",\""
                    +certificate.getDeadline()+"\",\""
                    +certificate.getName()+"\",\""
                    +certificate.getPk()+ "\")";
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("存储证书错误");
            return false;
        }
    }

    static public boolean wRegister(MyStruct struct) {
        /*
        向数据库存入注册信息；
         */
        try {
            Statement sql=con.createStatement();
            System.out.println("存储注册信息成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("存储注册信息错误");
            return false;
        }
    }
    static public boolean wLogin(String ID,int status){
        /*
        向数据库写入登陆状态；
         */
        try {
            String sql="update `users` set `status`="+status+" where `uid`=\""+ID+"\"";
            Statement statement=con.createStatement();
            statement.executeUpdate(sql);
            System.out.println(ID+" 写入登陆状态成功 "+status);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ID+" 写入登陆状态错误 "+status);
            return false;
        }
    }
    static public LinkedList<MyStruct.Friend> rFriendList(String ID){
        /*
        查询好友列表；
         */
        LinkedList<MyStruct.Friend> friends=new LinkedList<MyStruct.Friend>();
        try {
            String sql="SELECT * FROM `friend` WHERE `me`=\"" + ID + "\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            System.out.println("uid remark tid startTime");
            while (result.next()) {
                MyStruct.Friend friend=new MyStruct.Friend();
                friend.setRemark(result.getString("remark"));
                friend.setStartTime(result.getInt("startTime"));
                MyStruct.User user=rSearchID(result.getString("ta"));
                friend.setU(user);
           //     System.out.println("查询账号 "+result.getString("ta")+" uid "+user.getUid()+user.getStatus()+user.getGender()+user.getStartTime()+user.getEmail());

                String sql1="SELECT `tname` FROM `tags` WHERE `tid`="+result.getInt("tid")+  " and `uid`=\""+ID+"\"";
                Statement statement1=con.createStatement();
                ResultSet result1 = statement1.executeQuery(sql1);
                result1.next();
                friend.setTid(result1.getString("tname"));

                System.out.println(friend.getU().getUid()+" "+friend.getRemark()+" "+friend.getTid()+" "+friend.getStartTime());
                friends.addLast(friend);
            }
            System.out.println(ID+" 查询好友列表成功");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ID+" 查询好友列表错误");
        }
        return friends;
    }
    static public LinkedList<String> rOnLineFriend(String ID){
        /*
        查询在线状态好友ID；
         */
        LinkedList<String> friends=new LinkedList<String>();
        try {
            String sql="SELECT * FROM `friend` WHERE `me`=\"" + ID + "\" and `status`=1";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            System.out.println("uid remark tid startTime");
            while (result.next()) {
                friends.addLast(result.getString("ta"));
            }
            System.out.println(ID+" 查询好友列表成功");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ID+" 查询好友列表错误");
        }
        return friends;
    }
     static public LinkedList<String> rGroupUser(String ID){
        /*
        查询群内成员ID；
         */
         LinkedList<String> groups=new LinkedList<String>();
         try {
             String sql="SELECT `uid` FROM `belong` WHERE `gid`=\"" + ID+"\"";
             Statement statement=con.createStatement();
             ResultSet result = statement.executeQuery(sql);
             System.out.println("uid");
             while (result.next()) {
                 groups.addLast(result.getString("uid"));
             }
             System.out.println(ID+" 查询群成员成功");
         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println(ID+" 查询群成员错误");
         }
         return groups;
     }
    static public MyStruct.User rSearchID(String ID){
        /*
        查询用户；
         */
        MyStruct.User user=new MyStruct.User();
        try {
            String sql="SELECT * FROM `users` WHERE `uid`=\"" + ID + "\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            System.out.println("uid email name photo pswd sign uname startTime gender status");
            while (result.next()) {
                user.setEmail(result.getString("email"));
                user.setGender(result.getInt("gender"));
                user.setName(result.getString("name"));
                user.setPhoto(result.getString("photo"));
                user.setPsswd(result.getString("psswd"));
                user.setSign(result.getString("sign"));
                user.setStartTime(result.getInt("startTime"));
                user.setStatus(result.getInt("status"));
                user.setUid(ID);
                user.setUname(result.getString("uname"));
                System.out.println(user.getUid()+" "+user.getEmail()+" "+user.getName()+" "+user.getPhoto()+" "+user.getPsswd()+" "+user.getSign()
                        +" "+user.getUname()+" "+user.getStartTime()+" "+user.getGender()+" "+user.getStatus());
            }
            System.out.println(ID+" 查询ID成功");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ID+" 查询ID错误");
        }
        return user;
    }
    static public boolean rInfo(String ID){
        /*
        查询个人信息；
        与查询用户功能重复，暂时保留
         */
         return false;
     }
     static public boolean wInfo(MyStruct.User user){
        /*
        修改个人信息；
         */ try {
             String sql="update  `users` set  `email`=\""+user.getEmail()
                     +"\", `gender`="+user.getGender()
             +",`startTime`= "+user.getStartTime()
             +",`status`=  "+user.getStatus()
             +",`name`=\""+user.getName()
                     + "\" ,`photo`= \""+user.getPhoto()
                     +"\" ,`psswd`= \""+user.getPsswd()
             +"\" ,`sign`= \""+user.getSign()
             +"\" ,`uname`= \""+user.getUname()
             +"\" ,`uid`=\""+user.getUid()
                     +"\" where `uid`=\"" + user.getUid() + "\"";
             Statement statement=con.createStatement();
             statement.executeUpdate(sql);
             System.out.println(user.getUid()+" 修改个人信息成功");
             return true;
         } catch (SQLException e) {
             e.printStackTrace();
             System.out.println(user.getUid()+" 修改个人信息错误");
             return false;
         }
     }
    static public boolean wAddF(String ID,MyStruct.Friend friend){
        /*
        修改好友列表，添加好友；
         */
        try {
            MyStruct.User user=rSearchID(friend.getU().getUid());
            String sql="insert into `friend` (`me` ,`ta` ,`startTime` ,`remark` ,`tid`,`status`) values(\""
                    +ID+"\",\""
                    +friend.getU().getUid()+"\","
                    +friend.getStartTime()+","
                    +friend.getRemark()+"\","
                    +friend.getTid()+ ","
                    +user.getStatus()+")";
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(sql);
            System.out.println("添加好友成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("添加好友错误");
            return false;
        }
    }
    static public boolean wAddG(String IDA,String IDG){
        /*
        修改群列表，添加群；
         */
        return false;
    }
    static public boolean wDeleteF(String IDA,String IDB){
        /*
        修改好友列表，删除好友；
         */
        try {
            String sql="delete `friend` where `me`=\""+IDA+"\" and `ta`=\""+IDB+"\"";
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(sql);
            System.out.println("添加好友成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("添加好友错误");
            return false;
        }
    }
    static public boolean wDeleteG(String IDA,String IDG){
        /*
        修改群列表，退出群聊；
         */
        return false;
    }
    static public String wCreateG(String message){
        /*
        组建群聊；
         */
        String IDG="";//群账号
        return IDG;
    }
}