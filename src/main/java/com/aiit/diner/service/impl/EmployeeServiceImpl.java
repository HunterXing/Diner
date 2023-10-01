package com.aiit.diner.service.impl;

import com.aiit.diner.common.R;
import com.aiit.diner.utils.JwtUtils;
import com.aiit.diner.utils.RedisUtils;
import com.aiit.diner.vo.EmployeeVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aiit.diner.entity.Employee;
import com.aiit.diner.mapper.EmployeeMapper;
import com.aiit.diner.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * @author xingheng
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{
    @Value("${JWT.expireTime}")
    private int expireTime;
    @Override
    public R<EmployeeVo> login(HttpServletRequest request, Employee employee) {
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
            return R.error("用户已禁用");
        }

        // 将token存入redis/或者更新
        String key = "TOKEN" +  String.valueOf(emp.getId());
        // 生成token
        String token = JwtUtils.generateToken(emp.getId());
        log.info( "登录成功，token: {}" , token);
        RedisUtils.set(key, token, 60 * expireTime);

        EmployeeVo employeeVo = new EmployeeVo();
        BeanUtils.copyProperties(emp,employeeVo);
        employeeVo.setToken(token);
        return R.success(employeeVo);
    }

    @Override
    public R<Boolean> addEmployee(Employee employee) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = getOne(queryWrapper);
        if(emp != null){
            return R.error("用户名已存在");
        }
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateUser(1L);
        employee.setUpdateUser(1L);
        // 设置时间 LocalDateTime
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        if (save(employee)) {
            return R.success(true);
        }
        return R.error("新增失败");
    }
}
