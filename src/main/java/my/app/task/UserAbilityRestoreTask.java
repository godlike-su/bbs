package my.app.task;

import my.app.support.MyBatis;
import org.apache.ibatis.session.SqlSession;

public class UserAbilityRestoreTask implements Task
{

    @Override
    public void execute() throws Exception
    {
        //"update user_ability set  imageCount=imageMax , msgCount=msgMax, replyCount=replyMax "
        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("userAbility.RestoreTask");
            sql.commit();
        }

    }
}
