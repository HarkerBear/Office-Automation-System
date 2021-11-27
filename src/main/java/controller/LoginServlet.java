package controller;

import com.alibaba.fastjson.JSON;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.exception.BusinessException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet",urlPatterns = "/check_login")
public class LoginServlet extends HttpServlet {
    Logger logger=LoggerFactory.getLogger(LoginServlet.class);
    private UserService userService=new UserService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        //accept input
        String userName=request.getParameter("username");
        String password=request.getParameter("password");
        //carry check flow
        Map<String,Object> result=new HashMap<>();
        try {
            User user=userService.checkLogin(userName,password);
            //store user info in session
            HttpSession session=request.getSession();
            session.setAttribute("login_user",user);
            result.put("code","0");
            result.put("message","success");
            result.put("redirect_url","/index");
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            result.put("code",e.getCode());
            result.put("message",e.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            result.put("code",e.getClass().getSimpleName());
            result.put("message",e.getMessage());
        }
        //return result
        String json=JSON.toJSONString(result);
        response.getWriter().println(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
