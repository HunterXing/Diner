package com.aiit.diner.service.impl;

import com.aiit.diner.common.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aiit.diner.entity.Employee;
import com.aiit.diner.mapper.EmployeeMapper;
import com.aiit.diner.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{
    @Override
    public R<Employee> login(HttpServletRequest request, Employee employee) {
        //1、将页面提交的密码password进行md5加密处理

        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username和password查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Employee::getUsername, employee.getUsername())
                .eq(Employee::getPassword, password);
        Employee emp = getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        // 4、查看是否禁用
        if(emp.getStatus() == 0){
            return  R.error("用户已禁用");
        }
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }
}
