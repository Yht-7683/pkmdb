package com.pkm.pkmdb.service;

import com.pkm.pkmdb.dao.BagDao;
import com.pkm.pkmdb.object.Bag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BagService {
    @Autowired
    private BagDao bagDao;
    public List<Bag> searchBag(String keyword){
        return  bagDao.searchBag("%"+keyword+"%");

    }
    public List<Bag> getAllBag(){
        return  bagDao.getAllBag();
    }

}
