package com.app.projectlogistics.DataTransferObject.Utilities;

import com.app.projectlogistics.Enum.CarrierOptionEnum;

import java.util.ArrayList;
import java.util.List;

public class CarrierOptionDTO {

    private List<CarrierOptionEnum> carrierOptionEnumList = new ArrayList<>();

    public List<CarrierOptionEnum> getCarrierOptionEnumList() {
        return carrierOptionEnumList;
    }

    public void setCarrierOptionEnumList(CarrierOptionEnum carrierOptionEnum) {
        carrierOptionEnumList.add(carrierOptionEnum);
    }
}
