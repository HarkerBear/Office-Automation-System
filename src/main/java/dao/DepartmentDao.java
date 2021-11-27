package dao;

import entity.Department;

public interface DepartmentDao {
    public Department selectById(Long departmentId);
}
