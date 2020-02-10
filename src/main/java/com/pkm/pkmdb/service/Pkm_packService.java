package com.pkm.pkmdb.service;

import com.pkm.pkmdb.dao.Pkm_packDao;
import com.pkm.pkmdb.object.Pkm_pack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Pkm_packService {
    @Autowired
    private Pkm_packDao pkm_packDao;
    public Pkm_pack getPkm_packById(int id){
        return pkm_packDao.getPkm_packById(id);
    }
    public List<Pkm_pack> getAllPkm_pack(){
        return pkm_packDao.getAllPkm_pack();
    }
    public List<Pkm_pack> searchPkm_pack(String keyword){
        return pkm_packDao.searchPkm_pack("%"+keyword+"%");
    }
    public Boolean addPkm_pack(Pkm_pack object){
        return pkm_packDao.addPkm_pack(object);
    }
    public Boolean delPkm_pack(int id){
        return pkm_packDao.delPkm_pack(id);
    }
    public Boolean upPkm_pack(Pkm_pack object){
        return pkm_packDao.upPkm_pack(object);
    }
}
