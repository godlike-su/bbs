package my.app.service.impl;

import my.app.entity.Message;
import my.app.mapper.MessageMapper;
import my.app.service.MesgReplyService;
import my.app.util.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MesgReplyServiceImpl implements MesgReplyService
{
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Message selectMessageOne(Long msgId)
    {
        return messageMapper.selectOne(msgId);
    }

    @Override
    public Map selectReplyMessageOne(Long msgId, HttpServletRequest request)
    {
        Map<String, Object> ref = messageMapper.replyOne(msgId);
        // 将img1,img2,img3转成可以显示的URL
        handleImageUrl(ref, "img1", request);
        handleImageUrl(ref, "img2", request);
        handleImageUrl(ref, "img3", request);
        return ref;
    }
    // 处理 'img1', 'img2', 'img3'字段，转成显示用的URL
    public void handleImageUrl(Map msg, String column, HttpServletRequest request)
    {
        String url = "";
        //获取帖子中的 img?字段
        String imgName = (String)msg.get(column);
        if(imgName != null && imgName.length() >0)
        {
            //获取帖子中的 storePath 图片存储目录
            String storePath = (String) msg.get("storePath");
            String path = storePath + imgName;

            FileStore store = new FileStore() ;
            //获取相对路径
            url = store.getUrl(path);
        }
        //更改为相对路径
        msg.put(column, url);
    }

    @Override
    public int replyMessageOne(Message message)
    {
        messageMapper.insertMes(message);
        // 更新统计数据
        Map<String, Object> map = new HashMap<>();
        //父级帖子的id
        map.put("msgId", message.ref1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 回复数+1，并且记录最后一次回复数据
        map.put("replyUser", message.creator);  //回复用户id
        map.put("replyText", message.title);     //回复文本
        map.put("replyUser", message.replyUser);       // 最新的回复用户名
        map.put("replyName", message.replyName);       // 最新的回复用户名

        map.put("replyTime", sdf.format(new Date()));   //回复时间
        messageMapper.updateOne(map);

        return 1;
    }

    @Override
    public List<Map> replyMessageList(Map map)
    {
        return messageMapper.replyList(map);
    }
}
