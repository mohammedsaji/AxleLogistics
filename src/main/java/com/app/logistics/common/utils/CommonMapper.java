package com.app.logistics.common.utils;

public interface CommonMapper<REQ,RESP,VO>{

    VO toVO(REQ Dto);
    RESP toDTO(VO vo);

}
