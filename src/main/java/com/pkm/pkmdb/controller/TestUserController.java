package com.pkm.pkmdb.controller;


import com.pkm.pkmdb.domain.Res;
import com.pkm.pkmdb.object.*;
import com.pkm.pkmdb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("/testBoot")
public class TestUserController {
    @Autowired
    UserService userService;
    @Autowired
    PkmBagService pkmBagService;
    @Autowired
    Ball_packService ball_packService;
    @Autowired
    private BallService ballService;
    @Autowired
    private BallBagService ballBagService;
    @RequestMapping("upBallPack/{userid}/{ballid}/{num}")
    public List up(@PathVariable("userid") String userid,@PathVariable("ballid") int ballid, @PathVariable("num") int num){
        Ball ball=ballService.getBallById(ballid);
        User user=userService.getUserById(userid);
        Ball_pack ball_pack=ball_packService.getBall_packById(userid,ballid);
        int total = ball.getMoney()*num;
        if(user.getMoney()>=total) {
            user.setMoney(user.getMoney() - total);
            userService.upUser(user);
            ball_packService.upBall_pack(userid, ballid, ball_pack.getNum() + num);
            System.out.println("购买成功");
        }
        else System.out.println("购买失败");
        List<BallBag> list=ballBagService.getBallBagById(userid);
        return list;
    }
    @RequestMapping("showUserBall/{userid}")
    public List show(@PathVariable("userid") String userid){
        List<BallBag> list=ballBagService.getBallBagById(userid);
        return list;
    }

//    @RequestMapping("getUser/{id}/{name}")
//    public List<PkmBag> getId(@PathVariable("id") String id,@PathVariable("name") String name){
//        return pkmBagService.searchPkmBag(id ,name);
//    }
////    @RequestMapping("getall")
////    public List<Bag> getall(){
////        return bagService.getAllBag();
////    }
//    @RequestMapping("test")
//    public String getRandomString(){
//            String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//            Random random=new Random();
//            StringBuffer sb=new StringBuffer();
//            for(int i=0;i<6;i++){
//                int number=random.nextInt(62);
//                sb.append(str.charAt(number));
//            }
//            return sb.toString();
//    }
//    @RequestMapping("del/{id}")
//    public List<User> del(@PathVariable String id){
//        userService.delUser(id);
//        return userService.getAllUser();
//    }
//    public void addBall_pack(String userid){
//        for(int i=0;i<5;i++){
//            Ball_pack ball_pack=new Ball_pack(i,userid,i+1,0);
//            ball_packService.addBall_pack(ball_pack);
//
//        }
//
//    }
//    @RequestMapping("add")
//    public List<User> add(){
//        Date date1=new Date();
//        System.out.println(date1);
//        User user1=new User("a124","test","123","123456","123456","正常","用户","男",new java.sql.Date(date1.getTime()),1000);
//        userService.addUser(user1);
//        addBall_pack(user1.getId());
//        return userService.getAllUser();
//    }
//    @RequestMapping("up")
//    public User upd(){
//        Date date1=new Date();
//        System.out.println(date1);
//        User user1=new User("a123","test2","123","123456","123456","正常","用户","男",date1,1000);
//        userService.upUser(user1);
//        return userService.getUserById(user1.getId());
//    }





}
