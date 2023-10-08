package com.daoying.system.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据接口
 * @author daoying
 */

@Repository
public interface UserRepository extends JpaRepository<User,Integer>, CrudRepository<User,Integer> {
    Optional<User> findByAccount(String account);
}
