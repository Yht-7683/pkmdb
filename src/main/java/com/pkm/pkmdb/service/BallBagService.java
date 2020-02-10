package com.pkm.pkmdb.service;

import com.pkm.pkmdb.dao.BallBagDao;
import com.pkm.pkmdb.object.BallBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BallBagService {
    @Autowired
    BallBagDao ballBagDao;
    public List<BallBag> getBallBagById(String id){
        return ballBagDao.getBallBagById(id);
    }
}
