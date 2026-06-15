package com.app.projectlogistics.DataTransferObject.PageFindingDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Component;

@Component
public class PageDTO {

    @Positive
    @Max(value = Integer.MAX_VALUE)
    private Integer pageNo = 1;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
