package com.pkm.pkmdb.dao;

import com.pkm.pkmdb.object.BallBag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BallBagDao {
    List<BallBag> getBallBagById(String id);

}
