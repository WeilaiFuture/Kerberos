const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
const path = require('path');
const emailer = require('./emailer');
const db = require('./db');

var app = express();
var email_rvcs = new Map();
var uid_svcs = new Map();

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(express.static('./'));
app.all('*', function(req, res, next) {
	res.header("Access-Control-Allow-Origin", "*");
	res.header("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
	res.header("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
	res.header("X-Powered-By", ' 3.2.1')
	if (req.method == "OPTIONS") res.send(200); /*让options请求快速返回*/
	else next();
});
// 指定模板存放目录
app.set('views', './views');
// 指定模板引擎为 Handlebars
app.set('view engine', 'hbs');

app.get('/', (req, res)=>{
    res.sender('index');
});
app.post('/users/getrvc', (req, res)=>{//【获取注册验证码】
	let email = req.body.email;
	let rvc = emailer.sendRVC(email);
	console.log("0:" + email);
	console.log("1:" + rvc);
	email_rvcs.set(email, rvc);
	res.send();
});
app.post('/users/register', (req, res)=>{
	let info = req.body;
	let result = {
		uid: "",
		res: 1//result: 0(成功)，1(验证码错误),2(注册异常)。
	};
	if(!(email_rvcs.get(info.email) === info.rvc)){
		res.json(result);
	}
	else{
		result.uid = db.getNewUid();
		let user = {
			name: info.name,
			psswd: info.psswd,
			email: info.email,
			uid: result.uid
		}
		if(!db.addNewUser(user)){
			result.res = 2;
			res.json(result);
		}
		else{
			result.res = 0;
			res.json(result);
		}
	}
});
app.post('/users/getsvc', (req, res)=>{
	let uid = req.body.uid;
	console.log("uid:" + uid);
	let result = {
		email: "",
		res: 1, //0：成功；1：账号不存在。
	}
	let user = db.getUser(uid);
	if(user == null){
		res.json(result);
	}
	else{
		let email = user.email;
		let svc = emailer.sendSVC(email);
		uid_svcs.set(uid, svc);
		result.email = email;
		result.res = 0;
		res.json(result);
	}
});
app.post('/users/reset', (req, res)=>{
	let info = req.body;
	console.log("here");
	let result = {
		uid: "",
		res: 1//result: 0(成功)，1(验证码错误),2(注册异常)。
	};
	if(!(uid_svcs.get(info.uid) === info.svc)){
		res.json(result);
	}
	else{
		if(false){
			result.res = 2;
			res.json(result);
		}
		else{
			result.res = 0;
			res.json(result);
		}
	}
});
app.listen(8000, ()=>{
    console.log("服务已经启动了...");
});

function plusXing (str, frontLen, endLen) {
	var len = str.length-frontLen-endLen;
	var xing = '';
	for (var i=0;i<len;i++) {
		xing+='*';
	}
	return str.substring(0,frontLen)+xing+str.substring(str.length-endLen);
};
