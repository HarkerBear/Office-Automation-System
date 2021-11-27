package controller;

import entity.Department;
import entity.Employee;
import entity.Node;
import entity.User;
import service.DepartmentService;
import service.EmployeeService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "IndexServlet",urlPatterns = "/index")
public class IndexServlet extends HttpServlet {
    private UserService userService= new UserService();
    private EmployeeService employeeService=new EmployeeService();
    private DepartmentService departmentService=new DepartmentService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        //get User
        User user=(User)session.getAttribute("login_user");
        //get Employee Info by userId
        Employee employee=employeeService.selectById(user.getEmployeeId());
        Department department=departmentService.selectById(employee.getDepartmentId());
        //get Nodes List by userId
        List<Node> list=userService.selectNodeByUserId(user.getUserId());
        //put Nodes List to request
        request.setAttribute("node_list",list);
        //put Employee Info to session for future usage
        session.setAttribute("current_employee",employee);
        session.setAttribute("current_department",department);
        request.getRequestDispatcher("/index.ftl").forward(request,response);
    }
}
