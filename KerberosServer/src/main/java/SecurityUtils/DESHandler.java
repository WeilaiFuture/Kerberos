package SecurityUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.Base64;

/**
 * 2 * @Author: WeiLai
 * 3 * @Date: 2021/5/30 19:24
 * 4
 */
public class DESHandler {
    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "12345678";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";


    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }

    /**
     * DES加密字符串
     *
     * @param encryptKey 加密密码，长度不能够小于8位
     * @param encryptString 待加密字符串
     * @return 加密后内容
     */
    public static String EncryptDES(String encryptString, String encryptKey)
    {
        if (encryptKey== null || encryptKey.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (encryptString == null)
            return null;
        try {
            Key secretKey = generateKey(encryptKey);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(encryptString.getBytes(CHARSET));

            //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
            //Android平台可以使用android.util.Base64
            return new String(Base64.getEncoder().encode(bytes));

        } catch (Exception e) {
            e.printStackTrace();
            return encryptString;
        }

    }

    /**
     * DES解密字符串
     *
     * @param decryptKey 解密密码，长度不能够小于8位
     * @param decryptString 待解密字符串
     * @return 解密后内容
     */
    public static String DecryptDES(String decryptString, String decryptKey)
    {
        if (decryptKey== null || decryptKey.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (decryptString == null)
            return null;
        try {
            Key secretKey = generateKey(decryptKey);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(decryptString.getBytes(CHARSET))), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return decryptString;
        }
    }

}
