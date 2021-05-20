using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static Kerberos_Client.MainWindow;

namespace Kerberos_Client
{
    class JsonHelper
    {
        private static readonly JsonSerializerSettings MyJsonSerializerSettings;

        static JsonHelper()
        {
            MyJsonSerializerSettings = new JsonSerializerSettings();
            IsoDateTimeConverter dateTimeConverter = new IsoDateTimeConverter();
            dateTimeConverter.DateTimeFormat = "yyyy-MM-dd HH:mm:ss";
            MyJsonSerializerSettings.Converters.Add(dateTimeConverter);
        }

        /// <summary>
        /// Json字符串转换为数据结构
        /// </summary>
        /// <param name="json>报文</param>
        /// <returns>返回对应数据结构 </returns>
        public static T FromJson<T>(string json)
        {
            if (string.IsNullOrEmpty(json))
            {
                return default(T);
            }
            return JsonConvert.DeserializeObject<T>(json, MyJsonSerializerSettings);
        }

        /// <summary>
        /// 将数据结构转换为Json字符串
        /// </summary>
        /// <param name="data>数据</param>
        /// <returns>返回Json字符串 </returns>
        public static string ToJson<T>(T data)
        {
            return JsonConvert.SerializeObject(data, MyJsonSerializerSettings);
        }
    }
    public class RealTimeDataMsg
    {
        [JsonProperty("messageType")]
        public int MessageType { get; set; }


        [JsonProperty("time")]
        public DateTime Time { get; set; }

    }
}
