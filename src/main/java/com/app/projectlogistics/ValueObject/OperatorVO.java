package com.app.projectlogistics.ValueObject;

import com.app.projectlogistics.validationInterface.OnCreate;
import com.app.projectlogistics.validationInterface.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="OPRTR_MST")
public class OperatorVO {

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
    private List<ManagerVO> managerVOList;

    public void addManagerVO(ManagerVO managerVO){
        managerVOList.add(managerVO);
    }

    public void removeManagerVO(ManagerVO managerVO){
        managerVOList.remove(managerVO);
    }

    public List<ManagerVO> getManagerVOList(){
        return managerVOList;
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
    private List<DriverVO> driverVOList = new ArrayList<>();

    public void addDriverVO(DriverVO driverVO){
        driverVOList.add(driverVO);
        driverVO.setOperatorVO(this);
    }

    public void removeDriverVO(DriverVO driverVO){
        driverVOList.remove(driverVO);
        driverVO.setOperatorVO(null);
    }

    public List<DriverVO> getDriverVOList() {
        return driverVOList;
    }

    @OneToMany(mappedBy = "operatorVO", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    public List<VehicleVO> vehicleVOList = new ArrayList<>();

    public void addVehicleVO(VehicleVO vehicleVO){
        vehicleVOList.add(vehicleVO);
        vehicleVO.setshippingOperatorVO(this);
    }

    public void removeVehicleVO(VehicleVO vehicleVO){
        vehicleVOList.remove(vehicleVO);
        vehicleVO.setshippingOperatorVO(null);
    }

    public List<VehicleVO> getVehicleVOList() {
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