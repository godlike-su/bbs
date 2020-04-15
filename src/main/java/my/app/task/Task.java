package my.app.task;

import javax.servlet.http.HttpServletRequest;

public interface Task
{
    public abstract void execute() throws Exception;
}
