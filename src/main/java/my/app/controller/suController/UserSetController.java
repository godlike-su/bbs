package my.app.controller.suController;

import af.spring.AfRestData;
import af.spring.AfRestError;
import com.alibaba.fastjson.JSONObject;

import my.app.entity.User;
import my.app.service.ThumbService;
import my.app.service.TmpFileService;
import my.app.service.UserService;
import my.app.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员设置用户操作
 */
@Controller
@RequestMapping("/u")
public class UserSetController
{
    @Autowired
    private UserService userService;

    @Autowired
    private ThumbService thumbService;

    @Autowired
    private TmpFileService tmpFileService;

    //管理员设置用户值
    @PostMapping("/admin/setUser.do")
    public Object setUser(@RequestBody JSONObject jreq,
                          @SessionAttribute User user) throws Exception
    {
        if(!user.isAdmin)
            throw new Exception("您不是管理员！");

        String cmd = jreq.getString("cmd").trim();
        Integer id = jreq.getInteger("id");
        String name = jreq.getString("name").trim();
        String value = jreq.getString("value").trim();

        if(cmd.equals("setPassword"))
        {
            User u = userService.selectUser(id);
            if(u==null || u.isAdmin==null || u.isAdmin)
                return new AfRestError("管理员用户密码不得更改");
            value = MyUtil.md5(value);
            userService.setPassword(id, value);
        }
        else
        {
            userService.setUserPro(id, name, cmd, value);
        }

        return new AfRestData("");
    }

    //管理员设置用户头像
    @PostMapping("/admin/setPhoto.do")
    public Object setPhoto(@SessionAttribute User user,
                           HttpServletRequest request) throws Exception
    {


        MultipartHttpServletRequest mhr = (MultipartHttpServletRequest) request;

        // 获取表单参数
//		String argValue = mhr.getParameter("your_arg_name");
//        int id = Integer.valueOf(mhr.getParameter("id"));
        MultipartFile mf = mhr.getFile("file"); // 表单里的 name='file'
        int id = Integer.valueOf(mhr.getParameter("id"));
        Map<String, Object> result = new HashMap<>();

        if(mf!=null && !mf.isEmpty())
        {
            String realName = mf.getOriginalFilename();
            String suffix = MyUtil.getSuffix(realName);
            String tmpName = MyUtil.guid2() + suffix;

            File tmpFile = tmpFileService.store.getFile(tmpName);

            //接收上传...
            mf.transferTo(tmpFile);

            if(tmpFile.length()> 500* 1000)
                throw new Exception("图片太大!需小于500KB!");

            // 头像的正式URL
            User u = userService.selectUser(id);
            if(u==null)
                return new AfRestError("没有该用户id：" + id);

            String url = thumbService.usePhoto(request, u, tmpFile);
            //判断，如果是换自己的头像，则为session中的用户信息也要更改
            if(id == user.id)
                user.thumb = url;

            // 回应给客户端的消息
            result.put("realName", realName);
            result.put("tmpName", tmpName);
            result.put("tmpUrl", url);
            result.put("url", url);
            result.put("isOwn", id==user.id);
        }
        return new AfRestData(result);
    }
}
