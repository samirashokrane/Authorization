package com.vispax.authorization.application.repository;

import com.raiconic.infra.dto.enums.RoleEnum;
import com.vispax.authorization.application.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findFirstByName(final RoleEnum name);
    Optional<Role> findByName(final String roleName);



}
