package service;

import dao.DepartmentDao;
import entity.Department;
import utils.MybatisUtils;

public class DepartmentService {
    public Department selectById(Long departmentId){
        return (Department) MybatisUtils.executeQuery(sqlSession -> sqlSession.getMapper(DepartmentDao.class).selectById(departmentId));
    }
}
