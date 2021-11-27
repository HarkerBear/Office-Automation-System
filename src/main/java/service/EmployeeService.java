package service;

import dao.EmployeeDao;
import entity.Employee;
import utils.MybatisUtils;

public class EmployeeService {
    public Employee selectById(Long employeeId){
        return (Employee) MybatisUtils.executeQuery(sqlSession -> {
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            return employeeDao.selectById(employeeId);
        });
    }
}
