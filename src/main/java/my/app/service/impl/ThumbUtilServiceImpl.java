package my.app.service.impl;

import my.app.entity.User;
import my.app.mapper.UserMapper;
import my.app.service.ThumbService;
import my.app.util.MyUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class ThumbUtilServiceImpl implements ThumbService
{
    @Autowired
    private UserMapper userMapper;

    @Override
    public String usePhoto(HttpServletRequest request, User user, File tmpImage) throws Exception
    {
        // 示例： 000/1_15728601766396.jpg, 保存在数据库的图片路径
        String path = String.format("%03d/%d_%s.jpg",
                user.id/1000, user.id, MyUtil.guid2());

        //保存路径 /bbsfile/photo
//        String urlPrefix = "/bbsfile/photo/";
//        String rootDir = request.getServletContext().getRealPath(urlPrefix);
//        FileStore store = new FileStore(rootDir, urlPrefix);


        // 先删除旧的照片
        if(user.thumb!=null && user.thumb.length()>0)
        {
            File oldFile = store.fileOfUrl(user.thumb);
            try
            {
                FileUtils.deleteQuietly(oldFile);
            }catch(Exception e)
            {
                System.out.println("** 不能删除旧的头像: " + oldFile);
            }
        }

        // 保存新的头像图片
        File dstFile = store.getFile(path);
        dstFile.getParentFile().mkdirs(); // 创建层次目录

//        FileUtils.moveFile(tmpImage, dstFile); // 转移图片
        clipPhoto(tmpImage, dstFile); // 图片裁剪为正方形
        try
        {
            //删除缓存区里的图片
            tmpImage.delete();
        }catch (Exception e){}

        // 头像图片的正式URL
        String url = store.getUrl(path);
        user.thumb = url;
        //记录到user表
        User u = new User();
        u.setId(user.id);
        u.setThumb(user.thumb);
        userMapper.updateOne(u);

        //返回头像在前端显示的相对路径
        return request.getContextPath() + url;
    }

    // 加入 thumbnailator-0.4.8.jar，图像裁剪
    private static void clipPhoto (File srcFile, File dstFile) throws Exception
    {
        // 读取源图，缩小为100x100 (最大尺寸)
        BufferedImage img = Thumbnails.of(srcFile).size(100, 100).asBufferedImage();

        //截取中间的正方形
        int w = img.getWidth();
        int h = img.getHeight();
        int size = w < h ? w : h;   //取最小值
        int x = (w-size) / 2;
        int y = (h-size) / 2;

        //截取
        Thumbnails.of(img)
                .scale(1.0f)
                .sourceRegion(x,y,size,size)
                .outputFormat("jpg")
                .toFile( dstFile );
    }
}
