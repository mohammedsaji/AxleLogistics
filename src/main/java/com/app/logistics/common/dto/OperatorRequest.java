package com.app.logistics.common.dto;

import com.app.logistics.Enum.CarrierOptionEnum;

import java.util.ArrayList;
import java.util.List;

public class OperatorRequest {

    private List<CarrierOptionEnum> carrierOptionEnumList = new ArrayList<>();

    public List<CarrierOptionEnum> getCarrierOptionEnumList() {
        return carrierOptionEnumList;
    }

    public void setCarrierOptionEnumList(CarrierOptionEnum carrierOptionEnum) {
        carrierOptionEnumList.add(carrierOptionEnum);
    }
}
