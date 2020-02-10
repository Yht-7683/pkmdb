package com.pkm.pkmdb.controller;

import com.pkm.pkmdb.domain.Res;
import com.pkm.pkmdb.object.Ball;
import com.pkm.pkmdb.object.BallBag;
import com.pkm.pkmdb.object.Ball_pack;
import com.pkm.pkmdb.object.User;
import com.pkm.pkmdb.service.BallBagService;
import com.pkm.pkmdb.service.BallService;
import com.pkm.pkmdb.service.Ball_packService;
import com.pkm.pkmdb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BuyBallController {
    @Autowired
    private Ball_packService ball_packService;
    @Autowired
    private UserService userService;
    @Autowired
    private BallService ballService;
    @Autowired
    private BallBagService ballBagService;
    @RequestMapping("/public/upBallPack")
    public Res up(@RequestParam("userid") String userid, @RequestParam("ballid") int ballid,@RequestParam("num") int num){
        Ball ball=ballService.getBallById(ballid);
        User user=userService.getUserById(userid);
        Ball_pack ball_pack=ball_packService.getBall_packById(userid,ballid);
        int total = ball.getMoney()*num;
        if(user.getMoney()>=total){
            user.setMoney(user.getMoney()-total);
            userService.upUser(user);
            if(ball_packService.upBall_pack(userid,ballid,ball_pack.getNum()+num))
                return new Res(Res.SUCCESS,"购买成功！",null);
            else return new Res(Res.ERROR,"购买失败！",null);
        }
        else {
            return new Res(Res.ERROR,"金币不足！",null);
        }
    }
    @RequestMapping("public/showUserBall")
    public Res show(@RequestParam("userid") String userid){
        List<BallBag> list=ballBagService.getBallBagById(userid);
        return new Res(Res.SUCCESS,"查找成功",list);
    }
}
