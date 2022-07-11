package com.vispax.authorization.application.repository;

import com.vispax.authorization.application.model.UserRole;
import com.vispax.authorization.application.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole,UserRoleId> {

    @Query("select u from UserRole u where u.userId=:userId")
    List<UserRole> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("delete from UserRole u where u.userId=:userId and u.roleId=:roleId")
    void deleteUserRole(@Param("userId") Long userId,@Param("roleId") Integer roleId);

/*
    @Query("select r from UserRole r, User u where u.userId= r.userId and u.username in :lstUser")
    List<UserRole> findAllUserRoles(@Param("lstUser") List<String> lstUserName);
*/


}
