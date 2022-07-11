package com.vispax.authorization.application.service;


import com.raiconic.infra.service.facade.BaseFacadeService;
import com.vispax.authorization.application.model.Role;
import com.vispax.authorization.application.repository.RoleRepository;
import com.vispax.authorization.application.service.mapper.user.RoleMapper;
import com.vispax.authorization.dto.user.response.RoleList;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class RoleServiceImpl extends BaseFacadeService implements RoleService {

    private static final long serialVersionUID = 1L;

    private final RoleRepository roleRepository;



    @Override
    public RoleList findAll(){
        String activeCodeUser = "";
        List<Role> lstRole = roleRepository.findAll();

        RoleList roleList = RoleList.builder().lstRole(RoleMapper.INSTANCE.convert(lstRole)).build();

        return roleList;

    }
}
