package my.app.controller;

import af.spring.AfRestData;
import af.spring.AfRestError;
import com.alibaba.fastjson.JSONObject;
import my.app.entity.User;
import my.app.entity.UserAbility;
import my.app.service.UserService;
import my.app.util.MyUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息主页
 */
@Controller
@RequestMapping("/u/profile")
public class UserProfileController
{
    @Autowired
    private UserService userService;

    @GetMapping("/edit")
    public String edit(@SessionAttribute User user,
                       @SessionAttribute UserAbility userAbility,
                       Model model) throws Exception
    {
        model.addAttribute("user", user);
        model.addAttribute("userAbility", userAbility);
        return "user/profile";
    }

    //修改密码
    @PostMapping("/password.do")
    public Object password(@SessionAttribute User user,
                           HttpSession session,
                           Model model,
                           @RequestBody JSONObject jreq) throws Exception
    {
        //从body中获取密码
        String password = (String) jreq.get("password");
        //进行md5转码
        password = MyUtil.md5(password);

        //判断密码是否与原密码相同，不相同则返回错误
        if(!password.equals(user.password))
        {
            return new AfRestError("原密码错误");
        }
        String password2 = jreq.getString("password2");
        password2 = MyUtil.md5(password2);
        userService.setPassword(user.id, password2);

//        UserLoginUtil.logout(session);

        //return "redirect:/";
        return new AfRestData("");
    }

    //修改用户
    @PostMapping("/setUser.do")
    public Object setUser(@SessionAttribute User user,
                              @RequestBody JSONObject jreq) throws Exception
    {
        String cmd = jreq.getString("cmd").trim();
        String value = jreq.getString("value").trim();

        if(cmd.equals("setName"))
        {
            userService.setName(user.name, value, user.id);
            user.name = value;
        }
        else if(cmd.equals("setSex"))
        {
            boolean sex = value.equals("1");
            userService.setSex(user.id, sex);
            user.sex = sex;
        }

        return new AfRestData("");
    }

}
