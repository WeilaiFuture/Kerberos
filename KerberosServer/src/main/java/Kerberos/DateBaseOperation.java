package Kerberos;

import Json.MyJson;
import Json.MyStruct;
import StateMachine.Application;
import org.springframework.boot.SpringApplication;

import java.sql.*;
import java.util.LinkedList;

import static Json.MyJson.OrderToString;

public class DateBaseOperation {
    /*
包含所有数据库操作的封装函数；
 */
    static private Connection connection=null; // 声明Connection对象
    public static void main(String []args) {

    }
    static public Connection connectDataBase() {
        /*
        连接数据库；
         */
        Connection conn = null;
        // no ssl certificate:
        //information_schema@rm-uf6t4cbyfz681x569.mysql.rds.aliyuncs.com:3306【rm-uf6t4cbyfz681x569】
        String url = "jdbc:mysql://" +
                "rm-uf6t4cbyfz681x569jo.mysql.rds.aliyuncs.com:3306/kerbors" +
                "?sslmode=require" +
                "&connectTimeout=300000" +
                "&socketTimeout=6000000";
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
        connection=conn;
        return conn;
    }

    static public boolean writeCertif(MyStruct.Certificate certificate){
        try{
            if(connection == null||connection.isClosed()){
                connectDataBase();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

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
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("无法存储证书");
            return false;
        }
    }

    static public boolean writeKc(String ID,String K){
        try{
            if(connection == null||connection.isClosed()){
                connectDataBase();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        /*
        向数据库存入Kcv；
         */
        try {
            String sql="update `Certificate` set `Kc`=\""+K+"\" where `name`=\""+ID+"\"";
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("存储Kcv错误");
            return false;
        }
    }
    static public String readKc(String ID){
        try{
            if(connection == null||connection.isClosed()){
                connectDataBase();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        /*
        向数据库读取Kcv；
         */
        try {
            String sql="SELECT `Kc` FROM `Certificate` WHERE `name`=\"" + ID + "\"";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            result.next();
            return result.getString("Kc");
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("读取Kc错误");
            return null;
        }
    }
}
