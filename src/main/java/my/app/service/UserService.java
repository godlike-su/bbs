package my.app.service;

import my.app.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户信息更改
 */
public interface UserService
{
    //修改用户名
    void setName(String tmpName, String name, Integer id) throws Exception;

    //修改性别
    void setSex(Integer id, Boolean sex) throws Exception;

    //设置经验与等级
    void setExperience(User user, int experience) throws Exception;

    //修改密码
    void setPassword(Integer id, String password) throws Exception;

    //修改vip
    void setVip(Integer id, Integer vip) throws Exception;

    //修改vipName
    void setvipName(Integer id, String vipName) throws Exception;

    //设置除了密码、头像，id除外
    void setUserPro(Integer id, String name, String cmd, String value)throws Exception;

    //注册用户
    int insertUser(User user);

    //获取用户信息
    User selectUser(int id);

    //获取用户信息
    User selectUser(String qqid);

    //获取符合条件的用户数量
    int userCount(Map map);

    //获取符合条件的用户信息
    List<User> selectUserAll(Map map);
}
