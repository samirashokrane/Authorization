package com.vispax.authorization.application.service.mapper.user;

import com.raiconic.infra.dto.enums.RoleEnum;
import com.vispax.authorization.application.model.Role;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class RoleMapper {

    public static final RoleMapper INSTANCE = new RoleMapper();

    public static List<RoleEnum> convert(List<Role> lstRole) {
        List<RoleEnum> lstRoleEnum = new ArrayList<>();
        for (Role role : lstRole){
            lstRoleEnum.add(role.getName());
        }
        return lstRoleEnum;
    }
}
