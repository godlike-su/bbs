package my.app.service;

import my.app.entity.UserAbility;

/**
 * 用户权力工具
 */
public interface UserAbilityService
{
    //权利初始化
    public void init(UserAbility row, int userId);

    //图片-1
    public void useImageCount(UserAbility row)throws Exception;

    //发帖-1
    /*
    map.put("userId", row.userId);
    map.put("msgCount", row.msgCount);
    sql.update("userAbility.useCount", map);
     */
    public void useMsgCount(UserAbility row)throws Exception;

    //回复-1
     /*
     map.put("userId", row.userId);
    map.put("replyCount", row.replyCount);
    sql.update("userAbility.useCount", map);
     */
    public void useReplyCount(UserAbility row)throws Exception;

    //恢复用户权力
    int RestoreTask() throws Exception;

    //注册
    int insertUserAbility(UserAbility userAbility)throws Exception;

    //查询用户权力
    UserAbility selectAbilityOne(int id);
}
