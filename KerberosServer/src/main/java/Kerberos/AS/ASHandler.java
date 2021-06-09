package Kerberos.AS;

import Framework.SessionLayer.Handlers.SessionHandler;
import Framework.SessionLayer.SessionLayer;
import Json.MyJson;
import Json.MyStruct;
import Kerberos.DateBaseOperation;
import SecurityUtils.DESHandler;
import SecurityUtils.RSAHandler;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.interfaces.RSAPublicKey;

public class ASHandler extends SessionHandler {
    /*
    1 只有一个的入口函数
    2 在构造的时候需要定下来，构造函数要可以抛出异常
     */

    @Override
    public void receive(String channelName,Object msg) {
        /*
         * AS收到两种报文，一种是证书报文
         * 另一种是认证报文
         */
        MyJson.Order receiveOrder = MyJson.StringToOrder((String) msg);
        System.out.println(receiveOrder.getSrc());
        switch (receiveOrder.getMsgType()){
            case "0001":{
                //从报文中读取公钥
                MyStruct receiveExtend = MyJson.StringToStruct(receiveOrder.getExtend());
                String publicKey = receiveExtend.certificate.getPk();
                //随机生成一个字符串作为对称钥
                String Kc = RandomStringUtils.randomAlphanumeric(8);
                //String Kc = "12345678";
                //使用公钥加密对称钥
                try{
                    MyStruct sendExtend = new MyStruct();
                    RSAPublicKey rsaPublicKey = RSAHandler.getPublicKey(publicKey);
                    sendExtend.my_k = new MyStruct.My_k();
                    sendExtend.my_k.setKey(RSAHandler.publicEncrypt(Kc,rsaPublicKey));
                    //填写extend字段
                    receiveOrder.setExtend(MyJson.StructToString(sendExtend));
                    //更改消息类型
                    receiveOrder.setMsgType("0002");
                    //源地址和目的地址调换位置
                    String src = receiveOrder.getSrc();
                    receiveOrder.setSrc(receiveOrder.getDst());
                    receiveOrder.setDst(src);
                    //写入数据库1 写入证书 2 写入Kc（代码中的SessionKey）
                    DateBaseOperation.writeCertif(receiveExtend.certificate);
                    DateBaseOperation.writeKc(receiveExtend.certificate.getName(),Kc);
                    //发送
                    System.out.println(MyJson.OrderToString(receiveOrder));
                    SessionLayer.send(channelName,MyJson.OrderToString(receiveOrder));
                    MyJson.StringToOrder(MyJson.OrderToString(receiveOrder));
                    System.out.println("回复：0002");
                    //SessionLayer.logOut(channelName);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }

            case "0003":{
                MyStruct receiveExtend = MyJson.StringToStruct(receiveOrder.getExtend());
                //获取IDc
                String IDc = receiveExtend.message1.getIdc();
                //获取TGS的ID
                String IDtgs = "TGS";
                //查询数据库，根据IDc查找Kc
                String Kc = DateBaseOperation.readKc(IDc);
                String Ktgs = DateBaseOperation.readKc(IDtgs);
                //判断Kc、Ktgs是否是空的

                //生成Kc_tgs
                String Kc_tgs = RandomStringUtils.randomAlphanumeric(8);

                MyStruct sendExtend = new MyStruct();
                MyStruct ticket = new MyStruct();

                //生成Ticket
                ticket.ticket = new MyStruct.Ticket();
                ticket.ticket.setKey(Kc_tgs);
                ticket.ticket.setIdc(IDc);
                ticket.ticket.setAdc("");
                ticket.ticket.setIdt(IDtgs);
                ticket.ticket.setTs("");
                ticket.ticket.setLifetime("");

                //填写字段
                sendExtend.message2 = new MyStruct.Message2();
                sendExtend.message2 = new MyStruct.Message2();
                sendExtend.message2.setKey(Kc_tgs);
                sendExtend.message2.setIdt(receiveExtend.message1.getIdt());
                sendExtend.message2.setTs("");
                sendExtend.message2.setLifetime("");
                sendExtend.message2.setT(DESHandler.EncryptDES(MyJson.StructToString(ticket),Ktgs));
                //填写extend字段
                receiveOrder.setExtend(DESHandler.EncryptDES(MyJson.StructToString(sendExtend),Kc));
                //更改消息类型
                receiveOrder.setMsgType("0004");
                //源地址和目的地址调换位置
                String src = receiveOrder.getSrc();
                receiveOrder.setSrc(receiveOrder.getDst());
                receiveOrder.setDst(src);
                //发送
                SessionLayer.send(channelName,MyJson.OrderToString(receiveOrder));
                System.out.println("回复：0004");
                //SessionLayer.logOut(channelName);
                break;
            }

            default:

                break;
        }
    }
}
