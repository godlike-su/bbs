package my.app.service.impl;

import my.app.entity.User;
import my.app.entity.UserAbility;
import my.app.mapper.MessageMapper;
import my.app.mapper.UserAbilityMapper;
import my.app.mapper.UserMapper;
import my.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserUtilImpl implements UserService
{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserAbilityMapper userAbilityMapper;

    @Override
    public void setName(String tmpName, String name, Integer id) throws Exception
    {
        Map<String, Object> map = new HashMap<>();

        int i = 0;
        User user = new User();
        user.setId(id);
        user.setName(name);
        i = userMapper.updateOne(user);
        if(i<=0)
        {
            throw new Exception("修改用户名失败");
        }

        //原来的用户名
        map.put("tmp", tmpName);
        map.put("replyName", name);
        map.put("replyUser", id);
        //用户名修改了，帖子的replyName与refstr也要修改
        messageMapper.setUser(map);

        map.remove("replyName");
        map.remove("replyUser");

        map.put("refstr", name);
        map.put("refId", id);
        messageMapper.setUser(map);
    }

    @Override
    public void setSex(Integer id, Boolean sex) throws Exception
    {
        User user = new User();
        user.setId(id);
        user.setSex(sex);
        userMapper.updateOne(user);
    }

    @Override
    public void setExperience(User user, int experience) throws Exception
    {
        experience = user.experience + experience;

        user.setExperience(experience);
        int level = 0;
        if(experience >= 10 && experience <100)
            level = 1;
        else if(experience >= 100 && experience <1000)
            level = 2;
        else if(experience >= 1000 && experience <10000)
            level = 3;
        else if(experience >= 10000 && experience <100000)
            level = 4;
        else if(experience >= 100000 && experience <1000000)
            level = 5;
        else if(experience >= 1000000)
            level = 6;

        user.setLevel(level);
        User u = new User();
        u.setId(user.id);
        u.setExperience(experience);
        u.setLevel(level);
        userMapper.updateOne(u);
    }

    @Override
    public void setPassword(Integer id, String password) throws Exception
    {
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        userMapper.updateOne(user);
    }

    @Override
    public void setVip(Integer id, Integer vip) throws Exception
    {
        Map<String, Object> map = new HashMap<>();

        User user = new User();
        user.setId(id);
        user.setVip(vip.byteValue());
        if(vip <= 0)
        {
           user.setVipName("非会员");
        }
        else {
            user.setVipName("VIP" + vip);
        }
        userMapper.updateOne(user);

        UserAbility userAbility = new UserAbility();
        userAbility.setUserId(id);
        int msgMax = (vip+1) * 3;
        int imageMax = (vip+1) * 5;
        int replyMax = (vip+1) * 20;
        userAbility.setMsgMax(msgMax);
        userAbility.setImageMax(imageMax);
        userAbility.setReplyMax(replyMax);
        userAbilityMapper.setAbilityMax(userAbility);
    }

    @Override
    public void setvipName(Integer id, String vipName) throws Exception
    {
        User user = new User();
        user.setId(id);
        user.setVipName(vipName);
        userMapper.updateOne(user);
    }

    @Override
    public void setUserPro(Integer id, String name, String cmd, String value) throws Exception
    {
        if(cmd.equals("setName"))
        {
            Map<String, Object> map = new HashMap<>();

            int i = 0;
            User user = new User();
            user.setId(id);
            user.setName(value);
            i = userMapper.updateOne(user);
            if(i<=0)
            {
                throw new Exception("修改用户名失败");
            }
            //原来的用户名
            map.put("tmp", name);
            map.put("replyName", value);
            map.put("replyUser", id);
            //用户名修改了，帖子的replyName与refstr也要修改
            messageMapper.setUser(map);
            map.remove("replyName");
            map.remove("replyUser");

            map.put("refstr", name);
            map.put("refId", id);
            messageMapper.setUser(map);
        }
        else if(cmd.equals("setSex"))
        {
            boolean sex = value.equals("1");
            this.setSex(id, sex);
        }
        else if(cmd.equals("setVip"))
        {
            this.setVip(id, Integer.parseInt(value));
        }
        else if(cmd.equals("setvipName"))
        {
            this.setvipName(id, value);
        }
    }

    @Override
    public int insertUser(User user)
    {
        return userMapper.insertUser(user);
    }

    @Override
    public User selectUser(int id)
    {
        return userMapper.selectOne(id);
    }

    @Override
    public User selectUser(String qqid)
    {
        return userMapper.selectOneQqid(qqid);
    }

    @Override
    public int userCount(Map map)
    {
        return userMapper.selectCount(map);
    }

    @Override
    public List<User> selectUserAll(Map map)
    {
        return userMapper.selectAll(map);
    }
}
