package com.daoying.system.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * token数据接口
 * @author daoying
 */

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>, CrudRepository<Token, Integer> {

    Optional<Token> findByToken(String token);

    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(@Param("id") Integer id);
}
