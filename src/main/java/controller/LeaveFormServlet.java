package controller;

import com.alibaba.fastjson.JSON;
import entity.LeaveForm;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.LeaveFormService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LeaveFormServlet",urlPatterns = "/leave/*")
public class LeaveFormServlet extends HttpServlet {
    private LeaveFormService leaveFormService=new LeaveFormService();
    private Logger logger= LoggerFactory.getLogger(LeaveFormServlet.class);
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String uri=request.getRequestURI();
        String methodName=uri.substring(uri.lastIndexOf("/")+1);
        if(methodName.equals("create")){
            this.create(request,response);
        }else if(methodName.equals("list")){
            this.getLeaveFormList(request,response);
        }else if(methodName.equals("audit")){
            this.audit(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }

    /**
     * create leave application form
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void create(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        //1. get all the required data
        HttpSession session=request.getSession();
        User user=(User)session.getAttribute("login_user");
        String formType=request.getParameter("formType");
        String strStartTime=request.getParameter("startTime");
        String strEndTime=request.getParameter("endTime");
        String reason=request.getParameter("reason");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH");
        //2. interface with service
        Map result=new HashMap<>();
        try {
            LeaveForm leaveForm=new LeaveForm();
            leaveForm.setEmployeeId(user.getEmployeeId());
            leaveForm.setStartTime(sdf.parse(strStartTime));
            leaveForm.setEndTime(sdf.parse(strEndTime));
            leaveForm.setFormType(Integer.parseInt(formType));
            leaveForm.setReason(reason);
            leaveForm.setCreateTime(new Date());
            leaveFormService.createLeaveForm(leaveForm);
            //3. store and transport response
            result.put("code","0");
            result.put("message","success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Application Error",e);
            result.put("code",e.getClass().getSimpleName());
            result.put("message",e.getMessage());
        }
        String json= JSON.toJSONString(result);
        response.getWriter().println(json);
    }

    /**
     * get the leave list to audit
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getLeaveFormList(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        User user=(User) request.getSession().getAttribute("login_user");
        List<Map> formList=leaveFormService.getLeaveFormList("Processing",user.getEmployeeId());
        Map result=new HashMap();
        result.put("code","0");
        result.put("msg","");
        result.put("count",formList.size());
        result.put("data",formList);
        String json=JSON.toJSONString(result);
        response.getWriter().println(json);
    }

    private void audit(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        String formId=request.getParameter("formId");
        String result=request.getParameter("result");
        String reason=request.getParameter("reason");
        User user=(User)request.getSession().getAttribute("login_user");
        Map mpResult=new HashMap();
        try {
            leaveFormService.audit(Long.parseLong(formId), user.getEmployeeId(), result, reason);
            mpResult.put("code","0");
            mpResult.put("message","success");
        }catch (Exception e){
            logger.error("Failed",e);
            mpResult.put("code",e.getClass().getSimpleName());
            mpResult.put("message",e.getMessage());
        }
        String json=JSON.toJSONString(mpResult);
        response.getWriter().println(json);

    }
}
