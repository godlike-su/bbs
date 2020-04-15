package my.app.util;

import my.app.db.User;
import my.app.db.UserAbility;
import my.app.support.MyBatis;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpSession;

/* 用户登录相关的工具集
 *
 */
public class UserLoginUtil
{
    //登陆以后，设置Session
    public static void login(HttpSession session, User user) throws Exception
    {
        session.setAttribute("user", user);

        // 获取user_ability
        //"select * from user_ability where userId=" + user.id;
        UserAbility userAbility = null;
        try(SqlSession sql = MyBatis.factory.openSession()){
            userAbility = sql.selectOne("userAbility.selectOne", user.id);
        }
        //设置 userAbility
        session.setAttribute("userAbility", userAbility);
    }

    //退出登录
    public static void logout(HttpSession session)
    {
        session.removeAttribute("user");
        session.removeAttribute("userAbility");
    }
}
