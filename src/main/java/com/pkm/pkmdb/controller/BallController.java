package com.pkm.pkmdb.controller;

import com.pkm.pkmdb.domain.Res;
import com.pkm.pkmdb.object.Ball;
import com.pkm.pkmdb.object.Pokemon;
import com.pkm.pkmdb.service.BallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BallController {
    @Autowired
    BallService ballService;
    @RequestMapping("/public/getAllBall")
    public Res show(){
        List<Ball> list=ballService.getAllBall();
        return new Res(Res.SUCCESS,"查找成功",list);
    }
    //删除精灵
    @RequestMapping("/public/delBall")
    public Res del(int id){
        if(ballService.delBall(id)){
            return new Res(Res.SUCCESS,"删除成功！",null);
        }else {
            return new Res(Res.ERROR,"删除失败！",null);
        }
    }
    //添加精灵
    @RequestMapping("/public/addBall")
    public Res add(Ball ball){
        if(ballService.getBallById(ball.getId())!=null){
            return new Res(Res.FAILURE,"宝可梦已经存在！",null);
        }
        if(ballService.addBall(ball)){
            return new Res(Res.SUCCESS,"添加成功！",null);
        }else {
            return new Res(Res.ERROR,"添加失败！",null);
        }
    }
    //更新精灵信息
    @RequestMapping("/public/upBall/")
    public Res up(Ball ball){
        if(ballService.upBall(ball)){
            return new Res(Res.SUCCESS,"修改成功！",null);
        }
        else {
            return new Res(Res.ERROR,"修改失败！",null);
        }
    }

    @RequestMapping("/public/searchBall")
    public Res select(@RequestParam("searchParam") String keyword){
        List<Ball> list=ballService.searchBall(keyword);
        return new Res(Res.SUCCESS,"查找成功",list);
    }
}
