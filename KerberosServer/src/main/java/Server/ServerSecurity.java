package Server;

import Json.MyStruct;

public class ServerSecurity {

    public static MyStruct.Certificate createCertif(String ID,String pk){
        /*
        生成证书
         */
        MyStruct.Certificate certificate=new MyStruct.Certificate();
        certificate.setPk(pk);
        certificate.setName(ID);
        //certificate.setDeadline();
        String message="";
        return certificate;
    }

}
