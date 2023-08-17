package com.arkwith.starter.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);

    @Query("select "
            + "distinct m "
            + "from Member m " 
            + "where "
            + "   m.username like %:kw% "
            + "   or m.nickname like %:kw% "
            + "   or m.name like %:kw% ")
    Page<Member> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
    
}
