package my.app.service;

import my.app.entity.User;
import my.app.util.FileStore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 头像操作+头像保存路径
 */
public interface ThumbService
{
    //用户头像路径
    public static FileStore store
            = new FileStore("c:/bbsfile/photo/", "/bbsfile/photo/");

    //上传用户头像
    public String usePhoto(HttpServletRequest request, User user, File tmpImage) throws Exception;
}
