package Kerberos.TGS;

import Framework.SessionLayer.Handlers.SessionHandler;
import Framework.SessionLayer.SessionLayer;
import Json.MyJson;
import Json.MyStruct;
import Kerberos.DateBaseOperation;
import SecurityUtils.DESHandler;
import org.apache.commons.lang3.RandomStringUtils;

public class TGSHandler extends SessionHandler {
    /*
    1 只有一个的入口函数
    2 在构造的时候需要定下来，构造函数要可以抛出异常
     */

    TGSHandler(){

        String Kc = DateBaseOperation.readKc(TGSConfig.TGSID);
        if(Kc == null){
            MyStruct.Certificate certificate = new MyStruct.Certificate();
            //myStruct.certificate.setPk(null);
            //myStruct.certificate.setDeadline(null);

            certificate.setName(TGSConfig.TGSID);

            DateBaseOperation.writeCertif(certificate);
            DateBaseOperation.writeKc(TGSConfig.TGSID,RandomStringUtils.randomAlphanumeric(8));
        }
    }
    @Override
    public void receive(String channelName,Object msg) {

        MyJson.Order receiveOrder = MyJson.StringToOrder((String) msg);
        switch (receiveOrder.getMsgType()){
            case "0005":{
                /*
                 * 一条0005信息的处理过程
                 * 1
                 * 2
                 * 3 从数据库中读取Ktgs、Kv
                 */
                System.out.println(receiveOrder.getExtend());
                MyStruct receiveExtend = MyJson.StringToStruct(receiveOrder.getExtend());
                //设定TGS的ID
                String IDtgs = TGSConfig.TGSID;
                //从message3中读取IDv
                String IDv = receiveExtend.message3.getIdv();

                //从数据库中读取Ktgs & Kv
                String Ktgs = DateBaseOperation.readKc(IDtgs);
                String Kv = DateBaseOperation.readKc(IDv);
                //获取接收到的ticketTGS
                MyStruct ticketTGS = MyJson.StringToStruct(DESHandler.DecryptDES(receiveExtend.message3.getT(),Ktgs));
                //从ticketTGS中读取Kc_tgs & IDc
                String Kc_tgs = ticketTGS.ticket.getKey();
                String IDc = ticketTGS.ticket.getIdc();
                //随机生成Kc_v
                String Kc_v = RandomStringUtils.randomAlphanumeric(8);

                //生成ticketV
                MyStruct.Ticket t=new MyStruct.Ticket();
                t.setKey(Kc_v);
                t.setIdc(IDc);
                t.setAdc(null);
                t.setIdt(IDv);
                t.setLifetime(null);
                t.setTs(null);

                // 生成message4
                MyStruct sendExtend = new MyStruct();
                sendExtend.message4 = new MyStruct.Message4();
                sendExtend.message4.setKey(Kc_v);
                sendExtend.message4.setIdv(IDv);
                sendExtend.message4.setTs(null);
                sendExtend.message4.setT(DESHandler.EncryptDES(MyJson.ObjectToString(t),Kv));

                //加密、填写extend字段
                receiveOrder.setExtend(DESHandler.EncryptDES(MyJson.StructToString(sendExtend),Kc_tgs));
                //更改消息类型
                receiveOrder.setMsgType("0006");
                //源地址和目的地址调换位置
                String src = receiveOrder.getSrc();
                receiveOrder.setSrc(receiveOrder.getDst());
                receiveOrder.setDst(src);
                //发送信息
                SessionLayer.send(channelName,MyJson.OrderToString(receiveOrder));
                System.out.println("回复：0006");
                break;
            }
            default:{
                break;
            }
        }
    }
}
