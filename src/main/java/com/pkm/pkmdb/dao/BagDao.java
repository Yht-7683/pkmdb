package com.pkm.pkmdb.dao;

import com.pkm.pkmdb.object.Bag;
import com.pkm.pkmdb.object.Ball;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BagDao {
    List<Bag> searchBag(String keyword);
    List<Bag> getAllBag();
}
