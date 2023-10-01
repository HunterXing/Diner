package com.aiit.diner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author xingheng
 */
@Slf4j
@CrossOrigin(origins = "*")
@SpringBootApplication
@ServletComponentScan
public class DinerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DinerApplication.class,args);
        log.info("项目启动成功...");
    }
}
