package com.app.logistics.operator.entity;

import com.app.logistics.common.entity.BaseEntity;
import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.vehicle.entity.Vehicle;
import com.app.logistics.manager.entity.Manager;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OPRTR_MST")
public class Operator extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name = "OPRTR_ID")
    private Integer operatorId;

    @Size(max = 255)
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "OPRTR_NAME")
    private String operatorName;

    @Size(max = 20)
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "AIR|SEA|ROAD|RAIL", groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "OPRTR_TRANSPORT_TYPE")
    private String operatorTransportType;

    @Column(name = "UPDATED_BY")
    @Max(value = Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    private Integer updatedBy;

    @OneToMany(mappedBy = "operatorVO", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Manager> managerList = new ArrayList<>();

    @OneToMany(mappedBy = "operatorVO", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Driver> driverList = new ArrayList<>();

    @OneToMany(mappedBy = "operatorVO", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Vehicle> vehicleVOList = new ArrayList<>();

    public void addManager(Manager manager) {
        managerList.add(manager);
    }

    public void removeManager(Manager manager) {
        managerList.remove(manager);
    }

    public List<Manager> getManagerVOList() {
        return managerList;
    }

    public void addDriver(Driver driver) {
        driverList.add(driver);
        driver.setOperatorVO(this);
    }

    public void removeDriver(Driver driver) {
        driverList.remove(driver);
        driver.setOperatorVO(null);
    }

    public List<Driver> getDriverList() {
        return driverList;
    }

    public void addVehicle(Vehicle vehicleVO) {
        vehicleVOList.add(vehicleVO);
        vehicleVO.setOperatorVO(this);
    }

    public void removeVehicle(Vehicle vehicleVO) {
        vehicleVOList.remove(vehicleVO);
        vehicleVO.setOperatorVO(null);
    }

    public List<Vehicle> getVehicleList() {
        return vehicleVOList;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorTransportType() {
        return operatorTransportType;
    }

    public void setOperatorTransportType(String operatorTransportType) {
        this.operatorTransportType = operatorTransportType;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}