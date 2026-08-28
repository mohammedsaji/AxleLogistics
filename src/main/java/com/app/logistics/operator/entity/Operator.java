package com.app.logistics.operator.entity;

import com.app.logistics.common.validations.OnCreate;
import com.app.logistics.common.validations.OnUpdate;
import com.app.logistics.driver.entity.Driver;
import com.app.logistics.vehicle.entity.Vehicle;
import com.app.logistics.manager.entity.Manager;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="OPRTR_MST")
public class Operator {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    @Column(name="OPRTR_ID")
    private Integer operatorId;

    @Size(max=255)
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Column(name="OPRTR_NAME")
    private String operatorName;

    @Size(max=20)
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "AIR|SEA|ROAD|RAIL", groups = {OnCreate.class, OnUpdate.class})
    @Column(name="OPRTR_TRANSPORT_TYPE")
    private String operatorTransportType;


    @OneToMany(mappedBy = "operatorVO", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Manager> managerList;

    public void addManagerVO(Manager manager){
        managerList.add(manager);
    }

    public void removeManagerVO(Manager manager){
        managerList.remove(manager);
    }

    public List<Manager> getManagerVOList(){
        return managerList;
    }

    @Column(name="CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name="UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name="UPDATED_BY")
    @Max(value=Integer.MAX_VALUE, groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    private Integer updatedBy;

    @OneToMany(mappedBy = "operatorVO", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Driver> driverList = new ArrayList<>();

    public void addDriverVO(Driver driver){
        driverList.add(driver);
        driver.setOperatorVO(this);
    }

    public void removeDriverVO(Driver driver){
        driverList.remove(driver);
        driver.setOperatorVO(null);
    }

    public List<Driver> getDriverVOList() {
        return driverList;
    }

    @OneToMany(mappedBy = "operatorVO", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    public List<Vehicle> vehicleVOList = new ArrayList<>();

    public void addVehicleVO(Vehicle vehicleVO){
        vehicleVOList.add(vehicleVO);
        vehicleVO.setshippingOperatorVO(this);
    }

    public void removeVehicleVO(Vehicle vehicleVO){
        vehicleVOList.remove(vehicleVO);
        vehicleVO.setshippingOperatorVO(null);
    }

    public List<Vehicle> getVehicleVOList() {
        return vehicleVOList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}