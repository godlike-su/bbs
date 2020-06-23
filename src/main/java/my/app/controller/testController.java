package my.app.controller;

import af.spring.AfRestData;
import my.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController
{
    @Autowired
    UserService userService;

    @GetMapping("/get")
    public Object getUser()
    {
        return new AfRestData(userService.selectUserAll(null));
    }
}
