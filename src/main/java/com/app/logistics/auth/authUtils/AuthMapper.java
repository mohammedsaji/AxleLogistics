package com.app.logistics.auth.authUtils;

import com.app.logistics.auth.dto.AuthRequest;
import com.app.logistics.auth.dto.AuthResponse;
import com.app.logistics.auth.entity.Auth;
import com.app.logistics.common.utils.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper extends CommonMapper<AuthRequest, AuthResponse, Auth> {
}
