package my.app.task;

import my.app.support.MyBatis;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyTaskManager extends Thread
{
    private List<TaskInfo> taskList = new ArrayList<>();
    private boolean quitFlag = false;
    private long sleep = 10*1000;

    public void doStart()
    {
        start();
    }

    public void doQuit()
    {
        quitFlag = true;
        interrupt();
    }

    @Override
    public void run()
    {
        while (!quitFlag)
        {
            try { sleep(this.sleep); }catch(Exception e) {}
            try(SqlSession sql = MyBatis.factory.openSession()){
                sql.selectOne("user.selectCount");
            }
            for(TaskInfo info: taskList)
            {
                executeEveryDay ( info );
            }
        }
    }

    private void executeEveryDay(TaskInfo info)
    {
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_YEAR);
        if(today == info.lastValue)
            return; // 今天已经完成

        // 执行任务
        try{
            System.out.printf("** 执行定期任务 [ %s ] :。。。\n", info.name);
            info.task.execute();
            info.status = 0;
            System.out.printf("** 定期任务 [ %s ]: OK ! \n", info.name);
        }catch (Exception e)
        {
            String reason = e.getMessage();
            if(reason == null) e.getClass().getName();
            System.out.printf("** 定期任务 [ %s ]: 失败 !  %s\n", info.name, reason);
            info.status = -1;
            e.printStackTrace();
        }

        info.lastValue = today;
        info.lastTime = cal.getTimeInMillis();
    }

    public void addTask(Task task, String name)
    {
        TaskInfo info = new TaskInfo();
        info.name = name;
        info.task = task;
        taskList.add(info);
    }

    // 任务信息
    private static class TaskInfo
    {
        String name = "";
        long lastTime = 0;
        int lastValue = -1;
        int fromHour = -1; // [未用到] 开始时间点
        int toHour = -1; // [未用到] 结束时间点
        int status = 0; // [未用到] 执行成功，还是失败
        int numRetry = 0; // [未用到] 错误重试次数
        Task task;
    }
}
