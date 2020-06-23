package my.app.service.impl;

import my.app.entity.UserAbility;
import my.app.mapper.UserAbilityMapper;
import my.app.service.UserAbilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserAbilityUtilImpl implements UserAbilityService
{
    @Autowired
    private UserAbilityMapper userAbilityMapper;

    @Override
    public void init(UserAbility row, int userId)
    {
        row.userId = userId;

        row.banFlag = 0;
        row.banDate = null;

        // 每天10张图片，5个帖子，20条回复
        // 每天5张图片，3个帖子，20条回复
        row.imageCount = row.imageMax = 5;
        row.msgCount = row.msgMax = 3;
        row.replyCount = row.replyMax = 20;
    }

    @Override
    public void useImageCount(UserAbility row) throws Exception
    {
        row.imageCount -= 1;

        UserAbility userAbility=new UserAbility();
        userAbility.setUserId(row.userId);
        userAbility.setImageCount(row.imageCount);

        userAbilityMapper.useCount(userAbility);
    }

    @Override
    public void useMsgCount(UserAbility row) throws Exception
    {
        row.msgCount -= 1;

        UserAbility userAbility=new UserAbility();
        userAbility.setUserId(row.userId);
        userAbility.setImageCount(row.msgCount);

        userAbilityMapper.useCount(userAbility);
    }

    @Override
    public void useReplyCount(UserAbility row) throws Exception
    {
        row.replyCount -= 1;

        UserAbility userAbility=new UserAbility();
        userAbility.setUserId(row.userId);
        userAbility.setImageCount(row.replyCount);

        userAbilityMapper.useCount(userAbility);
    }

    @Override
    public int RestoreTask() throws Exception
    {
        Map map = new HashMap();
        return userAbilityMapper.RestoreTask(map);
    }

    @Override
    public int insertUserAbility(UserAbility userAbility) throws Exception
    {
        return userAbilityMapper.insertInit(userAbility);
    }

    @Override
    public UserAbility selectAbilityOne(int id)
    {
        return userAbilityMapper.selectOne(id);
    }
}
