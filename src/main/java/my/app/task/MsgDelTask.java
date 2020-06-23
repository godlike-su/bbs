package my.app.task;

import my.app.entity.Message;
import my.app.service.MsgDelTaskService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/** 清理 message.delFlag=1 的记录
 *
 *
 */
@Component
public class MsgDelTask implements Task
{
    @Autowired
    private MsgDelTaskService msgDelTaskService;

    @Override
    public void execute() throws Exception
    {
        System.out.println("** 清理帖子(delFlag=1) ...");
        //"select id, ref1, storePath from message where delFlag=1 LIMIT 5000"
        List<Message> msgList = msgDelTaskService.selDeltask();

        for(Message msg : msgList)
        {
            try{
                clearMessage( msg );
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void clearMessage(Message msg) throws Exception
    {
        // 删除附件照片目录
//        String urlPrefix = "bbsfile/message/";
//        rootDir = rootDir + urlPrefix;
//        FileStore store = new FileStore(rootDir, urlPrefix) ;
//        File storeDir = store.getFile(msg.storePath);
        //获取文件的绝对路径
        File storeDir = msgDelTaskService.store.getFile(msg.storePath);
        if(storeDir.exists())
        {
            try {
                FileUtils.deleteQuietly(storeDir);
            }catch(Exception e)
            {
            }
        }

        // 删除数据库记录
        msgDelTaskService.delref(msg.id);
    }
}
