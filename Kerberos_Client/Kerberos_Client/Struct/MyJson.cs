using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Kerberos_Client
{
	//Json 数据结构字段
	public class Order
	{
        string msgId;   //报文唯一标识符
        string src;		//发送者ID
		string dst;		//接受者ID
		string msgType;	//消息类型
		string extend;	//拓展字段，报文字段
		bool statusReport;//状态字段
        string sTS;     //发送方时间戳
        private string rTS;     //接收方时间戳
        string contentType;//消息类型

		public string MsgId
		{
			get { return msgId; }
			set { msgId = value; }
		}

		public string Src
		{
			get { return src; }
			set { src = value; }
		}
		public string Dst
		{
			get { return dst; }
			set { dst = value; }
		}
		public string MsgType
		{
			get { return msgType; }
			set { msgType = value; }
		}
		public string Extend
		{
			get { return extend; }
			set { extend = value; }
		}
		public bool StatusReport
		{
			get { return statusReport; }
			set { statusReport = value; }
		}
		public string STS
		{
			get { return sTS; }
			set { sTS = value; }
		}

		public string RTS
		{
			get { return rTS; }
			set { rTS = value; }

		}
		public string ContentType
		{
			get { return contentType; }
			set { contentType = value; }

		}

	}
}
