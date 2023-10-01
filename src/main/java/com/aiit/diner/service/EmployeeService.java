package com.aiit.diner.service;

import com.aiit.diner.common.R;
import com.aiit.diner.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xingheng
 */
public interface EmployeeService extends IService<Employee> {

    R<Employee> login(HttpServletRequest request, Employee employee);

    R<Boolean> addEmployee(Employee employee);
}
