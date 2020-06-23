package my.app.service;

import my.app.entity.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface MesgReplyService
{
    //查询一条帖子
    Message selectMessageOne(Long msgId);

    //查看reply原帖,并把图片转换成前端格式
    Map selectReplyMessageOne(Long msgId, HttpServletRequest request);

    // 插入一条回复并更新原帖统计数据
    int replyMessageOne(Message message);

    /**
     * 查询符合条件的回复(详细显示内容页)
     * ref1 delFlag startIndex pageSize
     * @param map
     * @return
     */
    List<Map> replyMessageList(Map map);
}
