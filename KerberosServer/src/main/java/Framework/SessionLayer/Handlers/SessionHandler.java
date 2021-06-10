package Framework.SessionLayer.Handlers;

public abstract class SessionHandler {
    /*
     * 信息处理模块的唯一入口函数，不接受抛出异常，异常需要在内部处理干净
     */
    public abstract void receive(String channelName,Object msg) throws Exception;


}
