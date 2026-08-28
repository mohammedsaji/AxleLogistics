package com.app.logistics.manager.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.manager.dto.ManagerRequest;
import com.app.logistics.manager.dto.ManagerResponse;
import com.app.logistics.manager.entity.Manager;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManagerMapper extends CommonMapper<ManagerRequest, ManagerResponse, Manager> {
}
