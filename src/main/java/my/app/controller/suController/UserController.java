package my.app.controller.suController;

import af.spring.AfRestData;
import my.app.entity.User;
import my.app.entity.UserAbility;
import my.app.service.UserAbilityService;
import my.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员查看所有用户信息
 */
@Controller
@RequestMapping("/u")
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserAbilityService userAbilityService;

    //查看所有用户信息
    @GetMapping("/user/list")
    public String list(Model model,
                       @SessionAttribute User user,
                       Integer pageNumber,
                       String filter) throws Exception
    {
        if(!user.isAdmin)
            throw new Exception("您不是管理员！");

        return "user/list";
    }

    //返回所有用户信息
    @GetMapping("/user/listInfo")
    public Object list2( Integer pageNumber,
                         String filter) throws Exception
    {
        //查询条件
        if(pageNumber==null) pageNumber=1;
        if(filter==null) filter = "";

        Map<String, Object> map = new HashMap<>();
        map.put("filter", filter);  //筛选器
        //count：符合条件的记录一共有多少条
        int count = userService.userCount(map);

        //一页显示的数据量
        int pageSize = 10;
        int pageCount = count / pageSize;
        if(count % pageSize != 0) pageCount += 1;

        //查询
        //开始查询的信息index
        int startIndex = pageSize * (pageNumber-1);
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);

        //查询用户
        List<User> userList = userService.selectUserAll(map);

        Map<String, Object> result = new HashMap();
        result.put("userList", userList);
        result.put("count", count);
        result.put("pageNumber", pageNumber);
        result.put("filter", filter);

        return new AfRestData(result);
    }

    @GetMapping("/user/edit")
    public String edit(Model model,
                       @SessionAttribute User user,
                       int id) throws Exception
    {
        if(!user.isAdmin)
            throw new Exception("您不是管理员！");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        User user1 = userService.selectUser(id);
        if(user1==null)
            throw new Exception("无此用户 ，id=" + id);

        //查询用户权力
        UserAbility userAbility = userAbilityService.selectAbilityOne(id);

        model.addAttribute("user", user1);
        model.addAttribute("userAbility", userAbility);

        return "user/edit";
    }
}
