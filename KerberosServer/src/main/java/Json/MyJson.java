package Json;

import com.alibaba.fastjson.annotation.JSONField;

public class MyJson {
    public class Order{
        @JSONField(name="msgID")
        String msgId;
        @JSONField(name="Src")
        String Src;
        @JSONField(name="Dst")
        String Dst;
        @JSONField(name="msgType")
        String msgType;
        @JSONField(name="extend")
        String extend;
        @JSONField(name="statusReport")
        boolean statusReport;
        @JSONField(name="sTS")
        String sTS;
        @JSONField(name="rTS")
        String rTS;
        @JSONField(name="ContentType")
        String ContentType;
    }
}
