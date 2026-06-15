package com.app.projectlogistics.Service;

import com.app.projectlogistics.DataTransferObject.Manager.RQTManagerDTO;
import com.app.projectlogistics.DataTransferObject.Manager.RSPManagerDTO;
import com.app.projectlogistics.DataTransferObject.MessageDTO.ResponseMessageDTO;
import com.app.projectlogistics.Repository.ManagerRepo;
import com.app.projectlogistics.UserDetailServices.CustomizedUserDetails;
import com.app.projectlogistics.ValueObject.AccountVO;
import com.app.projectlogistics.ValueObject.ManagerVO;
import com.app.projectlogistics.ValueObject.OperatorVO;
import org.apache.tomcat.websocket.AuthenticationException;
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

    public ManagerVO dtoToVOConverter(String action, RQTManagerDTO rqtManagerDTO, CustomizedUserDetails userDetails){
        if (action == null || rqtManagerDTO == null || userDetails == null) {
            return new ManagerVO();
        }

        ManagerVO managerVO = new ManagerVO();
        if(action.equalsIgnoreCase("Update")){
            managerVO.setManagerId(rqtManagerDTO.getManagerId());
        }
        managerVO.setManagerName(rqtManagerDTO.getManagerName());
        managerVO.setManagerContactNo(rqtManagerDTO.getManagerContactNo());
        managerVO.setManagerStatus(rqtManagerDTO.getManagerStatus());
        if(action.equalsIgnoreCase("Save")) {
            managerVO.setCreatedAt(LocalDateTime.now());
        }else{
            managerVO.setCreatedAt(rqtManagerDTO.getCreatedAt());
        }
        managerVO.setUpdatedAt(LocalDateTime.now());
        managerVO.setUpdatedBy(userDetails.getEmployeeId());
        return managerVO;
    }

    public RSPManagerDTO voToDTOConverter(ManagerVO managerVO){
        if (managerVO == null) {
            return new RSPManagerDTO();
        }
        RSPManagerDTO rspManagerDTO = new RSPManagerDTO();
        rspManagerDTO.setManagerId(managerVO.getManagerId());
        rspManagerDTO.setManagerName(managerVO.getManagerName());
        rspManagerDTO.setManagerStatus(managerVO.getManagerStatus());
        rspManagerDTO.setManagerContactNo(managerVO.getManagerContactNo());
        rspManagerDTO.setCreatedAt(managerVO.getCreatedAt());
        rspManagerDTO.setUpdatedAt(managerVO.getUpdatedAt());
        rspManagerDTO.setUpdatedBy(managerVO.getUpdatedBy());
        return rspManagerDTO;
    }

    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<String> saveManager(RQTManagerDTO rqtManagerDTO, String username, CustomizedUserDetails userDetails){

        AccountVO savedAccountVO =  authService.saveUser(username);

        if(savedAccountVO != null){

            ManagerVO savingManagerVO = dtoToVOConverter("Save", rqtManagerDTO, userDetails);
            savingManagerVO.setAccountVO(savedAccountVO);

            OperatorVO getOperatorVO = operatorService.internalFetchService(rqtManagerDTO.getOperatorId());
            if(getOperatorVO != null && getOperatorVO.getOperatorId() > 0){
                savingManagerVO.setOperatorVO(getOperatorVO);
            }else{
                return ResponseEntity.status(400).body("Error: Operator ID " + rqtManagerDTO.getOperatorId() + " does not exist in the system.");
            }

            ManagerVO savedManagerVO = managerRepo.save(savingManagerVO);

            if(savedManagerVO.getManagerId() != null && savedManagerVO.getManagerId() > 0){
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
        Page<ManagerVO> page = managerRepo.findByOperatorVO_OperatorId(operatorId, pageable);
        List<ManagerVO> managerVOList = page.getContent();
        List<RSPManagerDTO> rspManagerDTOList = new ArrayList<>();

        for(ManagerVO managerVO:managerVOList){
            rspManagerDTOList.add(voToDTOConverter(managerVO));
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
        ManagerVO mutatedManagerVO = dtoToVOConverter("Update", rqtManagerDTO, userDetails);
        activeManagerOneness(mutatedManagerVO);
        ManagerVO updatedManagerVO = managerRepo.save(mutatedManagerVO);
        return voToDTOConverter(updatedManagerVO);
    }

    @Transactional ( propagation = Propagation.REQUIRED )
    public void activeManagerOneness(ManagerVO mutatedManagerVO){
        if (mutatedManagerVO == null || mutatedManagerVO.getOperatorVO() == null || mutatedManagerVO.getOperatorVO().getOperatorId() == null) {
            return;
        }
        List<ManagerVO> managerVOList = managerRepo.findByOperatorVO_OperatorId(mutatedManagerVO.getOperatorVO().getOperatorId());

        for(ManagerVO managerVO : managerVOList){
            if(managerVO.getManagerId() != null && managerVO.getManagerId().equals(mutatedManagerVO.getManagerId())){
                managerVO.setManagerStatus("ACTIVE");
                mutatedManagerVO.setManagerStatus("ACTIVE");
            }else{
                managerVO.setManagerStatus("IN_ACTIVE");
            }
        }
        managerRepo.saveAll(managerVOList);
    }

    @Transactional(readOnly = true)
    public RSPManagerDTO fetchManager(Integer managerId) {
        ManagerVO fetchManagerVO = managerRepo.findById(managerId).orElse(null);
        return voToDTOConverter(fetchManagerVO);
    }

    @Transactional(readOnly = true)
    public RSPManagerDTO fetchByManagerName(String managerName) {
        if (managerName == null || managerName.trim().isEmpty()) {
            return new RSPManagerDTO();
        }

        ManagerVO fetchManagerVO = managerRepo.findByManagerName(managerName);
        if(fetchManagerVO != null){
            return voToDTOConverter(fetchManagerVO);
        }
        return null;
    }
}
