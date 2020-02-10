package com.pkm.pkmdb.service;

import com.pkm.pkmdb.dao.PkmBagDao;
import com.pkm.pkmdb.object.PkmBag;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PkmBagService {
    @Autowired
    PkmBagDao pkmBagDao;
    public List<PkmBag> getPkmBagById(String id){
        return pkmBagDao.getPkmBagById(id);
    }
    public List<PkmBag> searchPkmBag(String id,String name){
        return pkmBagDao.searchPkmBag(id,"%"+name+"%");
    }
}
