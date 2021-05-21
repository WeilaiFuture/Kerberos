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

        public String getMsgId() {
            return msgId;
        }

        public String getSrc() {
            return Src;
        }

        public String getDst() {
            return Dst;
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
            return ContentType;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public void setSrc(String src) {
            Src = src;
        }

        public void setDst(String dst) {
            Dst = dst;
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
            ContentType = contentType;
        }
    }
}
