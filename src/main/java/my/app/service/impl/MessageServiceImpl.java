package my.app.service.impl;

import af.spring.AfRestError;
import my.app.entity.Message;
import my.app.mapper.MessageMapper;
import my.app.service.MessageService;
import my.app.util.TimeStrUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService
{
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public int insertMes(Message message)
    {
        return messageMapper.insertMes(message);
    }

    @Override
    public int selectMessageCount(Map map)
    {
        return messageMapper.selectCount(map);
    }

    @Override
    public List<Map> homeMessageList(Map map)
    {
        List<Map> messageList = messageMapper.selectList(map);

        // 需要进一步处理处理成前端需要的格式
        TimeStrUtil tts = new TimeStrUtil();
        for(Map m : messageList)
        {
            String timeCreate =  m.get("timeCreate").toString();
            m.put("timeCreate", tts.format(timeCreate));

            String replyTime = (String) m.get("replyTime").toString();
            m.put("replyTime", tts.format(replyTime));
        }
        return messageList;
    }

    @Override
    public int deleteMessage(Map map)
    {
        return messageMapper.UpDel(map);
    }

    @Override
    public int rootUpdateMessageState(long msgId, String cmd)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("msgId", msgId);

        // 设置 topFlag/niceFlag/delFlag 标识
        if(cmd.equals("top"))
            map.put("topFlag", 1);      //置顶
        else if(cmd.equals("nice"))
            map.put("niceFlag", 1);     //加精
        else if(cmd.equals("cantop"))
            map.put("topFlag", 0);      //取消置顶
        else if(cmd.equals("cannice"))
            map.put("niceFlag", 0);     //取消加精
        else if(cmd.equals("del"))
            map.put("delFlag", 1);      //删除

        int i = messageMapper.suSetFlags(map);
        if(i==0)
            return 0;

        // 如果是删除操作，还需要把这条帖子的回复都删除
        if(cmd.equals("del"))
        {
            map.put("ref1", msgId);
            this.deleteMessage(map);
//            messageMapper.UpDel(map);
        }
        return 1;
    }


}
