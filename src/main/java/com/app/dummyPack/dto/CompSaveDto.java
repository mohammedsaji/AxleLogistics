package com.app.dummyPack.dto;

import org.springframework.stereotype.Component;

@Component
public class CompSaveDto {

    private RqtAuthDto rqtAuthDto;

    private RqtBkDto rqtBkDto;

    public RqtAuthDto getRqtAuthDto() {
        return rqtAuthDto;
    }

    public void setRqtAuthDto(RqtAuthDto rqtAuthDto) {
        this.rqtAuthDto = rqtAuthDto;
    }

    public RqtBkDto getRqtBkDto() {
        return rqtBkDto;
    }

    public void setRqtBkDto(RqtBkDto rqtBkDto) {
        this.rqtBkDto = rqtBkDto;
    }
}
