package SecurityUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
/**
 * 2 * @Author: WeiLai
 * 3 * @Date: 2021/5/31 16:59
 * 4
 */
public class RSAUtils {

    /**
     * 私钥转换成C#格式
     * @param encodedPrivateKey
     * @return
     */
    public static String getRSAPrivateKeyAsNetFormat(byte[] encodedPrivateKey) {
        try {
            StringBuffer buff = new StringBuffer(1024);

            PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(
                    encodedPrivateKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory
                    .generatePrivate(pvkKeySpec);

            buff.append("<RSAKeyValue>");
            buff.append("<Modulus>"
                    + encodeBase64(removeMSZero(pvkKey.getModulus()
                    .toByteArray())) + "</Modulus>");

            buff.append("<Exponent>"
                    + encodeBase64(removeMSZero(pvkKey.getPublicExponent()
                    .toByteArray())) + "</Exponent>");

            buff.append("<P>"
                    + encodeBase64(removeMSZero(pvkKey.getPrimeP()
                    .toByteArray())) + "</P>");

            buff.append("<Q>"
                    + encodeBase64(removeMSZero(pvkKey.getPrimeQ()
                    .toByteArray())) + "</Q>");

            buff.append("<DP>"
                    + encodeBase64(removeMSZero(pvkKey.getPrimeExponentP()
                    .toByteArray())) + "</DP>");

            buff.append("<DQ>"
                    + encodeBase64(removeMSZero(pvkKey.getPrimeExponentQ()
                    .toByteArray())) + "</DQ>");

            buff.append("<InverseQ>"
                    + encodeBase64(removeMSZero(pvkKey.getCrtCoefficient()
                    .toByteArray())) + "</InverseQ>");

            buff.append("<D>"
                    + encodeBase64(removeMSZero(pvkKey.getPrivateExponent()
                    .toByteArray())) + "</D>");
            buff.append("</RSAKeyValue>");

            return buff.toString();
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    /**
     * 公钥转成C#格式
     * @param encodedPublicKey
     * @return
     */
    public static String getRSAPublicKeyAsNetFormat(byte[] encodedPublicKey) {
        try {
            StringBuffer buff = new StringBuffer(1024);

            //Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey pukKey = (RSAPublicKey) keyFactory
                    .generatePublic(new X509EncodedKeySpec(encodedPublicKey));

            buff.append("<RSAKeyValue>");
            buff.append("<Modulus>"
                    + encodeBase64(removeMSZero(pukKey.getModulus()
                    .toByteArray())) + "</Modulus>");
            buff.append("<Exponent>"
                    + encodeBase64(removeMSZero(pukKey.getPublicExponent()
                    .toByteArray())) + "</Exponent>");
            buff.append("</RSAKeyValue>");
            return buff.toString();
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    /**
     * base64编码
     * @param input
     * @return
     * @throws Exception
     */
    public static String encodeBase64(byte[] input) throws Exception {
        Class clazz = Class
                .forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        Method mainMethod = clazz.getMethod("encode", byte[].class);
        mainMethod.setAccessible(true);
        Object retObj = mainMethod.invoke(null, new Object[] { input });
        return (String) retObj;
    }

    private static byte[] removeMSZero(byte[] data) {
        byte[] data1;
        int len = data.length;
        if (data[0] == 0) {
            data1 = new byte[data.length - 1];
            System.arraycopy(data, 1, data1, 0, len - 1);
        } else
            data1 = data;

        return data1;
    }
}