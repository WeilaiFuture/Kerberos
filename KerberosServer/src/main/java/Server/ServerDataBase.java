package Server;

import Json.MyStruct;

import java.sql.*;
import java.util.LinkedList;

public class ServerDataBase {
    /*
    包含所有数据库操作的封装函数；
     */
    static private Connection con=null; // 声明Connection对象
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

    static public boolean wCertif(MyStruct struct) {
        /*
        向数据库存入证书；
         */
        try {
            String sql="insert into 'Certificate' ('version' ,'serial' ,'deadline' ,'name' ,'pk') values(\""
                    +struct.certificate.getVersion()+"\",\""
                    +struct.certificate.getSerial()+"\",\""
                    +struct.certificate.getDeadline()+"\",\""
                    +struct.certificate.getName()+"\",\""
                    +struct.certificate.getPk()+ "\")";
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("存储证书错误");
        }
        return false;
    }

    static public boolean wRegister(MyStruct struct) {
        /*
        向数据库存入注册信息；
         */
        try {
            Statement sql=con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("存储注册信息错误");
        }
        return false;
    }
    static public boolean wLogin(String ID,String status){
        /*
        向数据库写入登陆状态；
         */
        try {
            String sql="update 'users' set ('status') values(1)";
            Statement statement=con.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("写入登陆状态错误");
        }
        return false;
    }
    static public LinkedList<MyStruct.Friend> rFriendList(String ID){
        /*
        查询好友列表；
         */
        LinkedList<MyStruct.Friend> friends=new LinkedList<MyStruct.Friend>();
        try {
            String sql="SELECT * FROM 'friend' WHERE 'me'=\"" + ID + "\"";
            Statement statement=con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                MyStruct.Friend friend=new MyStruct.Friend();
                friend.setRemark(result.getString("remark"));
                friend.setStartTime(result.getInt("startTime"));

                String sql1="SELECT 'tname' FROM 'tags' WHERE 'tid'="+result.getInt("tid")+  " and 'uid'=\""+ID+"\"";
                Statement statement1=con.createStatement();
                ResultSet result1 = statement.executeQuery(sql1);
                friend.setTid(result1.getString("tname"));

                MyStruct.User user=rSearchID(result.getString("ta"));
                friend.setU(user);

                friends.addLast(friend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("查询好友列表错误");
        }
        return friends;
    }
    static public LinkedList<String> rOnLineFriend(String ID){
        /*
        查询在线状态好友ID；
         */
        return new LinkedList<String>();
    }
     static public LinkedList<String> rGroupUser(String ID){
        /*
        查询群内成员ID；
         */
         return new LinkedList<String>();
     }
    static public MyStruct.User rSearchID(String ID){
        /*
        查询用户；
         */
        return new MyStruct.User();
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
         */
         return false;
     }
    static public boolean wAddF(String IDA,String IDB){
        /*
        修改好友列表，添加好友；
         */
        return false;
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
        return false;
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