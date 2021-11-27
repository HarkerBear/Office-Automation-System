package dao;

import entity.Employee;

public interface EmployeeDao {
    public Employee selectById(Long employeeId);
}
