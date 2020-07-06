package com.pkm.pkmdb;

import com.pkm.pkmdb.object.Bag;
import com.pkm.pkmdb.service.BagService;
import org.junit.Test;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PkmdbApplicationTests {
    @Autowired
    private BagService bagService;
    @Autowired
    private RedisTemplate<Object, Object> template;
    @Test
    public void testredis(){
        List<Bag> list = bagService.getAllBag();
        template.opsForSet().add("2","s","d");
        System.out.println(template.opsForSet().members("1"));

    }


}
