package com.pkm.pkmdb.service;

import com.pkm.pkmdb.dao.PokemonDao;
import com.pkm.pkmdb.object.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonService {
    @Autowired
    private PokemonDao pokemonDao;
    public Pokemon getPokemonById(int id){
        return pokemonDao.getPokemonById(id);
    }
    public List<Pokemon> getAllPokemon(){
        return pokemonDao.getAllPokemon();
    }
    public List<Pokemon> searchPokemon(String keyword){
        return pokemonDao.searchPokemon("%"+keyword+"%");
    }
    public Boolean addPokemon(Pokemon object){
        return pokemonDao.addPokemon(object);
    }
    public Boolean delPokemon(int id){
        return pokemonDao.delPokemon(id);
    }
    public Boolean upPokemon(Pokemon object){
        return pokemonDao.upPokemon(object);
    }
    public int getPokemonNum(){
        return pokemonDao.getPokemonNum();
    }
}
