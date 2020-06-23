package my.app.service;

import my.app.entity.User;
import my.app.entity.UserAbility;

import javax.servlet.http.HttpSession;

/**
 * 登录+退出的session操作
 */
public interface UserLoginService
{
    //登陆以后，设置Session
    void login(HttpSession session, User user) throws Exception;

    //登陆以后，设置Session
    void login(HttpSession session, User user, UserAbility userAbility) throws Exception;

    //退出登录
    void logout(HttpSession session);
}
