package my.app.mapper;

import my.app.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/*
用户数据查询
 */
@Mapper
@Repository
public interface UserMapper{

    /*
    插入用户数据
     */
    int insertUser(User user);

    /*
    查找一条用户信息，登录检查
    id name password
     */
    User selectUser(Map map);

    /*
    修改用户账户
    name sex thumb password vip vipName experience level 随意一条
    id qqid 其中一个
     */
    int updateOne(User user);

    /*
    查看所有用户信息，或者查看单个用户信息+分页+like搜索
    id qqid filter startIndex+pageSize
     */
    List<User> selectAll(Map map);

    /*
    查看共有多少个用户，或者like搜索有多少符合条件的用户
    filter
     */
    int selectCount(Map map);

    //查看一个用户信息id
    User selectOne(int id);

    //查看一个用户信息qqid
    User selectOneQqid(String qqid);


}
