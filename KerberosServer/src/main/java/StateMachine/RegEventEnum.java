package StateMachine;

public enum RegEventEnum {
    // 连接
    CONNECT,
    // 开始登录
    BEGIN_TO_LOGIN,
    // 登录成功
    LOGIN_SUCCESS,
    // 登录失败
    LOGIN_FAILURE,
    // 注销登录
    LOGOUT;
}