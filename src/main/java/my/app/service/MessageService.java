package my.app.service;

import my.app.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public interface MessageService
{
    //插入一条帖子
    int insertMes(Message message);

    /**
     * 查看符合条件的帖子总数
     * ref1 主帖  delFlag删除标识
     * filter筛选器 niceFlag精华帖
     * @param map
     * @return
     */
    int selectMessageCount(Map map);

    /**
     * 查询符合条件的帖子，并处理好，显示在主页（非帖子详细版）
     * 查看符合条件的帖子总数
     * ref1 主帖  delFlag删除标识
     * filter筛选器 niceFlag精华帖
     * startIndex pageSize
     */
    List<Map> homeMessageList(Map map);

    //删除帖子，将delFlag改为1
    int deleteMessage(Map map);

    //管理员修改帖子状态，传入帖子id，与需要修改的状态
    int rootUpdateMessageState(long msgId, String cmd);

}
