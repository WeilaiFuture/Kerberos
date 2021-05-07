package Framework;

public class FunctionSet {
    //其他AS、TGS、Server继承此类
    public FunctionSet(){}
    //函数入口，参数为收到的报文
    public boolean Inter(String message){
        return false;
    }
}
