package my.app.suController;

import my.app.db.User;
import my.app.db.UserAbility;
import my.app.support.MyBatis;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/u")
public class UserController
{
    //查看所有用户信息
    @GetMapping("/user/list")
    public String list(Model model,
                       @SessionAttribute User user,
                       Integer pageNumber,
                       String filter) throws Exception
    {
        if(!user.isAdmin)
            throw new Exception("您不是管理员！");

        //查询条件
        if(pageNumber==null) pageNumber=1;
        if(filter==null) filter = "";

        Map<String, Object> map = new HashMap<>();
        map.put("filter", filter);  //筛选器
        //count：符合条件的记录一共有多少条
        int count = 0;
        try(SqlSession sql =  MyBatis.factory.openSession()){
            count = sql.selectOne("user.selectCount", map);
        }

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
        List<User> userList;
        try(SqlSession sql = MyBatis.factory.openSession()){
            userList = sql.selectList("user.selectAll", map);
        }


        model.addAttribute("userList", userList);   //所有用户
        model.addAttribute("count",count);  //总数量
        model.addAttribute("pageCount",pageCount);  //总页数
        model.addAttribute("pageNumber",pageNumber);  //当前页码：1,2,3...
        model.addAttribute("baseIndex", count - pageSize * (pageNumber-1)); //总数量
        model.addAttribute("filter", filter);

        return "user/list";
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
        User user1;
        try(SqlSession sql = MyBatis.factory.openSession()){
            user1 = sql.selectOne("user.selectAll", map);
        }
        if(user1==null)
            throw new Exception("无此用户 ，id=" + id);

        //查询用户权力
        UserAbility userAbility;
        try(SqlSession sql = MyBatis.factory.openSession()){
            userAbility = sql.selectOne("userAbility.selectOne", map);
        }

        model.addAttribute("user", user1);
        model.addAttribute("userAbility", userAbility);

        return "user/edit";
    }
}
