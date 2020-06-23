package my.app.service;

import my.app.util.FileStore;

/**
 * 临时文件保存路径
 */
public interface TmpFileService
{
    //临时文件路径
    // 临时图片的存储位置
    public static FileStore store
            = new FileStore("c:/bbsfile/tmp/", "/bbsfile/tmp/");
}
