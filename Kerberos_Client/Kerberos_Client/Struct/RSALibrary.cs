using Org.BouncyCastle.Asn1.Pkcs;
using Org.BouncyCastle.Asn1.X509;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Math;
using Org.BouncyCastle.Pkcs;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.X509;
using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using System.Xml;

namespace Kerberos_Client
{
    public class RSALibrary
    {
        #region RSA 加密解密
        #region RSA 的密钥产生
        /// <summary>
        /// RSA产生密钥
        /// </summary>
        /// <param name="xmlKeys">私钥</param>
        /// <param name="xmlPublicKey">公钥</param>
        public static void RSAKey(out string xmlKeys, out string xmlPublicKey)
        {
            try
            {
                RSACryptoServiceProvider rsa = new RSACryptoServiceProvider();
                xmlKeys = rsa.ToXmlString(true);
                xmlPublicKey = rsa.ToXmlString(false);
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
        #endregion

        #region RSA加密函数
        //############################################################################## 
        //RSA 方式加密 
        //KEY必须是XML的形式,返回的是字符串 
        //该加密方式有长度限制的！
        //############################################################################## 

        /// <summary>
        /// RSA的加密函数
        /// </summary>
        /// <param name="xmlPublicKey">公钥</param>
        /// <param name="encryptString">待加密的字符串</param>
        /// <returns></returns>
        public static string RSAEncrypt(string xmlPublicKey, string encryptString)
        {
            try
            {
                RSACryptoServiceProvider rsa = new RSACryptoServiceProvider();
                rsa.FromXmlString(xmlPublicKey);
                //把你要加密的内容转换成byte[]
                byte[] PlainTextBArray = Encoding.UTF8.GetBytes(encryptString);
                int MaxBlockSize = rsa.KeySize / 8 - 11;
                if (encryptString.Length <= MaxBlockSize)
                    return Convert.ToBase64String(rsa.Encrypt(PlainTextBArray, false));


                using (MemoryStream PlaiStream = new MemoryStream(PlainTextBArray))
                using (MemoryStream CrypStream = new MemoryStream())
                {
                    byte[] Buffer = new byte[MaxBlockSize];
                    int BlockSize = PlaiStream.Read(Buffer, 0, MaxBlockSize);

                    while (BlockSize > 0)
                    {
                        byte[] ToEncrypt = new byte[BlockSize];
                        Array.Copy(Buffer, 0, ToEncrypt, 0, BlockSize);

                        byte[] Cryptograph = rsa.Encrypt(ToEncrypt, false);
                        CrypStream.Write(Cryptograph, 0, Cryptograph.Length);
                        BlockSize = PlaiStream.Read(Buffer, 0, MaxBlockSize);
                    }
                    return Convert.ToBase64String(CrypStream.ToArray(), Base64FormattingOptions.None);
                }
                ////使用.NET中的Encrypt方法加密
                //byte[] CypherTextBArray = rsa.Encrypt(PlainTextBArray, false);
                ////最后把加密后的byte[]转换成Base64String，这里就是加密后的内容了
                //string EncryptedContent = Convert.ToBase64String(CypherTextBArray);
                //return EncryptedContent;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
        #endregion

        #region RSA的解密函数        
        /// <summary>
        /// RSA的解密函数
        /// </summary>
        /// <param name="xmlPrivateKey">私钥</param>
        /// <param name="decryptString">待解密的字符串</param>
        /// <returns></returns>
        public static string RSADecrypt(string xmlPrivateKey, string decryptString)
        {
            try
            {
                if (string.Empty.Equals(decryptString))
                    return string.Empty;
                RSACryptoServiceProvider rsa = new RSACryptoServiceProvider();
                rsa.FromXmlString(xmlPrivateKey);
                byte[] CiphertextData = Convert.FromBase64String(decryptString);
                int MaxBlockSize = rsa.KeySize / 8;    //解密块最大长度限制
                if (CiphertextData.Length <= MaxBlockSize)
                    return Encoding.UTF8.GetString(rsa.Decrypt(Convert.FromBase64String(decryptString), false));

                using (MemoryStream CrypStream = new MemoryStream(CiphertextData))
                using (MemoryStream PlaiStream = new MemoryStream())
                {
                    byte[] Buffer = new byte[MaxBlockSize];
                    int BlockSize = CrypStream.Read(Buffer, 0, MaxBlockSize);

                    while (BlockSize > 0)
                    {
                        byte[] ToDecrypt = new byte[BlockSize];
                        Array.Copy(Buffer, 0, ToDecrypt, 0, BlockSize);

                        byte[] Plaintext = rsa.Decrypt(ToDecrypt, false);
                        PlaiStream.Write(Plaintext, 0, Plaintext.Length);

                        BlockSize = CrypStream.Read(Buffer, 0, MaxBlockSize);
                    }

                    return Encoding.UTF8.GetString(PlaiStream.ToArray());
                }
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
        #endregion
        #endregion

        #region RSA数字签名
        #region 获取Hash描述表        
        /// <summary>
        /// 获取Hash描述表
        /// </summary>
        /// <param name="strSource">待签名的字符串</param>
        /// <param name="strHashData">Hash描述</param>
        /// <returns></returns>
        public static bool GetHash(string strSource, ref string strHashData)
        {
            try
            {
                //从字符串中取得Hash描述 
                byte[] Buffer;
                byte[] HashData;
                System.Security.Cryptography.HashAlgorithm MD5 = System.Security.Cryptography.HashAlgorithm.Create("MD5");
                Buffer = System.Text.Encoding.GetEncoding("GB2312").GetBytes(strSource);
                HashData = MD5.ComputeHash(Buffer);
                strHashData = Convert.ToBase64String(HashData);
                return true;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
        #endregion

        #region RSA签名
        /// <summary>
        /// RSA签名
        /// </summary>
        /// <param name="strKeyPrivate">私钥</param>
        /// <param name="strHashbyteSignature">待签名Hash描述</param>
        /// <param name="strEncryptedSignatureData">签名后的结果</param>
        /// <returns></returns>
        public static bool SignatureFormatter(string strKeyPrivate, string strHashbyteSignature, ref string strEncryptedSignatureData)
        {
            try
            {
                System.Security.Cryptography.RSACryptoServiceProvider RSA = new System.Security.Cryptography.RSACryptoServiceProvider();
                RSA.FromXmlString(strKeyPrivate);
                byte[] cipherbytes = RSA.SignData(Encoding.UTF8.GetBytes(strHashbyteSignature), "MD5");
                strEncryptedSignatureData= Convert.ToBase64String(cipherbytes);
                return true;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
        #endregion

        #region RSA 签名验证
        /// <summary>
        /// RSA签名验证
        /// </summary>
        /// <param name="strKeyPublic">公钥</param>
        /// <param name="strHashbyteDeformatter">Hash描述</param>
        /// <param name="strDeformatterData">签名后的结果</param>
        /// <returns></returns>
        public static bool SignatureDeformatter(string strKeyPublic, string strHashbyteDeformatter, string strDeformatterData)
        {
            try
            {
                if (string.Empty.Equals(strHashbyteDeformatter) || string.Empty.Equals(strDeformatterData))
                    return false;
                byte[] HashbyteDeformatter;
                HashbyteDeformatter = Convert.FromBase64String(strHashbyteDeformatter);
                System.Security.Cryptography.RSACryptoServiceProvider RSA = new System.Security.Cryptography.RSACryptoServiceProvider();
                RSA.FromXmlString(strKeyPublic);

                if (RSA.VerifyData(Encoding.UTF8.GetBytes(strDeformatterData), "MD5", HashbyteDeformatter))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
        #endregion
        #endregion

        /// <summary>
        /// RSA私钥格式转换，java->.net
        /// </summary>
        /// <param name="privateKeyInfoData">java生成的RSA私钥</param>
        /// <returns></returns>
        public static string RSAPrivateKeyJava2DotNet(string privateKeyInfoData)
        {
            RsaPrivateCrtKeyParameters privateKeyParam = (RsaPrivateCrtKeyParameters)PrivateKeyFactory.CreateKey(Convert.FromBase64String(privateKeyInfoData));

            return string.Format("<RSAKeyValue><Modulus>{0}</Modulus><Exponent>{1}</Exponent><P>{2}</P><Q>{3}</Q><DP>{4}</DP><DQ>{5}</DQ><InverseQ>{6}</InverseQ><D>{7}</D></RSAKeyValue>",
                Convert.ToBase64String(privateKeyParam.Modulus.ToByteArrayUnsigned()),
                Convert.ToBase64String(privateKeyParam.PublicExponent.ToByteArrayUnsigned()),
                Convert.ToBase64String(privateKeyParam.P.ToByteArrayUnsigned()),
                Convert.ToBase64String(privateKeyParam.Q.ToByteArrayUnsigned()),
                Convert.ToBase64String(privateKeyParam.DP.ToByteArrayUnsigned()),
                Convert.ToBase64String(privateKeyParam.DQ.ToByteArrayUnsigned()),
                Convert.ToBase64String(privateKeyParam.QInv.ToByteArrayUnsigned()),
                Convert.ToBase64String(privateKeyParam.Exponent.ToByteArrayUnsigned()));
        }

        /// <summary>   
        /// RSA私钥格式转换，.net->java   
        /// </summary>   
        /// <param name="privateKey">.net生成的私钥</param>   
        /// <returns></returns>   
        public static string RSAPrivateKeyDotNet2Java(string privateKey)   
    {   
        XmlDocument doc = new XmlDocument();   
        doc.LoadXml(privateKey);   
        BigInteger m = new BigInteger(1, Convert.FromBase64String(doc.DocumentElement.GetElementsByTagName("Modulus")[0].InnerText));   
        BigInteger exp = new BigInteger(1, Convert.FromBase64String(doc.DocumentElement.GetElementsByTagName("Exponent")[0].InnerText));   
        BigInteger d = new BigInteger(1, Convert.FromBase64String(doc.DocumentElement.GetElementsByTagName("D")[0].InnerText));   
        BigInteger p = new BigInteger(1, Convert.FromBase64String(doc.DocumentElement.GetElementsByTagName("P")[0].InnerText));   
        BigInteger q = new BigInteger(1, Convert.FromBase64String(doc.DocumentElement.GetElementsByTagName("Q")[0].InnerText));   
        BigInteger dp = new BigInteger(1, Convert.FromBase64String(doc.DocumentElement.GetElementsByTagName("DP")[0].InnerText));   
        BigInteger dq = new BigInteger(1, Convert.FromBase64String(doc.DocumentElement.GetElementsByTagName("DQ")[0].InnerText));   
        BigInteger qinv = new BigInteger(1, Convert.FromBase64String(doc.DocumentElement.GetElementsByTagName("InverseQ")[0].InnerText));   
     
        RsaPrivateCrtKeyParameters privateKeyParam = new RsaPrivateCrtKeyParameters(m, exp, d, p, q, dp, dq, qinv);   
     
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.CreatePrivateKeyInfo(privateKeyParam);   
        byte[] serializedPrivateBytes = privateKeyInfo.ToAsn1Object().GetEncoded();   
        return Convert.ToBase64String(serializedPrivateBytes);   
    }   
     
    /// <summary>   
    /// RSA公钥格式转换，java->.net   
    /// </summary>   
    /// <param name="publicKey">java生成的公钥</param>   
    /// <returns></returns>   
    public static string RSAPublicKeyJava2DotNet(string publicKey)   
    {   
        RsaKeyParameters publicKeyParam = (RsaKeyParameters)PublicKeyFactory.CreateKey(Convert.FromBase64String(publicKey));   
        return string.Format("<RSAKeyValue><Modulus>{0}</Modulus><Exponent>{1}</Exponent></RSAKeyValue>",   
            Convert.ToBase64String(publicKeyParam.Modulus.ToByteArrayUnsigned()),   
            Convert.ToBase64String(publicKeyParam.Exponent.ToByteArrayUnsigned()));   
    }   
     
    /// <summary>   
    /// RSA公钥格式转换，.net->java   
    /// </summary>   
    /// <param name="publicKey">.net生成的公钥</param>   
    /// <returns></returns>   
    public static string RSAPublicKeyDotNet2Java(string publicKey)   
    {   
        XmlDocument doc = new XmlDocument();   
        doc.LoadXml(publicKey);   
        BigInteger m = new BigInteger(1, Convert.FromBase64String(doc.DocumentElement.GetElementsByTagName("Modulus")[0].InnerText));   
        BigInteger p = new BigInteger(1, Convert.FromBase64String(doc.DocumentElement.GetElementsByTagName("Exponent")[0].InnerText));   
        RsaKeyParameters pub = new RsaKeyParameters(false, m, p);   
     
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfoFactory.CreateSubjectPublicKeyInfo(pub);   
        byte[] serializedPublicBytes = publicKeyInfo.ToAsn1Object().GetDerEncoded();   
        return Convert.ToBase64String(serializedPublicBytes);   
    }   
    }
}