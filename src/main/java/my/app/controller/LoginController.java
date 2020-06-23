package my.app.controller;

import af.spring.AfRestData;
import af.spring.AfRestError;
import com.alibaba.fastjson.JSONObject;
import my.app.entity.User;
import my.app.entity.UserAbility;
import my.app.service.UserAbilityService;
import my.app.service.UserLoginService;
import my.app.service.UserService;
import my.app.util.MyUtil;
import my.app.util.VerifyPng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/*
用于用户登录的Handler
 */
@Controller
public class LoginController
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserAbilityService userAbilityService;

    @Autowired
    private UserLoginService userLoginService;

    //注册界面
    @GetMapping("/register")
    public String register(Model model)
    {
        return "user/register";
    }

    //注册用户
    //
    @PostMapping("/register.do")
    public Object register(HttpServletRequest request,
                           HttpServletResponse response,
                           HttpSession session,
                           @RequestBody JSONObject jreq) throws Exception
    {
        //安全监测，验证码监测
        int state = jreq.getInteger("state");
        if(state!=(int)session.getAttribute("stateCode"))
        {
            return new AfRestError("验证码错误或超时，请刷新验证码");
        }
        session.removeAttribute("stateCode");
        session.removeAttribute("verifyCode");


        User user = jreq.getObject("user", User.class);
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

        //注册用户
        try
        {
            userService.insertUser(user);
        }catch(Exception e)
        {
            // id重复时抛出异常
            //com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
            return new AfRestError("用户名被占用或者输入格式不正确");
        }
        System.out.println(user.id);
        //user_ability 注册
        UserAbility userAbility = new UserAbility();
        userAbilityService.init(userAbility, user.id);
        userAbilityService.insertUserAbility(userAbility);

        return new AfRestData(user.id);
    }

    //注册成功界面
    @GetMapping("/prompt/{id}")
    public String prompt(Model model, @PathVariable int id)
    {
        model.addAttribute("id", id);
        return "user/prompt";
    }

    //登录界面  returnUrl 保存有用户上一个界面链接，登录完成跳转回用户的上一个链接
    @GetMapping("/login")
    public String login(Model model, String returnUrl, HttpSession session)
    {
        if(returnUrl==null) returnUrl="";
        model.addAttribute("returnUrl", returnUrl);
        
        return "user/login3";
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
        session.removeAttribute("verifyCode");

        User user = userService.selectUser(id);
        password = MyUtil.md5(password);
        if(user == null)
        {
            return new AfRestError("无此用户id: " + id);
        }

        //如果密码错误
        if(!password.equals(user.password))
        {
            return new AfRestError("密码错误");
        }

        //设置当前会话
        userLoginService.login(session, user);
        return new AfRestData("");
    }

    //退出当前登录
    @GetMapping("/logout")
    public String logout(HttpSession session,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception
    {
        userLoginService.logout(session);

        //return "redirect:/";
        return "redirect:/message/list";
    }
    
    //获取验证码
    @PostMapping("/loginState.do")
    public Object loginState(HttpSession session) throws Exception
    {
        //验证码
        String stateStr = MyUtil.opeStateCode(session);
        session.setAttribute("verifyCode", stateStr);
        // 生成PNG发给客户端
        return new AfRestData("");
    }
    
    //显示验证码
    @GetMapping("/show")
    public void show(HttpSession session,  HttpServletResponse response) throws Exception
    {
    	//验证码
        String verifyCode = (String) session.getAttribute("verifyCode");
        // 生成PNG发给客户端
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache");
        VerifyPng v = new VerifyPng();
        
        v.toPNG(verifyCode, response.getOutputStream());
    }
}
