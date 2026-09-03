// utils/OperatorMapper.java — one-line import fix
package com.app.logistics.operator.utils;

import com.app.logistics.common.utils.CommonMapper;
import com.app.logistics.operator.dto.OperatorRequest;
import com.app.logistics.operator.dto.OperatorResponse;
import com.app.logistics.operator.entity.Operator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OperatorMapper extends CommonMapper<OperatorRequest, OperatorResponse, Operator> {
}