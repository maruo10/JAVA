package com.example.babanuki;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.babanuki.Member;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface MemberRepository extends CrudRepository<Member, Integer> {
  List<Member> findByGameId(Integer gameId);
}