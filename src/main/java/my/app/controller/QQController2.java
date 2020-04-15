package my.app.controller;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import my.app.db.User;
import my.app.db.UserAbility;
import my.app.support.MyBatis;
import my.app.util.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/** 本项目的主要服务接口都在这里
 * 
 */
@Controller
public class QQController2
{
	// TODO: 把以下三个参数的值替换成你的应用参数，在你的QQ应用管理界面里拷贝粘贴过来

	/**
	 * 请求QQ登录
	 */
	@GetMapping("/loginByQQ")
	public void loginByQQ(HttpServletRequest request, 
							HttpServletResponse response,
							HttpSession session) {
		response.setContentType("text/html;charset=utf-8");
		try {
			String redirect = new Oauth().getAuthorizeURL(request);
			// 获取state，用于鉴别回调的安全性
			String qqState = parseAccessToken(redirect, "state");
			System.out.println(qqState);
			session.setAttribute("qqState", qqState);
			response.sendRedirect(redirect);
			
			System.out.println("请求QQ登录,开始跳转...");
		} catch (QQConnectException | IOException e) {
			e.printStackTrace();
		}
	}

	/*
	map.put("qqid", openID);    //查询是否有该用户，有则直接登录，没有则注册一个再登录
	user = sql.selectOne("user.selectAll", map);    //查询是否存在改用户
	id = sql.insert("user.insertUser", user);       //注册用户，并返回主键
    map.put("thumb", pathFile);       //插入头像路径
    sql.update("user.updateOne", map);      //修改头像
    sql.insert("userAbility.insertInit", userAbility);      //注册用户权限
	 */
	/**
	 * QQ登录回调地址
	 * Map函数首先存入
	 * @return
	 */
	@GetMapping("/qqLogin")
	public String connection(HttpServletRequest request,
							 HttpServletResponse response,
							 Map<String,Object> map,
							 HttpSession session,
							 Model model,
							 String state)  throws Exception
	{
		/* 校验此次访问是否合法 */
		String qqState = (String) session.getAttribute("qqState");
		if(! state.equals(qqState))
		{
			throw new Exception("访问已经失效，请重新登录");
//			return "redirect:/qq/loign";
		}		
		session.removeAttribute("qqState");
		
		try
        {
            //获取qq登录传回来的信息
			AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
			String accessToken = null, openID = null;
			long tokenExpireIn = 0L;
			if ("".equals(accessTokenObj.getAccessToken()))
			{
				System.out.println("登录失败:没有获取到响应参数");
//				return "accessTokenObj=>" + accessTokenObj + "; accessToken" + accessTokenObj.getAccessToken();
                return "user/login";
			}
			else
            {
                accessToken = accessTokenObj.getAccessToken();
                tokenExpireIn = accessTokenObj.getExpireIn();
                System.out.println("accessToken：" + accessToken);
                System.out.println("tokenExpireIn：" + tokenExpireIn);
                request.getSession().setAttribute("demo_access_token", accessToken);
                request.getSession().setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));

                // 利用获取到的accessToken 去获取当前用的openid -------- start
                OpenID openIDObj = new OpenID(accessToken);
                openID = openIDObj.getUserOpenID();
                System.out.println("qqId：" + openID);

                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                if (userInfoBean.getRet() == 0)
                {
                    //获取用户名
                    String name = removeNonBmpUnicode(userInfoBean.getNickname());
                    //获取头像地址
                    String imgUrl = userInfoBean.getAvatar().getAvatarURL100();
                    //获取性别
                    String sex = userInfoBean.getGender();
                    System.out.println("imgUrl：" + imgUrl);

                    //检测是否有改qqid
                    User user = new User();
                    map.put("qqid", openID);
                    try (SqlSession sql = MyBatis.factory.openSession())
                    {
                        user = sql.selectOne("user.selectAll", map);
                    }
                    //若存在，则加入session然后跳转
                    if (user != null)
                    {
                        UserLoginUtil.login(session, user);
                        return "forward:/message/list";
                    }
//                    System.out.println("图片：" + imgUrl);
                    user = new User();
                    System.out.println(name);
                    user.name = name;
                    user.password = "123456";
                    user.password = MyUtil.md5(user.password);
                    user.sex = sex.equals("男") ? false : true;
                    user.vip = (byte) 0;
                    user.vipName = "";
                    user.isAdmin = false;
                    user.experience = 0;
                    user.level = 0;
                    user.isAdmin = false;
                    user.vip = 0;
                    user.vipName = "非会员";
                    user.qqid = openID;
                    user.qqName = "qq" + name;
                    user.qqFlag = true;
                    user.timeCreate = new Date();
                    user.timeUpdate = new Date();
                    user.timeLogin = new Date();
                    int id = 0;
                    System.out.println("创建qq数据......");
                    try (SqlSession sql = MyBatis.factory.openSession())
                    {
                        id = sql.insert("user.insertUser", user);
                        sql.commit();
                    }
                    System.out.println("创建qq数据成功......");

                    //下载图片到本地
                    String path = String.format("%03d/%d_%s.jpg",
                            id / 1000, id, MyUtil.guid2());

                    //保存qq头像路径 /bbsfile/photo
                    String urlPrefix = "/bbsfile/photo/";
                    String rootDir = request.getServletContext().getRealPath(urlPrefix);
                    FileStore store = new FileStore(rootDir, urlPrefix);
                    //
                    File dstFile = store.getFile(path);
                    dstFile.getParentFile().mkdirs(); // 创建层次目录
                    //从网页下载图片方法。放入网页图片地址， 与保存地址
                    HTTP http = new HTTP();
                    http.download(imgUrl, store.getFile(""), path, 10000000);
                    //获取相对地址，用于html显示的路径
                    String pathFile = store.getUrl(path);

                    map.put("thumb", pathFile);
                    //暂不启用
//                    map.put("qq", "qq" + id);
                    //修改头像
                    try (SqlSession sql = MyBatis.factory.openSession())
                    {
                        sql.update("user.updateOne", map);
                        sql.commit();
                    }
    //						map.put("imgUrl", imgUrl);
                    //user_ability
                    UserAbility userAbility = new UserAbility();
                    UserAbilityUtil.init(userAbility, user.id);
                    try(SqlSession sql = MyBatis.factory.openSession()){
                        sql.insert("userAbility.insertInit", userAbility);
                        sql.commit();
                    }

                    user.thumb = pathFile;
                    session.setAttribute("user", user);
                    session.setAttribute("userAbility",userAbility);
                    return "forward:/message/list";
                }
                else
                {
                    System.out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
                }

            }
		} catch (QQConnectException e) {
			e.printStackTrace();
		}
		return "user/login";
	}

	/**
	 * 处理掉QQ网名中的特殊表情
	 * @param str
	 * @return
	 */
	public String removeNonBmpUnicode(String str) {
		if (str == null) {
			return null;
		}
		str = str.replaceAll("[^\\u0000-\\uFFFF]", "");
		if ("".equals(str)) {
			str = "(* _ *)";
		}
		return str;
	}
	
	// 从应答中提取 state 的值
	private String parseAccessToken(String reply, String parameter)
	{
		String[] sss = reply.split("&");
		for(String s: sss)
		{
			String[] aa = s.split("=");
			if(aa[0].equals(parameter))
			{
				return aa[1];
			}
		}
		return null;
	}

}
