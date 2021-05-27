package Framework.SessionLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OfflineMsgQueue {
    public ConcurrentHashMap<String, List<Object>> offlineMsgQueue = new ConcurrentHashMap<>();
    public boolean write(String userName,Object msg){
        if(offlineMsgQueue.get(userName) != null){
            offlineMsgQueue.get(userName).add(msg);
        }
        else{
            List<Object> offLineMsgList = new ArrayList<Object>();
            offLineMsgList.add(msg);
            offlineMsgQueue.put(userName,offLineMsgList);
        }
        return true;
    }
    public List<Object> read(String userName){
        List<Object> offlineMsgList = offlineMsgQueue.get(userName);
        offlineMsgQueue.remove(userName);
        return offlineMsgList;
    }
}
