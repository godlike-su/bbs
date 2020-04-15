package my.app.filter;


import com.alibaba.fastjson.JSONObject;
import my.app.db.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName="UserLoginFilter", urlPatterns="/u/*")
public class UserLoginFilter implements Filter
{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        //检查用户登录
        if(true)
        {
            HttpSession session =  req.getSession();
            User user = (User) session.getAttribute("user");
            if(user == null)
            {
                //用户尚未登录
                if(uri.endsWith(".do"))
                {
                    // REST请求：返回-100错误
                    JSONObject json = new JSONObject(true);
                    json.put("error", -100);
                    json.put("reason","尚未登录!");
                    resp.setCharacterEncoding("UTF-8");
                    resp.setContentType("application/json");
                    resp.getWriter().write(json.toJSONString());
                    return;
                }
                else
                {
                    // ?号后面的部分也得带上
                    String query = req.getQueryString();
                    if(query!=null && query.length() > 0)
                        uri += "?" + query;

                    //MVC请求： 返回302重定向
                    resp.sendRedirect(req.getContextPath()
                                    + "/login"
                                    + "?returnUrl=" + uri);
                    return;
                }
            }
        }
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy()
    {

    }
}
