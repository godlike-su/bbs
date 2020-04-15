package my.app.util;

import my.app.db.User;
import my.app.db.UserAbility;
import my.app.support.MyBatis;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class UserAbilityUtil
{
    //权利初始化
    public static void init(UserAbility row, int userId)
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

    //图片-1
    /*
    map.put("userId", row.userId);      //userId
    map.put("imageCount", row.imageCount);     // imageCount
    sql.update("userAbility.useCount", map);
     */
    public static void useImageCount(UserAbility row)throws Exception
    {
        row.imageCount -= 1;

        Map<String, Object> map = new HashMap<>();
        map.put("userId", row.userId);
        map.put("imageCount", row.imageCount);

        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("userAbility.useCount", map);
            sql.commit();
        }
    }

    //发帖-1
    /*
    map.put("userId", row.userId);
    map.put("msgCount", row.msgCount);
    sql.update("userAbility.useCount", map);
     */
    public static void useMsgCount(UserAbility row)throws Exception
    {
        row.msgCount -= 1;

        Map<String, Object> map = new HashMap<>();
        map.put("userId", row.userId);
        map.put("msgCount", row.msgCount);

        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("userAbility.useCount", map);
            sql.commit();
        }
    }

    //回复-1
     /*
     map.put("userId", row.userId);
    map.put("replyCount", row.replyCount);
    sql.update("userAbility.useCount", map);
     */
    public static void useReplyCount(UserAbility row)throws Exception
    {
        row.replyCount -= 1;

        Map<String, Object> map = new HashMap<>();
        map.put("userId", row.userId);
        map.put("replyCount", row.replyCount);

        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("userAbility.useCount", map);
            sql.commit();
        }
    }

}
