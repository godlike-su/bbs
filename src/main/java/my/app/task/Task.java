package my.app.task;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface Task
{
    public abstract void execute() throws Exception;
}
