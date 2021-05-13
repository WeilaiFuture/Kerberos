var nodemailer = require('nodemailer');

var transporter = nodemailer.createTransport({
  service: 'outlook',
  secureConnection: true,
  auth: {
    user: 'easychat2021@outlook.com',
    pass: 'Ghs20200503'
  }
});
var emailOptions = {
  from: '"EasyChat"<easychat2021@outlook.com>',
  to: '',
  subject: 'Your InVision Code',
  html: ""
};

exports.sendRVC = function(email){
  let rvc = getVC();
  emailOptions.to = email;
  emailOptions.html = "Verify your email address" +
    "Let’s make it official. To verify your email address," +
    "enter this code in your browser. " +
    rvc +
    "If you didn’t request a code, you can safely ignore this email.";
  transporter.sendMail(emailOptions, function(err, info){
    if(err){
      console.log("发送注册验证码失败！");
    }
  });
  return rvc;//返回发送的验证码
};

exports.sendSVC = function(email){
  let svc = getVC();
  emailOptions.to = email;
  emailOptions.html = "Verify your email address" +
    "Let’s make it official. To verify your email address," +
    "enter this code in your browser. " +
    svc +
    " If you didn’t request a code, you can safely ignore this email.";
  transporter.sendMail(emailOptions, function(err, info){
    if(err){
      console.log("发送注册验证码失败！");
    }
  });
  return svc;//返回发送的验证码
};

function getVC(){
  let vc = "";
  for(let i = 0;i<6;++i){
    vc += Math.floor(Math.random()*10);
  }
  return vc;
};
