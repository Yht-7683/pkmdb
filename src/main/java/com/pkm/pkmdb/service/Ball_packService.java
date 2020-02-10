package com.pkm.pkmdb.service;

import com.pkm.pkmdb.dao.Ball_packDao;
import com.pkm.pkmdb.object.Ball_pack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Ball_packService {
    @Autowired
    private Ball_packDao ball_packDao;
    public Ball_pack getBall_packById(String userid, int ballid){
        return ball_packDao.getBall_packById(userid,ballid);
    }
    public List<Ball_pack> getAllBall_pack(){
        return ball_packDao.getAllBall_pack();
    }
    public List<Ball_pack> searchBall_pack(String keyword){
        return ball_packDao.searchBall_pack("%"+keyword+"%");
    }
    public Boolean addBall_pack(Ball_pack object){
        return ball_packDao.addBall_pack(object);
    }
    public Boolean delBall_pack(int id){
        return ball_packDao.delBall_pack(id);
    }
    public Boolean upBall_pack(String userid, int ballid ,int num){
        return ball_packDao.upBall_pack(userid,ballid,num);
    }
}
