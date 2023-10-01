package com.aiit.diner.controller;

import com.aiit.diner.common.R;
import com.aiit.diner.entity.Employee;
import com.aiit.diner.service.EmployeeService;
import com.aiit.diner.utils.JwtUtils;
import com.aiit.diner.utils.RedisUtils;
import com.aiit.diner.vo.EmployeeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "员工管理相关接口")
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @ApiOperation("员工登录接口")
    @PostMapping("/login")
    public R<EmployeeVo> login(HttpServletRequest request, @RequestBody Employee employee){
        return employeeService.login(request, employee);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 获取当前的token
        String token = request.getHeader("token");
        // 根据token获取用户信息
        Long id = JwtUtils.getUserIdByToken(token);
        String key =  "TOKEN" + id;
        RedisUtils.delete(key);
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     */
    @PostMapping
    public R<Boolean> save(HttpServletRequest request, @RequestBody Employee employee) {
        Long currentUserId = (Long) request.getAttribute("currentUserId");
        employee.setCreateUser(currentUserId);
        employee.setUpdateUser(currentUserId);
        return employeeService.addEmployee(employee);
    }

}
