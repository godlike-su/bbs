package my.app.service.impl;

import my.app.entity.Message;
import my.app.mapper.MessageMapper;
import my.app.service.MsgDelTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgDelTaskImpl implements MsgDelTaskService
{
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> selDeltask()
    {
        return messageMapper.selDeltask();
    }

    @Override
    public int delref(Long id)
    {
        return messageMapper.delref(id);
    }
}
