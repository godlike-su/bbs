package my.app.controller;

import af.spring.AfRestData;
import my.app.entity.User;
import my.app.service.ThumbService;
import my.app.service.TmpFileService;
import my.app.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/** 负责用户头像的上传
 *
 *
 */

@Controller
public class UserPhotoController
{
	@Autowired
    private ThumbService thumbService;

    @Autowired
    private TmpFileService tmpFileService;

    @PostMapping("/u/profile/setPhoto.do")
    public Object setPhoto(HttpServletRequest request,
                           @SessionAttribute User user) throws Exception
    {
        MultipartHttpServletRequest mhr = (MultipartHttpServletRequest) request;

        // 获取表单参数
//		String argValue = mhr.getParameter("your_arg_name");

        MultipartFile mf = mhr.getFile("file"); // 表单里的 name='file'
        Map<String, Object> result = new HashMap<>();

        if(mf!=null && !mf.isEmpty())
        {
            String realName = mf.getOriginalFilename();
            String suffix = MyUtil.getSuffix(realName);
            String tmpName = MyUtil.guid2() + suffix;

            File tmpFile = tmpFileService.store.getFile(tmpName);

            //接收上传...
            mf.transferTo(tmpFile);
            System.out.println("** file: " + tmpFile.getAbsolutePath());

            if(tmpFile.length()> 500* 1000)
                throw new Exception("图片太大!需小于500KB!");

            // 头像的正式URL
            String url = thumbService.usePhoto(request, user, tmpFile);

            // 回应给客户端的消息
            result.put("realName", realName);
            result.put("tmpName", tmpName);
            result.put("url", url);
        }
        return new AfRestData(result);
    }



}
