package my.app.service.impl;

import my.app.entity.User;
import my.app.entity.UserAbility;
import my.app.mapper.UserAbilityMapper;
import my.app.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserLoginUtilImpl implements UserLoginService
{
    @Autowired
    private UserAbilityMapper userAbilityMapper;

    @Override
    public void login(HttpSession session, User user) throws Exception
    {
        session.setAttribute("user", user);

        // 获取user_ability
        //"select * from user_ability where userId=" + user.id;
        UserAbility userAbility = userAbilityMapper.selectOne(user.id);
        //设置 userAbility
        session.setAttribute("userAbility", userAbility);
    }

    @Override
    public void login(HttpSession session, User user, UserAbility userAbility) throws Exception
    {
        session.setAttribute("user", user);
        session.setAttribute("userAbility", userAbility);
    }

    @Override
    public void logout(HttpSession session)
    {
        session.removeAttribute("user");
        session.removeAttribute("userAbility");
    }
}
