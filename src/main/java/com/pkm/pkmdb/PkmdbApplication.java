package com.pkm.pkmdb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@MapperScan("com.pkm.pkmdb.dao")
@SpringBootApplication
public class PkmdbApplication {

    public static void main(String[] args) {
        SpringApplication.run(PkmdbApplication.class, args);
    }

}
