package com.pkm.pkmdb.dao;

import com.pkm.pkmdb.object.BallBag;
import com.pkm.pkmdb.object.BallNum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BallNumDao {
    BallNum getBallNum(List<BallBag> list);
}
