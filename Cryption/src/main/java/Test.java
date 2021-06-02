
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.Map;

import static SecurityUtils.DESHandler.*;
import static SecurityUtils.RSAHandler.*;
import static SecurityUtils.RSAUtils.*;


/**
 * 2 * @Author: WeiLai
 * 3 * @Date: 2021/5/30 17:19
 * 4
 */
public class Test {
    public static void main(String[] args) throws Exception {
       //DES_Test();
       RSA_Test();
    }
    public static void DES_Test()
    {
        try {
            BufferedReader sr_P = new BufferedReader(new InputStreamReader(new FileInputStream("Plaintext.txt")));
            BufferedReader sr_K = new BufferedReader(new InputStreamReader(new FileInputStream("Kc.txt")));
            BufferedWriter sw_EN = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("DESENout.txt")));
            BufferedWriter sw_DE = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("DESDEout.txt")));
            String line = null;
            while ((line = sr_P.readLine()) != null) {
                String kc = sr_K.readLine();
                String Enc_str = EncryptDES(line, kc);
                String Dec_str = DecryptDES(Enc_str, kc);
                sw_DE.write(Dec_str);
                sw_DE.newLine();
                sw_EN.write(Enc_str);
                sw_EN.newLine();


            }
            sr_P.close();
            sr_K.close();
            sw_EN.close();
            sw_DE.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void RSA_Test() throws Exception {

        try {
            String line;
            BufferedReader sr_P = new BufferedReader(new InputStreamReader(new FileInputStream("Plaintext.txt")));
            BufferedReader sr_K = new BufferedReader(new InputStreamReader(new FileInputStream("Kc.txt")));
            BufferedWriter sw_EN = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("RSAENout.txt")));
            BufferedWriter sw_DE = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("RSADEout.txt")));

            BufferedWriter sw_dig = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Digitial.txt")));
            //BufferedWriter sw_di = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Dig.txt")));
            //BufferedWriter sw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("PKB.txt")));
            //BufferedWriter sw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("PKI.txt")));
            BufferedWriter sw3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("PKB1.txt")));
            BufferedWriter sw4 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("PKI1.txt")));
            //BufferedReader sw1 = new BufferedReader(new InputStreamReader(new FileInputStream("PKB.txt")));
            //BufferedReader sw2 = new BufferedReader(new InputStreamReader(new FileInputStream("PKI.txt")));
            BufferedReader sr1 = new BufferedReader(new InputStreamReader(new FileInputStream("PKB.txt")));
            BufferedReader sr2 = new BufferedReader(new InputStreamReader(new FileInputStream("PKI.txt")));
            String PKB=sr1.readLine();
            String PKI=sr2.readLine();
            String PKB1=getRSAPublicKeyAsNetFormat( Base64.decodeBase64(PKB));
            String PKI1=getRSAPrivateKeyAsNetFormat( Base64.decodeBase64(PKI));
            while ((line = sr_P.readLine()) != null)
            {
                //Map<String, String> map = createKeys(1024);
                //String  PKB = map.get("publicKey");
                //String  PKI = map.get("privateKey");
                //String PKB=sr1.readLine();
                //String PKI=sr2.readLine();
                //String PKB1=getRSAPublicKeyAsNetFormat( Base64.decodeBase64(PKB));
                //String PKI1=getRSAPrivateKeyAsNetFormat( Base64.decodeBase64(PKI));
                //RSALibrary.RSAKey(out PKI, out PKB);
                //PKB=sw1.readLine();
                //PKI=sw2.readLine();
                String Enc_str = publicEncrypt(line,getPublicKey(PKB));
                String Dec_str = privateDecrypt(Enc_str,getPrivateKey(PKI)) ;
                String dig_str=generateSign(getPrivateKey(PKI),line);
                // RSALibrary.SignatureFormatter(PKI, line, ref dig_str);
                sw_DE.write(Dec_str);
                sw_EN.write(Enc_str);
                sw_EN.newLine();
                sw_DE.newLine();
                sw_dig.write(dig_str);
                sw_dig.newLine();
                if (verifySign(getPublicKey(PKB), dig_str, line))
                    System.out.println("1");
                //sw_di.WriteLine(dig_str);
                //sw1.write(PKB);
                //sw2.write(PKI);
                sw3.write(PKB1);
                sw4.write(PKI1);
                //sw1.newLine();
                //sw2.newLine();
                sw3.newLine();
                sw4.newLine();
            }
            sr_P.close();
            sr_K.close();
            sw_EN.close();
            sw_DE.close();
            //sw_di.Close();
            //sw_dig.Close();
            //sw1.close();
            //sw2.close();
            sw3.close();
            sw4.close();
            sw_dig.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
