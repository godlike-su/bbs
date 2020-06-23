package my.app.mapper;

import my.app.entity.UserAbility;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UserAbilityMapper
{
    //查看一个用户权力 id
    UserAbility selectOne(Integer id);

    //用户权力-1
    int useCount(UserAbility userAbility);

    //插入用户权力
    int insertInit(UserAbility userAbility);

    //权力恢复(定时功能)
    int RestoreTask(Map map);

    //改变用户权力
    int setAbilityMax(UserAbility userAbility);


}
