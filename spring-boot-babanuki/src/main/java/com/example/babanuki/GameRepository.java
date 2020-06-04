package com.example.babanuki;

import org.springframework.data.repository.CrudRepository;

import com.example.babanuki.Game;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface GameRepository extends CrudRepository<Game, Integer> {

}
