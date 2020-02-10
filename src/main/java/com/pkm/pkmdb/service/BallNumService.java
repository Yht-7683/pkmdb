package com.pkm.pkmdb.service;

import com.pkm.pkmdb.object.BallBag;
import com.pkm.pkmdb.object.BallNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BallNumService {
    @Autowired
    BallNumService ballNumService;
    public BallNum getBallNum(List<BallBag> list){
        BallNum ballNum = new BallNum();
        list.forEach(ballBag -> {
            int id=ballBag.getId();
            int num=ballBag.getNum();
            if(id==1) ballNum.setNum1(num);
            else if (id==2) ballNum.setNum2(num);
            else if (id==3) ballNum.setNum3(num);
            else if (id==4) ballNum.setNum4(num);
            else if (id==5) ballNum.setNum5(num);

        });
        return ballNum;
    }
}
