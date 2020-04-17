package my.app.controller;

import af.spring.AfRestData;
import af.spring.AfRestError;
import my.app.db.User;
import my.app.util.FileStore;
import my.app.util.MyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MesgImgController
{
	// 临时图片的存储位置
	public static FileStore store 
		= new FileStore("c:/bbsfile/tmp/", "/bbsfile/tmp/");
	
    //上传临时图片
    @PostMapping("/u/message/imageUp.do")
    public Object upload(HttpServletRequest request,
                         @SessionAttribute User user) throws Exception
    {
        MultipartHttpServletRequest mhr = (MultipartHttpServletRequest) request;

        // 从表单里获取其他参数
//		String argValue = mhr.getParameter("your_arg_name");

        MultipartFile mf = mhr.getFile("file"); // 表单里的 name='file'
        Map<String, Object> result = new HashMap<>();

        if(mf!=null && !mf.isEmpty())
        {
            String realName = mf.getOriginalFilename();
            String suffix = MyUtil.getSuffix(realName);
            String tmpName = MyUtil.guid2() + suffix;

//            File tmpFile = TmpFile.getFile(request, tmpName);
            File tmpFile = store.getFile(tmpName);

            //接收上传
            mf.transferTo(tmpFile);
            System.out.println("** file: " + tmpFile.getAbsolutePath());

            if(tmpFile.length() > 1000000)
                throw new Exception("图片太大!需小于1M!");

            // 回应给客户端的消息
            result.put("realName", realName);
            result.put("tmpName", tmpName);
            result.put("tmpUrl", store.getUrl(tmpName));
        }
        return new AfRestData(result);
    }

}
