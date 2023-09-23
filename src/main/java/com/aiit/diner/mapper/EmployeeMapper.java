package com.aiit.diner.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aiit.diner.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{
}
