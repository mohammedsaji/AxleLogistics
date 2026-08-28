package com.app.logistics.manager.service;

import com.app.logistics.auth.service.AuthService;
import com.app.logistics.dto.Manager.RQTManagerDTO;
import com.app.logistics.dto.Manager.RSPManagerDTO;
import com.app.logistics.dto.MessageDTO.ResponseMessageDTO;
import com.app.logistics.manager.repo.ManagerRepo;
import com.app.logistics.authUtils.CustomizedUserDetails;
import com.app.logistics.auth.entity.Auth;
import com.app.logistics.manager.entity.Manager;
import com.app.logistics.operator.entity.Operator;
import com.app.logistics.operator.service.OperatorService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerService {

    private final ManagerRepo managerRepo;

    private final OperatorService operatorService;

    private final AuthService authService;

    public ManagerService(ManagerRepo managerRepo,
                          @Lazy OperatorService operatorService,
                          @Lazy AuthService authService){
        this.managerRepo = managerRepo;
        this.operatorService = operatorService;
        this.authService = authService;
    }

    public Manager dtoToVOConverter(String action, RQTManagerDTO rqtManagerDTO, CustomizedUserDetails userDetails){
        if (action == null || rqtManagerDTO == null || userDetails == null) {
            return new Manager();
        }

        Manager manager = new Manager();
        if(action.equalsIgnoreCase("Update")){
            manager.setManagerId(rqtManagerDTO.getManagerId());
        }
        manager.setManagerName(rqtManagerDTO.getManagerName());
        manager.setManagerContactNo(rqtManagerDTO.getManagerContactNo());
        manager.setManagerStatus(rqtManagerDTO.getManagerStatus());
        if(action.equalsIgnoreCase("Save")) {
            manager.setCreatedAt(LocalDateTime.now());
        }else{
            manager.setCreatedAt(rqtManagerDTO.getCreatedAt());
        }
        manager.setUpdatedAt(LocalDateTime.now());
        manager.setUpdatedBy(userDetails.getEmployeeId());
        return manager;
    }

    public RSPManagerDTO voToDTOConverter(Manager manager){
        if (manager == null) {
            return new RSPManagerDTO();
        }
        RSPManagerDTO rspManagerDTO = new RSPManagerDTO();
        rspManagerDTO.setManagerId(manager.getManagerId());
        rspManagerDTO.setManagerName(manager.getManagerName());
        rspManagerDTO.setManagerStatus(manager.getManagerStatus());
        rspManagerDTO.setManagerContactNo(manager.getManagerContactNo());
        rspManagerDTO.setCreatedAt(manager.getCreatedAt());
        rspManagerDTO.setUpdatedAt(manager.getUpdatedAt());
        rspManagerDTO.setUpdatedBy(manager.getUpdatedBy());
        return rspManagerDTO;
    }

    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<String> saveManager(RQTManagerDTO rqtManagerDTO, String username, CustomizedUserDetails userDetails){

        Auth savedAuth =  authService.saveUser(username);

        if(savedAuth != null){

            Manager savingManager = dtoToVOConverter("Save", rqtManagerDTO, userDetails);
            savingManager.setAccountVO(savedAuth);

            Operator getOperator = operatorService.internalFetchService(rqtManagerDTO.getOperatorId());
            if(getOperator != null && getOperator.getOperatorId() > 0){
                savingManager.setOperatorVO(getOperator);
            }else{
                return ResponseEntity.status(400).body("Error: Operator ID " + rqtManagerDTO.getOperatorId() + " does not exist in the system.");
            }

            Manager savedManager = managerRepo.save(savingManager);

            if(savedManager.getManagerId() != null && savedManager.getManagerId() > 0){
                return ResponseEntity.status(200).body("Account created Successfully.");
            }else{
                return ResponseEntity.status(400).body("Invalid or Bad Request");
            }
        }

        return ResponseEntity.status(500).body("Internal server error");
    }

    @Transactional(readOnly = true)
    public ResponseMessageDTO fetchAllManager(Integer operatorId, int pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int elementCount = 10;
        Pageable pageable = PageRequest.of(pageNo-1,elementCount,Sort.by("managerId"));
        Page<Manager> page = managerRepo.findByOperatorVO_OperatorId(operatorId, pageable);
        List<Manager> managerList = page.getContent();
        List<RSPManagerDTO> rspManagerDTOList = new ArrayList<>();

        for(Manager manager : managerList){
            rspManagerDTOList.add(voToDTOConverter(manager));
        }
        ResponseMessageDTO responseMessageDTO = new ResponseMessageDTO();
        responseMessageDTO.setStatusCode(200);
        responseMessageDTO.setValue("ManagerList",rspManagerDTOList);
        responseMessageDTO.setValue("TotalPages",page.getTotalPages());
        responseMessageDTO.setValue("TotalElements",page.getTotalElements());
        return responseMessageDTO;
    }

    public ResponseEntity<String> deleteManager(RQTManagerDTO rqtManagerDTO) {
        if (rqtManagerDTO == null || rqtManagerDTO.getManagerId() == null) {
            return ResponseEntity.status(400).body("Manager ID parameter cannot be null");
        }
        managerRepo.deleteById(rqtManagerDTO.getManagerId());
        return ResponseEntity.status(200).body("Manager deleted successfully.");
    }

    public RSPManagerDTO updateManager(RQTManagerDTO rqtManagerDTO, CustomizedUserDetails userDetails) {
        if (rqtManagerDTO == null) {
            return new RSPManagerDTO();
        }
        Manager mutatedManager = dtoToVOConverter("Update", rqtManagerDTO, userDetails);
        activeManagerOneness(mutatedManager);
        Manager updatedManager = managerRepo.save(mutatedManager);
        return voToDTOConverter(updatedManager);
    }

    @Transactional ( propagation = Propagation.REQUIRED )
    public void activeManagerOneness(Manager mutatedManager){
        if (mutatedManager == null || mutatedManager.getOperatorVO() == null || mutatedManager.getOperatorVO().getOperatorId() == null) {
            return;
        }
        List<Manager> managerList = managerRepo.findByOperatorVO_OperatorId(mutatedManager.getOperatorVO().getOperatorId());

        for(Manager manager : managerList){
            if(manager.getManagerId() != null && manager.getManagerId().equals(mutatedManager.getManagerId())){
                manager.setManagerStatus("ACTIVE");
                mutatedManager.setManagerStatus("ACTIVE");
            }else{
                manager.setManagerStatus("IN_ACTIVE");
            }
        }
        managerRepo.saveAll(managerList);
    }

    @Transactional(readOnly = true)
    public RSPManagerDTO fetchManager(Integer managerId) {
        Manager fetchManager = managerRepo.findById(managerId).orElse(null);
        return voToDTOConverter(fetchManager);
    }

    @Transactional(readOnly = true)
    public RSPManagerDTO fetchByManagerName(String managerName) {
        if (managerName == null || managerName.trim().isEmpty()) {
            return new RSPManagerDTO();
        }

        Manager fetchManager = managerRepo.findByManagerName(managerName);
        if(fetchManager != null){
            return voToDTOConverter(fetchManager);
        }
        return null;
    }
}
