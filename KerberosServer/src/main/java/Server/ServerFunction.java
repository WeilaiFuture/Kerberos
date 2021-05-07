package Server;

import Framework.FunctionSet;

public class ServerFunction extends FunctionSet {
    //继承方法集
    ServerFunction(){
        //调用函数，发送本地证书给AS
    }
    public boolean Integer(String message){
        //判断消息头部，转到相应的处理函数
        return true;
    }
}
