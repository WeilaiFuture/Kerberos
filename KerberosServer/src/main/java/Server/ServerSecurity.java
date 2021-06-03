package Server;

import Json.MyStruct;

import java.util.Calendar;

public class ServerSecurity {

    public static MyStruct.Certificate createCertif(String ID,String pk){
        /*
        生成证书
         */
        //用Calendar类提供的方法获取年、月、日、时、分、秒
        Calendar cal=Calendar.getInstance();
        int year  =cal.get(Calendar.YEAR);   //年
        int month =cal.get(Calendar.MONTH)+1;  //月  默认是从0开始  即1月获取到的是0
        int day   =cal.get(Calendar.DAY_OF_MONTH);  //日，即一个月中的第几天
        int hour  =cal.get(Calendar.HOUR_OF_DAY);  //小时
        int minute=cal.get(Calendar.MINUTE);   //分
        int second=cal.get(Calendar.SECOND);  //秒

        //拼接成字符串输出
        String date=year+"-"+month+"-"+(day+1)+" "+hour+":"+minute+":"+second;
        MyStruct.Certificate certificate=new MyStruct.Certificate();
        certificate.setPk(pk);
        certificate.setName(ID);
        certificate.setDeadline(date);
        certificate.setVersion("1");
        certificate.setSerial("1");
        return certificate;
    }

}
