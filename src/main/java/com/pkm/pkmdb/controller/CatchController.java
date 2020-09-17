package com.pkm.pkmdb.controller;

import com.pkm.pkmdb.vo.Res;
import com.pkm.pkmdb.object.*;
import com.pkm.pkmdb.service.BallService;
import com.pkm.pkmdb.service.Ball_packService;
import com.pkm.pkmdb.service.Pkm_packService;
import com.pkm.pkmdb.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class CatchController {
    @Autowired
    private PokemonService pokemonService;
    @Autowired
    private Pkm_packService pkm_packService;
    @Autowired
    private BallService ballService;
    @Autowired
    private Ball_packService ball_packService;
    @RequestMapping("public/findPokemon")
    public Res findPokemon(){
        int id = 1 + (int)(Math.random() * (pokemonService.getPokemonNum() - 1 + 1));
        Pokemon pokemon=pokemonService.getPokemonById(id);
        return new Res(Res.SUCCESS,"发现一只野生的"+pokemon.getName(),pokemon);
    }
    @RequestMapping("public/catchPokemon")
    public Res catchPokemon(@RequestParam("userid") String userid,@RequestParam("ballid") int ballid,@RequestParam("pokemonid") int pokemonid){
        Ball ball=ballService.getBallById(ballid);
        Ball_pack ball_pack=ball_packService.getBall_packById(userid,ballid);
        Pokemon pokemon=pokemonService.getPokemonById(pokemonid);
        if(ball_pack.getNum()>0){
            double d = 0 + Math.random() * 100 % (100 - 0 + 1);
            int lv = 1+ (int)(Math.random() * (100 - 1 + 1));
            if(d<=ball.getProbability()){
                Pkm_pack pkm_pack=new Pkm_pack();
                pkm_pack.setLV(lv);
                Date date=new Date();
                pkm_pack.setMeetTime(date);
                pkm_pack.setUser_id(userid);
                pkm_pack.setPokemon_id(pokemonid);
                pkm_packService.addPkm_pack(pkm_pack);
                ball_packService.upBall_pack(userid,ballid,ball_pack.getNum()-1);
                PkmBag pkmBag=new PkmBag(pokemon,date,lv);
                return new Res(Res.SUCCESS,"捕捉成功",pkmBag);
            }
            else{
                ball_packService.upBall_pack(userid,ballid,ball_pack.getNum()-1);
                return new Res(Res.ERROR,"捕捉失败",null);
            }
        }
        else return new Res(Res.ERROR,"精灵球数量不足",null);


    }
}
