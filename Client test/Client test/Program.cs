using Client;
using System;
using System.IO;
using static Client.DESLibrary;

namespace Client_test
{
    class Program
    {
        static void Main(string[] args)
        {
            RSALibrary RSA = new RSALibrary();
            //DESLibrary DES = new DESLibrary();

            DES_test();
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
                string Dec_str = DecryptDES(line, kc);
                string Enc_str = EncryptDES(Dec_str, kc);
                sw_EN.WriteLine(Dec_str);
                sw_DE.WriteLine(Enc_str);

            }
            sr_P.Close();
            sr_K.Close();
            sw_EN.Close();
            sw_DE.Close();
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
