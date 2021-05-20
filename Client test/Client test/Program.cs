using Client;
using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using static Client.DESLibrary;

namespace Client_test
{
    class Program
    {
        static void Main(string[] args)
        {
            /// <summary>
            /// 利用RSA生成的密钥来加密解密
            /// </summary>
            DES_test();
            RSA_test();
        }
        public static void my_init()
        {
            string line = "";
            StreamReader sr = new StreamReader("Plaintext.txt");
            StreamWriter sw = new StreamWriter("Kc.txt");
            while ((line = sr.ReadLine()) != null)
            {
                string StrRandom = random_str(8);
                sw.WriteLine(StrRandom);
            }
            sw.Close();
            sr.Close();
        }
        static void DES_test()
        {
            string line;
            StreamReader sr_P = new StreamReader("Plaintext.txt");
            StreamReader sr_K = new StreamReader("Kc.txt");
            StreamWriter sw_EN = new StreamWriter("DESENout.txt");
            StreamWriter sw_DE = new StreamWriter("DESDEout.txt");
            while ((line = sr_P.ReadLine()) != null)
            {
                string kc = sr_K.ReadLine();
                string Enc_str = EncryptDES(line, kc);
                string Dec_str = DecryptDES(Enc_str, kc);
                sw_EN.WriteLine(Dec_str);
                sw_DE.WriteLine(Enc_str);

            }
            sr_P.Close();
            sr_K.Close();
            sw_EN.Close();
            sw_DE.Close();
        }

        static void RSA_test()
        {
            string line;
            StreamReader sr_P = new StreamReader("Plaintext.txt");
            StreamReader sr_K = new StreamReader("Kc.txt");
            StreamWriter sw_EN = new StreamWriter("RSAENout.txt");
            StreamWriter sw_DE = new StreamWriter("RSADEout.txt");
            StreamWriter sw_dig = new StreamWriter("Digitial.txt");
            StreamWriter sw_di = new StreamWriter("Dig.txt");
            string PKB, PKI;
            while ((line = sr_P.ReadLine()) != null)
            {
                RSALibrary.RSAKey(out PKI, out PKB);

                string Enc_str = RSALibrary.RSAEncrypt(PKB, line);
                string Dec_str = RSALibrary.RSADecrypt(PKI, Enc_str) ;
                string dig_str=string.Empty;
                RSALibrary.SignatureFormatter(PKI, line, ref dig_str);
                sw_EN.WriteLine(Dec_str);
                sw_DE.WriteLine(Enc_str);
                sw_dig.WriteLine(dig_str);
                if (RSALibrary.SignatureDeformatter(PKB, dig_str, line))
                    Console.WriteLine("1");
                sw_di.WriteLine(dig_str);

            }
            sr_P.Close();
            sr_K.Close();
            sw_EN.Close();
            sw_DE.Close();
            sw_di.Close();
            sw_dig.Close();

        }
        //生成字母和数字随机数
        public static string random_str(int length, bool sleep)
        {
            if (sleep)
            {
                System.Threading.Thread.Sleep(3);
            }
            char[] Pattern = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o','p','q','r','s','t','u','v','w','x','y','z' };
            string result = "";
            int n = Pattern.Length;
            System.Random random = new Random(~unchecked((int)DateTime.Now.Ticks));
            for (int i = 0; i < length; i++)
            {
                int rnd = random.Next(0, n);
                result += Pattern[rnd];
            }
            return result;
        }

        public static string random_str(int length)
        {
            return random_str(length, false);
        }
    }
}
