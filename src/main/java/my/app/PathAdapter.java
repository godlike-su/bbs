package my.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页路径
 */
@Controller
public class PathAdapter
{
    @GetMapping( { "", "/", "/index.html"} )
    public String index()
    {
        return "forward:/message/list";
    }
}
