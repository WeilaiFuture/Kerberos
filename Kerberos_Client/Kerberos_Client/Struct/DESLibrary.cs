using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace Kerberos_Client
{
    class DESLibrary
    {
        private static string ori = "12345678";
        //默认密钥向量
        private static byte[] Keys = ASCIIEncoding.ASCII.GetBytes(ori);

        /// <summary>
        /// DES加密字符串
        /// </summary>
        /// <param name="encryptString">待加密的字符串</param>
        /// <param name="encryptKey">加密密钥,要求为8位</param>
        /// <returns>加密成功返回加密后的字符串，失败返回源串 </returns>
        public static string EncryptDES(string encryptString, string encryptKey)
        {
            try
            {
                //byte[] rgbKey = Encoding.UTF8.GetBytes(encryptKey.Substring(0, 8));//转换为字节
               // byte[] rgbIV = Keys;
                byte[] inputByteArray = Encoding.UTF8.GetBytes(encryptString);
                DESCryptoServiceProvider dCSP = new DESCryptoServiceProvider();//实例化数据加密标准
                dCSP.Key = Encoding.ASCII.GetBytes(encryptKey);
                dCSP.IV = Encoding.ASCII.GetBytes(ori);
                MemoryStream mStream = new MemoryStream();//实例化内存流
                                                          //将数据流链接到加密转换的流
                CryptoStream cStream = new CryptoStream(mStream, dCSP.CreateEncryptor(), CryptoStreamMode.Write);
                cStream.Write(inputByteArray, 0, inputByteArray.Length);
                cStream.FlushFinalBlock();
                return Convert.ToBase64String(mStream.ToArray());
            }
            catch
            {
                return encryptString;
            }
        }

        /// <summary>
        /// DES解密字符串
        /// </summary>
        /// <param name="decryptString">待解密的字符串</param>
        /// <param name="decryptKey">解密密钥,要求为8位,和加密密钥相同</param>
        /// <returns>解密成功返回解密后的字符串，失败返源串</returns>
        public static string DecryptDES(string decryptString, string decryptKey)
        {
            try
            {
                //byte[] rgbKey = Encoding.UTF8.GetBytes(decryptKey);
                //byte[] rgbIV = Keys;
                byte[] inputByteArray = Convert.FromBase64String(decryptString);
                DESCryptoServiceProvider DCSP = new DESCryptoServiceProvider();
                DCSP.Key = Encoding.ASCII.GetBytes(decryptKey);
                DCSP.IV = Encoding.ASCII.GetBytes(ori);
                MemoryStream mStream = new MemoryStream();
                CryptoStream cStream = new CryptoStream(mStream, DCSP.CreateDecryptor(), CryptoStreamMode.Write);
                cStream.Write(inputByteArray, 0, inputByteArray.Length);
                cStream.FlushFinalBlock();
                return Encoding.UTF8.GetString(mStream.ToArray());
            }
            catch
            {
                return decryptString;
            }
        }
    }

}
