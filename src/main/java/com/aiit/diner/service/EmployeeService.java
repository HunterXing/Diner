package com.aiit.diner.service;

import com.aiit.diner.common.R;
import com.aiit.diner.entity.Employee;
import com.aiit.diner.vo.EmployeeVo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xingheng
 */
public interface EmployeeService extends IService<Employee> {

    R<EmployeeVo> login(HttpServletRequest request, Employee employee);

    R<Boolean> addEmployee(Employee employee);
}
