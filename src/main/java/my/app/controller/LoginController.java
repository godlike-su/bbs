package my.app.controller;

import af.spring.AfRestData;
import af.spring.AfRestError;
import com.alibaba.fastjson.JSONObject;
import my.app.db.User;
import my.app.db.UserAbility;
import my.app.support.MyBatis;
import my.app.util.MyUtil;
import my.app.util.UserAbilityUtil;
import my.app.util.UserLoginUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
用于用户登录的Handler
 */
@Controller
public class LoginController
{
    //注册界面
    @GetMapping("/register")
    public String register(Model model, HttpSession session)
    {
        return "user/register";
    }

    //注册用户
    //
    @PostMapping("/register.do")
    public Object register(HttpServletRequest request,
                           HttpServletResponse response,
                           HttpSession session,
                           @RequestBody User user) throws Exception
    {
//        user.id = user.id;
        user.name = user.name.trim();
        user.password = user.password.trim();
        user.sex = false;
        user.vip = (byte)0;
        user.vipName = "";
        user.isAdmin = false;
        user.experience = 0;
        user.level = 0;
        user.isAdmin = false;
        user.vip = 0;
        user.vipName = "非会员";
        user.timeCreate = new Date();
        user.timeUpdate = new Date();
        user.timeLogin = new Date();
        if(user.id==null)
        {
            return new AfRestError("账号不能为空！");
        }
        if(user.name.length() == 0)
        {
            return new AfRestError("昵称不能为空！");
        }
        if(user.password.length() > 0)
        {
            // 数据库里存储密码的MD5值
            user.password = MyUtil.md5(user.password);
        }
        else
        {
            return new AfRestError("密码不能为空！");
        }

        //首先查看是否有这个用户
        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.insert("user.insertUser", user);
            sql.commit();
        }catch(Exception e)
        {
            // id重复时抛出异常
            //com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
            return new AfRestError("用户名被占用或者输入格式不正确");
        }
        //user_ability 注册
        UserAbility userAbility = new UserAbility();
        UserAbilityUtil.init(userAbility, user.id);
        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.insert("userAbility.insertInit", userAbility);
            sql.commit();
        }

        return new AfRestData("");
    }

    //登录界面  returnUrl 保存有用户上一个界面链接，登录完成跳转回用户的上一个链接
    @GetMapping("/login")
    public String login(Model model, String returnUrl, HttpSession session)
    {
        if(returnUrl==null) returnUrl="";
        model.addAttribute("returnUrl", returnUrl);
        
        //验证码
        String stateStr = stateCode(session);
        model.addAttribute("stateStr", stateStr);

        return "user/login";
    }

    //登录
    //map函数存储 id,通过id查询数据库用户信息对比
    //map.put("id", id);
    @PostMapping("/login.do")
    public Object login(HttpServletRequest request,
                        HttpServletResponse response,
                        HttpSession session,
                        @RequestBody JSONObject jreq) throws Exception
    {
        Integer id = jreq.getInteger("id");
        String password = jreq.getString("password");
        
        //安全监测，验证码监测
        int state = jreq.getInteger("state");
        if(state!=(int)session.getAttribute("stateCode"))
        {
            return new AfRestError("验证码错误或超时，请刷新验证码");
        }
        session.removeAttribute("stateCode");

        User user = null;
        try(SqlSession sqlsession = MyBatis.factory.openSession()){
            Map<String, Object> map = new HashMap<>();
            //通过id来登录
            map.put("id", id);
            //密码是通过md5转码后存储
            password = MyUtil.md5(password);
            user = sqlsession.selectOne("user.selectUser", map);
        }
        //如果账号为空
        if(user == null )
        {
            return new AfRestError("账号不存在");
        }
        //如果密码错误
        if(!password.equals(user.password))
        {
            return new AfRestError("密码错误");
        }

        //设置当前会话
        UserLoginUtil.login(session, user);
        return new AfRestData("");
    }

    //退出当前登录
    @GetMapping("/logout")
    public String logout(HttpSession session,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception
    {
        UserLoginUtil.logout(session);

        //return "redirect:/";
        return "redirect:/message/list";
    }
    
    //验证码
    @PostMapping("/loginState.do")
    public Object login(HttpSession session) throws Exception
    {
        //验证码
        String stateStr = stateCode(session);

        return new AfRestData(stateStr);
    }

    //登录验证码
	public static String stateCode(HttpSession session)
	{
	    Random rand = new Random();
	
	    String op1="";
	    int result=0;
	    int op = rand.nextInt(3);
	    int num1 = rand.nextInt(10);
	    int num2 = rand.nextInt(10);
	    switch (op)
	    {
	        case 0 : result=num1+num2;op1="+";break;
	        case 1 : result=num1-num2;op1="-";break;
	        case 2 : result=num1*num2;op1="*";break;
	    }
	    session.setAttribute("stateCode", result);
	    return String.valueOf(num1)+op1+String.valueOf(num2)+"=?";
	}
}
