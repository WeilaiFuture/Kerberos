package Json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class MyJson {
   //报文解析
    public static MyStruct ToStruct(String message){
        //获取报文
        MyJson.Order order =MyJson.StringToOrder(message);
        //获取消息
        MyStruct mystruct=MyJson.StringToStruct(order.getExtend());
        return mystruct;
    }
    //序列化
    public static String OrderToString(Order order){
        String jsonOutput= JSON.toJSONString(order);
        return jsonOutput;
    }
    //反序列化
    public static Order StringToOrder(String jsonObject){
        Order order=JSON.parseObject(jsonObject, Order.class);
        return order;
    }
    //序列化
    public static String StructToString(MyStruct struct){
        String jsonOutput= JSON.toJSONString(struct);
        return jsonOutput;
    }
    //反序列化
    public static MyStruct StringToStruct(String jsonObject){
        MyStruct struct=JSON.parseObject(jsonObject, MyStruct.class);
        //MyStruct struct=gson.fromJson(jsonObject, MyStruct.class);
        return struct;
    }

    //序列化
    public static String ObjectToString(Object object){
        String jsonOutput= JSON.toJSONString(object);
        return jsonOutput;
    }
    //反序列化
    public static Object StringToObject(String jsonObject){
        Object object=JSON.parseObject(jsonObject, MyStruct.class);
        return object;
    }
    static public class Order{
        @JSONField(name="msgID")
        String msgId;
        @JSONField(name="Src")
        String src;
        @JSONField(name="Dst")
        String dst;
        @JSONField(name="msgType")
        String msgType;
        @JSONField(name="extend")
        String extend;
        @JSONField(name = "sign")
        String sign;
        @JSONField(name="statusReport")
        boolean statusReport;
        @JSONField(name="sTS")
        String sTS;
        @JSONField(name="rTS")
        String rTS;
        @JSONField(name="ContentType")
        String contentType;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getMsgId() {
            return msgId;
        }

        public String getSrc() {
            return src;
        }

        public String getDst() {
            return dst;
        }

        public String getMsgType() {
            return msgType;
        }

        public String getExtend() {
            return extend;
        }

        public boolean isStatusReport() {
            return statusReport;
        }

        public String getsTS() {
            return sTS;
        }

        public String getrTS() {
            return rTS;
        }

        public String getContentType() {
            return contentType;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public void setExtend(String extend) {
            this.extend = extend;
        }

        public void setStatusReport(boolean statusReport) {
            this.statusReport = statusReport;
        }

        public void setsTS(String sTS) {
            this.sTS = sTS;
        }

        public void setrTS(String rTS) {
            this.rTS = rTS;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }
}
