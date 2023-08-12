package com.arkwith.starter.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByUsername(String username);
    
}
