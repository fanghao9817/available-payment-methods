package edu.nyit.haoyu;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Jerry Fang
 * @Date: 2023/07/08/4:53
 * @Description:
 */
@SpringBootApplication
@Slf4j
@ServletComponentScan
@MapperScan
@EntityScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}