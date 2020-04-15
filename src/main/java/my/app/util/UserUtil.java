package my.app.util;

import my.app.db.User;
import my.app.support.MyBatis;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class UserUtil
{
    //修改用户名
    public static void setName(String tmpName, String name, Integer id) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("id", id);
        int i = 0;
        try(SqlSession sql = MyBatis.factory.openSession()){
            i = sql.update("user.updateOne", map);
            sql.commit();
        }
        if(i<=0)
        {
            throw new Exception("修改用户名失败");
        }
        map.remove("name", name);
        map.remove("id", id);

        //原来的用户名
        map.put("tmp", tmpName);
        map.put("replyName", name);
        map.put("replyUser", id);
        //用户名修改了，帖子的replyName与refstr也要修改
        try(SqlSession sql = MyBatis.factory.openSession()){
            int j = sql.update("message.setUser", map);
            sql.commit();
        }
        map.remove("replyName", name);
        map.remove("replyUser");

        map.put("refstr", name);
        map.put("refId", id);
        try(SqlSession sql = MyBatis.factory.openSession()){
            int j = sql.update("message.setUser", map);
            sql.commit();
        }
    }

    //修改性别
    public static void setSex(Integer id, Boolean sex) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("sex", sex);
        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("user.updateOne", map);
            sql.commit();
        }
    }

    //修改经验
    /*
    map.put("id", user.id);     //用户 id
    map.put("experience", experience);  //用户 experience
    map.put("level", 1);        //用户 level
     */
    public static void setExperience(User user, int experience) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        experience = user.experience + experience;
        map.put("id", user.id);
        map.put("experience", experience);
        int level = 0;
        if(experience > 10 && experience <=100)
            level = 1;
        else if(experience > 100 && experience <=1000)
            level = 2;
        else if(experience > 1000 && experience <=1000)
            level = 3;
        else if(experience > 1000 && experience <=10000)
            level = 4;
        else if(experience > 10000 && experience <=100000)
            level = 5;
        else if(experience > 100000)
            level = 6;
        map.put("level", level);

        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("user.updateOne", map);
            sql.commit();
        }

        //更新一下
        user.experience = experience;
        user.level = level;
    }

    //修改密码
    public static void setPassword(Integer id, String password) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("password", password);
        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("user.updateOne", map);
            sql.commit();
        }
    }

    //修改vip
    public static void setVip(Integer id, Integer vip) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("vip", vip);
        if(vip <= 0)
        {
            map.put("vipName", "非会员");
        }
        else {
            map.put("vipName", "VIP" + vip);
        }
        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("user.updateOne", map);
            sql.commit();
        }

        map.put("userId", id);
        int msgMax = (vip+1) * 3;
        int imageMax = (vip+1) * 5;
        int replyMax = (vip+1) * 20;
        map.put("msgMax", msgMax);
        map.put("imageMax", imageMax);
        map.put("replyMax", replyMax);
        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("userAbility.setAbilityMax", map);
            sql.commit();
        }
    }

    //修改vipName
    public static void setvipName(Integer id, String vipName) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("vipName", vipName);
        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("user.updateOne", map);
            sql.commit();
        }
    }

}
