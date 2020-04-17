package my.app.task;

import my.app.controller.MesgImgController;
import my.app.controller.MessageController;
import my.app.db.Message;
import my.app.support.MyBatis;
import my.app.util.FileStore;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 清理 message.delFlag=1 的记录
 *
 *
 */
public class MsgDelTask implements Task
{
    @Override
    public void execute() throws Exception
    {
        System.out.println("** 清理帖子(delFlag=1) ...");
        //"select id, ref1, storePath from message where delFlag=1 LIMIT 5000"
        List<Message> msgList = null;
        try(SqlSession sql = MyBatis.factory.openSession()){
            msgList = sql.selectList("message.selDeltask");
        }
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
        File storeDir = MessageController.store.getFile(msg.storePath);
        if(storeDir.exists())
        {
            try {
                FileUtils.deleteQuietly(storeDir);
            }catch(Exception e)
            {
            }
        }

        // 删除数据库记录
        Map<String, Object> map = new HashMap<>();
        map.put("id", msg.id);
        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.delete("message.delref", map);
            sql.commit();
        }
    }
}
