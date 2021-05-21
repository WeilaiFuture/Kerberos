package Security;

import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class DESede {
    /**
     * 生成密钥
     * @throws Exception
     */
    public static byte[] initKey(){
        try {
            //密钥生成
            KeyGenerator keyGen;
            keyGen = KeyGenerator.getInstance("DESede");
            //初始化密钥生成器
            keyGen.init(168);   //可指定密钥长度为128或者168，默认168
            //生成密钥
            SecretKey secretKey = keyGen.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  null;

    }

    /**
     * 加密
     * @throws Exception
     */
    public static byte[] encrypt3DES(byte[] data, byte[] key){
        try {
            //恢复密钥
            SecretKey secretKey = new SecretKeySpec(key, "DESede");
            //Cipher完成加密
            Cipher cipher;
            cipher = Cipher.getInstance("DESede");
            //cipher初始化
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypt = cipher.doFinal(data);
            return encrypt;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(BadPaddingException e) {
            e.printStackTrace();
        } catch(IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 解密
     */
    public static byte[] decrypt3DES(byte[] data, byte[] key) {
        try {
            //恢复密钥
            SecretKey secretKey = new SecretKeySpec(key, "DESede");
            //Cipher完成解密
            Cipher cipher = Cipher.getInstance("DESede");
            //初始化cipher
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] plain = cipher.doFinal(data);

            return plain;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(BadPaddingException e) {
            e.printStackTrace();
        } catch(IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

}
