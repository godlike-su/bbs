package my.app.mapper;

import my.app.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MessageMapper
{
    //插入帖子数据
    int insertMes(Message message);

    //查询可阅读帖子的数目
    int selectCount(Map map);

    //查看所有符合搜索要求的帖子
    List<Map> selectList(Map map);

    //查看回复
    List<Map> replyList(Map map);

    //查看回复帖子的主贴详细内容
    Map replyOne(Long msgId);

    //查看某一条主贴
    Message selectOne(Long msgId);

    //更新统计数据,根据id更新
    int updateOne(Map map);

    //用户删除的帖子
    Message selDetUs(int msgId);

    //修改，将delFlag=1
    int UpDel(Map map);

    //管理设置帖子置顶等操作
    int suSetFlags(Map map);

    //查看delFlag=1的帖子
    List<Message> selDeltask();

    //删除delFlag=1的帖子
    int delref(Long id);

    //更改userName后，需要修改的Message
    int setUser(Map map);

}
