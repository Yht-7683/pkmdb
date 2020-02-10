package com.pkm.pkmdb.dao;

import com.pkm.pkmdb.object.PkmBag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PkmBagDao {
    List<PkmBag> getPkmBagById(String id);
    List<PkmBag> searchPkmBag(@Param("id") String id, @Param("name")String name);
}
