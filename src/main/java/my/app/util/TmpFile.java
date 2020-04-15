package my.app.util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/** 临时文件的位置
 *
 *  上传上来的临时图片，直接就放在 webroot\tmp\目录，以便可以直接URL访问浏览
 *  springboot项目放弃该方式存储,已废弃
 *
 */
public class TmpFile
{
    // 临时文件目录 : WEBROOT\tmp\
    public static File getDir(HttpServletRequest request)
    {
        return new File(request.getServletContext().getRealPath("/tmp"));
    }

    //获取临时文件
    public static File getFile(HttpServletRequest request, String tmpName)
    {
        File dir = getDir(request);
        dir.mkdirs();
        return new File(dir, tmpName);
    }

    // 获取临时文件的访问URL
    public static String getUrl(HttpServletRequest request, String tmpName)
    {
        return request.getContextPath()
                + "/tmp/"
                + tmpName;
    }
}
