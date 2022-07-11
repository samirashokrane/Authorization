package com.vispax.authorization.application.repository;

import com.vispax.authorization.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<User,Integer> {


    Optional<User> findByUsername(String name);

    @Modifying
    @Query("delete from User u where u.username=:username")
    void deleteUser(@Param("username") String username);


    @Query("select u from User u where u.username in :usernames")
    List<User> findAllUserByNames(@Param("usernames") List<String> usernames);

}
