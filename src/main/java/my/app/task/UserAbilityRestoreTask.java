package my.app.task;

import my.app.service.UserAbilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAbilityRestoreTask implements Task
{
    @Autowired()
    private UserAbilityService userAbilityService;

    @Override
    public void execute() throws Exception
    {
        //"update user_ability set  imageCount=imageMax , msgCount=msgMax, replyCount=replyMax "
        userAbilityService.RestoreTask();
    }
}
