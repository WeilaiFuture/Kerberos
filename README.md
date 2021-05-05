# 项目介绍
1.	本程序为基于 ***Kerberos*** 认证（***DES***加密）和 ***RSA***加密的 ***IM*** 分布式通讯工具。


2.	工作分为 ***7*** 个工作模块，分别为
 
	① RSA 加密，包括公私钥的生成，以及信息加密

	② DES 加密，通过约定秘钥加密信息

	③ Kerberos 认证 ，ClientA与AS通讯，获取TGT；clientA凭TGT向TGS通讯，获取session key 以及 用ClintB与AS Master Key加密的报文；clientA与ClientB通信同步session key

	④ 数字签名，通过RSA加密实现
 
	⑤ 网络通讯，网络发送数据报文

	⑥ 应用，IM通讯，https://www.jianshu.com/p/b777938a4c52

	⑦ 互发证书
 

3.	实现以下功能：

	工具：

	1）加密工具：使用DES与RSA两种加密方式，根据报文的不同需求，分别使用不同的加密方式。

	2）RSA加密中的证书：约定好证书格式后，每个设备启动时，都会生成自己的证书，同时我们认为这个证书在系统内，独一无二，绝对真实，绝对安全，因此，先通过明文通讯，使得所有User设备与KDC设备间得到对方的公钥，即证书互信。同时可以认为系统内的Client与Server间都有对方的公钥，传输后续DES加密用到的session key。

	Kerberos认证：

	3）Kerberos认证体系：当所有设备启动后，其中两台设备扮演TGS和AS的角色，一台设备扮演通讯工具中的Server，其他设备扮演Client；所有Client和Server根据自己在AS中的账号登录验证身份，AS和TGS为每一个Client与Server间提供认证服务（由Client发起），Client与Client间没有必要提供认证服务。

	4）在Kerberos认证之前：由于Kerberos本身无RSA加解密需求，但为了将该功能引入Kerberos中，我们将最开始认证中出现的作为明文，根据最初已经获得的证书信息，使用RSA加密，通过通信功能传输。

	5）通过上述顺序为4）---> 3）--->2）的逻辑，实现了Kerberos认证功能后，认为除了出现新的User设备，整个系统内无需再使用Kerberos认证，AS和TGS设备监听新设备，不执行后续逻辑功能。

	通讯工具：

	6)加好友：Client A向Server发送添加Client B为好友的请求，Server将此请求转发给Client B，Client B再将回复信息发送给Server，Server继续转发。

	7)加群聊：邀请加群，一个Client向多个Client发送邀请，方式同6）；申请入群，一个Client向拥有群的Client发送申请，方式同6）。

	8)删除好友、退出群聊：Client A向Server发送删除好友Client B的信息，Server更改二者数据库信息。

	9)聊天：Client A与Client B或多个Client的即时通讯，通过Server转发。

	10)搜索功能：提供Search界面，某个Client可以输入某些关键词，Server会根据内容在系统数据库中查找并将一定信息返回给Client。

	11)登录界面：每个用户输入账号密码，登录聊天程序

	12)注册，修改密码+拓展（忘记密码）：通过网页修改，Server修改Server数据库。

	13)聊天记录存储在本地log文件中。

# 程序功能流程图
![image](./image/total_flow_chart.png)

# 数据库
	rm-uf6t4cbyfz681x569.mysql.rds.aliyuncs.com  
    端口：3306
