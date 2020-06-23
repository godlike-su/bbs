package my.app.service;

import my.app.entity.Message;
import my.app.util.FileStore;

import java.util.List;

public interface MsgDelTaskService
{
    //帖子图片保存路径
    public static FileStore store
            = new FileStore("c:/bbsfile/message/", "/bbsfile/message/");

    //查看message的delFlag=1的数据
    List<Message> selDeltask();

    //删除message为delFlag的帖子
    int delref(Long id);
}
