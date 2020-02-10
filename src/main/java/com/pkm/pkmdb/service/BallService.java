package com.pkm.pkmdb.service;

import com.pkm.pkmdb.dao.BallDao;
import com.pkm.pkmdb.object.Ball;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BallService {
    @Autowired
    private BallDao ballDao;
    public Ball getBallById(int id){
        return ballDao.getBallById(id);
    }
    public List<Ball> getAllBall(){
        return ballDao.getAllBall();
    }
    public List<Ball> searchBall(String keyword){
        return ballDao.searchBall("%"+keyword+"%");
    }
    public Boolean addBall(Ball object){
        return ballDao.addBall(object);
    }
    public Boolean delBall(int id){
        return ballDao.delBall(id);
    }
    public Boolean upBall(Ball object){
        return ballDao.upBall(object);
    }
}
