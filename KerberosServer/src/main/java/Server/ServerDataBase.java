package Server;

import Json.IdWorker;
import Json.MyJson;
import Json.MyStruct;
import StateMachine.Application;
import org.springframework.boot.SpringApplication;

import java.sql.*;
import java.util.LinkedList;

import static Json.MyJson.OrderToString;

public class ServerDataBase {
    static IdWorker worker = new IdWorker(1,1,1);
    /*
    包含所有数据库操作的封装函数；
     */
    static public Connection con=null; // 声明Connection对象
    static public Connection connectData() {
        /*
        连接数据库；
         */
        Connection conn = null;
        // no ssl certificate:
        String url = "jdbc:mysql://" +
                "rm-uf6t4cbyfz681x569jo.mysql.rds.aliyuncs.com:3306/kerbors" +
                "?sslmode=require&autoReconnect=true" +
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
    static public boolean rCertif(String ID) {
        /*
        向数据库查询证书是否存在；
         */
        try {
            String sql="SELECT * FROM `Certificate` WHERE `name`=\"" + ID + "\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            if(result.next()){
                System.out.println("查询证书成功"+result.getString("name"));
                return true;
            }
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("查询证书失败");
            return false;
        }
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
                    +certificate.getPk()+ "\")"+"\n"+"on duplicate key update `pk` =\""+ certificate.getPk()+"\"";
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(sql);
            System.out.println(certificate.getName()+"存储证书成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("存储证书错误");
            return false;
        }
    }
    static public boolean wKcv(String ID,String K) {
        /*
        向数据库存入Kcv；
         */
        try {
            String sql="update `Certificate` set `Kcv`=\""+K+"\" where `name`=\""+ID+"\"";
            Statement statement=con.createStatement();
            statement.executeUpdate(sql);
            System.out.println(ID+" 存储Kcv成功 "+K);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ID+存储Kcv错误"+K);
            return false;
        }
    }
    static public String rKcv(String ID) {
        /*
        向数据库读取Kcv；
         */
        try {
            String sql="SELECT `Kcv` FROM `Certificate` WHERE `name`=\"" + ID + "\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            if(result.next()){
                System.out.println(ID+"读取Kcv成功"+result.getString("Kcv"));
                return result.getString("Kcv");
            }
            else return "";
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("读取Kcv错误");
            return null;
        }
    }
    static public String rPK(String ID) {
        /*
        向数据库查找PK；
         */
        try {
            String sql="SELECT `pk` FROM `Certificate` WHERE `name`=\"" + ID + "\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            if(result.next()){
                System.out.println(ID+"查询PK成功"+result.getString("pk"));
                return result.getString("pk");
            }
            else return "";
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("查询PK失败");
            return null;
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
    static public boolean rLogin(String ID,String pswd){
        /*
        向数据库查询账号密码；
         */
        try {
            String sql="SELECT `psswd` FROM `users` WHERE `uid`=\"" + ID + "\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            if(result.next()){
                System.out.println(ID+"查询账号密码成功"+pswd);
                if(result.getString("psswd").equals(pswd))
                    return true;
                else return false;
            }
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("查询账号密码失败");
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
                MyStruct.User user=rInfo(result.getString("ta"));
                user.setPsswd(null);
                friend.setU(user);
                friend.setTid(result.getString("tid"));

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
            String sql="SELECT * FROM `friend` WHERE `me`=\"" + ID + "\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            System.out.println("uid remark tid startTime");
            while (result.next()) {
                String IDB=result.getString("ta");
                String sql1="SELECT * FROM `users` WHERE `uid`=\"" + IDB + "\"";
                Statement statement1=con.createStatement();
                ResultSet result1 = statement1.executeQuery(sql1);
                if(result1.next()){
                    if(result1.getString("status").equals("1"))
                        friends.addLast(IDB);
                }
            }
            System.out.println(ID+" 查询好友列表成功");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ID+" 查询好友列表错误");
        }
        return friends;
    }
    static public LinkedList<MyStruct.Friend> rSearchID(String ID){
        /*
        查询用户；
         */
        MyStruct.User user=new MyStruct.User();
        MyStruct myStruct=new MyStruct();
        myStruct.friendlist=new LinkedList<MyStruct.Friend>();
        try {
            String sql="SELECT * FROM `users` WHERE `uid`like \"%" + ID + "%\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            System.out.println("uid email name photo pswd sign uname startTime gender status");
            while (result.next()) {
                user.setEmail(result.getString("email"));
                user.setGender(result.getInt("gender"));
                user.setName(result.getString("name"));
                user.setPhoto(result.getString("photo"));
               // user.setPsswd(result.getString("psswd"));
                user.setSign(result.getString("sign"));
                user.setStartTime(result.getLong("startTime"));
                user.setStatus(result.getInt("status"));
                user.setUid(result.getString("uid"));
                user.setUname(result.getString("uname"));
                myStruct.friend=new MyStruct.Friend();
                myStruct.friend.setU(user);
                myStruct.friendlist.addLast(myStruct.friend);
                System.out.println(user.getUid()+" "+user.getEmail()+" "+user.getName()+" "+user.getPhoto()+" "+user.getPsswd()+" "+user.getSign()
                        +" "+user.getUname()+" "+user.getStartTime()+" "+user.getGender()+" "+user.getStatus());
            }
            System.out.println(ID+" 查询ID成功");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ID+" 查询ID错误");
        }
        return myStruct.friendlist;
    }
    static public MyStruct.User rInfo(String ID){
        /*
        查询个人信息；
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
                //user.setPsswd(result.getString("psswd"));
                user.setSign(result.getString("sign"));
                user.setStartTime(result.getLong("startTime"));
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
            MyStruct.User user=rInfo(friend.getU().getUid());
            String sql="insert into `friend` (`me` ,`ta` ,`startTime` ,`remark` ,`tid`) values(\""
                    +ID+"\",\""
                    +friend.getU().getUid()+"\","
                    +friend.getStartTime()+",\""
                    +friend.getRemark()+"\",\""
                    +friend.getTid()+ "\")";
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
    static public boolean wAddG(MyStruct.User user, String IDG){
        /*
        修改群列表，添加群；
         */
        try {
            String sql="insert into `belong` (`gid` ,`uid` ,`nickname` ) values(\""
                    +IDG+"\",\""
                    +user.getUid()+"\",\""
                    +user.getUname()+"\")";
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(sql);
            String sql1="update `groups` set `sum`=`sum`+1  where `gid`=\""+IDG+"\"";
            Statement statement1=con.createStatement();
            statement1.executeUpdate(sql1);
            System.out.println("添加群成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("添加群错误");
            return false;
        }
    }
    static public boolean wDeleteF(String IDA,String IDB){
        /*
        修改好友列表，删除好友；
         */
        try {
            String sql="delete from `friend` where `me`=\""+IDA+"\" and `ta`=\""+IDB+"\"";
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(sql);
            if(result>0)
              System.out.println("删除好友成功");
            else
                System.out.println("删除好友失败");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("删除好友错误");
            return false;
        }
    }
    static public boolean wDeleteG(String IDA,String IDG){
        /*
        修改群列表，退出群聊；
         */
        try {
            String sql="delete from `belong` where `gid`=(\""
                    +IDG+"\" and `uid`=\""
                    +IDA+"\")";
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(sql);
            String sql1="update `groups` set `sum`=`sum`-1  where `gid`=\""+IDG+"\"";
            Statement statement1=con.createStatement();
            statement1.executeUpdate(sql1);
            System.out.println("添加群成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("添加群错误");
            return false;
        }
    }
    static public boolean wCreateG(MyStruct.Group group){
        /*
        组建群聊；
         */
        try {
            String sql="insert into `groups` (`gid` ,`gname` ,`photo` ,`leader` ,`sign`,`startTime`,`sum`) values(\""
                    +group.getGid()+"\",\""
                    +group.getGname()+"\",\""
                    +group.getPhoto()+"\",\""
                    +group.getLeader()+"\",\""
                    +group.getSign()+"\","
                    +group.getStartTime()+",\""
                    +group.getList().size()+ "\")" ;
            Statement statement = con.createStatement();

            int result = statement.executeUpdate(sql);
            System.out.println("创建群聊成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("创建群聊错误");
            return false;
        }
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
    static public MyStruct.Group rGroupInfo(String ID){
        /*
        查询群信息；
         */
         MyStruct.Group group=new MyStruct.Group();
        try {
            String sql="SELECT * FROM `groups` WHERE `gid`=\"" + ID+"\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            if (result.next()) {
                group.setGid(result.getString("gid"));
                group.setGname(result.getString("gname"));
                group.setLeader(result.getString("leader"));
                LinkedList<String> IDs=rGroupUser(ID); //查询群内成员ID
                LinkedList<MyStruct.User> users=new LinkedList<MyStruct.User>();
                for(int i=0;i<IDs.size();i++){
                    users.addLast(rInfo(IDs.get(i)));//根据ID查找USER信息
                }
                group.setList(users);
                group.setPhoto(result.getString("photo"));
                group.setSign(result.getString("sign"));
                group.setStartTime(result.getLong("startTime"));
            }
            System.out.println(ID+" 查询群信息成功");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ID+" 查询群信息错误");
        }
        return group;
    }
    static public LinkedList<MyStruct.Group> rGroupList(String ID){
        /*
        查询群列表；
         */
        LinkedList<MyStruct.Group> groups=new LinkedList<MyStruct.Group>();
        try {
            //先查找ID加入的所有群的群号
            String sql="SELECT `gid` FROM `belong` WHERE `uid`=\"" + ID+"\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                //根据群号查其他所有的群成员和群信息
                groups.addLast(rGroupInfo(result.getString("gid")));
            }
            System.out.println(ID+" 查询群列表成功");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(ID+" 查询群列表错误");
        }
        return groups;
    }
    static public boolean wUpdateF(String ID,MyStruct.Friend friend){
        /*
        修改好友列表，添加好友；
         */
        try {
            MyStruct.User user=rInfo(friend.getU().getUid());
            String sql="insert into `friend` (`me` ,`ta` ,`startTime` ,`remark` ,`tid`) values(\""
                    +ID+"\",\""
                    +friend.getU().getUid()+"\","
                    +friend.getStartTime()+",\""
                    +friend.getRemark()+"\",\""
                    +friend.getTid()+ "\")"+"\n"+"on duplicate key update `tid` =\""+ friend.getTid()+"\"";
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(sql);
            System.out.println("更新好友成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("更新好友错误");
            return false;
        }
    }
}