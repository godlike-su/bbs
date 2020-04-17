package my.app.task;

import org.apache.commons.io.FileUtils;

import my.app.controller.MesgImgController;

import java.io.File;

/** 清理 tmp 的文件
 *
 *
 */
public class TmpFileClearTask implements Task
{
    int EXPIRED = 1000 * 3600 * 3; // 3小时以上的临时文件
//    int EXPIRED = 0; // 测试使用

    @Override
    public void execute() throws Exception
    {
        File tmpDir = MesgImgController.store.getFile("");
        if(! tmpDir.exists()) return;
        File[] files = tmpDir.listFiles();
        if(files == null || files.length ==0)
            return;

        // 清理过期的文件
        long now = System.currentTimeMillis();
        for(File file: files)
        {
            if(now - file.lastModified() > EXPIRED)
            {
                try
                {
                    System.out.println("** 清理过期文件: " + file.getAbsolutePath());
                    FileUtils.deleteQuietly(file);
                }catch (Exception e){}
            }
        }
    }
}
