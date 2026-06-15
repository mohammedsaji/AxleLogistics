package com.app.projectlogistics.Service;

import com.app.projectlogistics.DataTransferObject.Customer.RQTCustomerDTO;
import com.app.projectlogistics.DataTransferObject.Customer.RSPCustomerDTO;
import com.app.projectlogistics.Repository.CustomerRepo;
import com.app.projectlogistics.ValueObject.CustomerVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerVO saveCustomer(RQTCustomerDTO rqtCustomerDTO) {

        if (rqtCustomerDTO == null) {
            throw new IllegalArgumentException("Customer request data payload cannot be null");
        }

        CustomerVO savingCustomerVO = dtoToVOConverter(rqtCustomerDTO);
        CustomerVO savedCustomerVO = customerRepo.save(savingCustomerVO);
        return savedCustomerVO;
    }

    public CustomerVO dtoToVOConverter(RQTCustomerDTO rqtCustomerDTO) {

        if (rqtCustomerDTO == null) {
            return new CustomerVO();
        }

        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerId(rqtCustomerDTO.getCustomerId());
        customerVO.setCustomerName(rqtCustomerDTO.getCustomerName());
        customerVO.setCustomerEmail(rqtCustomerDTO.getCustomerEmail());
        customerVO.setCustomerPhoneno(rqtCustomerDTO.getCustomerPhoneno());
        customerVO.setCreatedAt(rqtCustomerDTO.getCreatedAt());
        customerVO.setCreatedBy(rqtCustomerDTO.getCreatedBy());

        return customerVO;
    }

    public RSPCustomerDTO voToDTOConverter(CustomerVO customerVO) {

        if (customerVO == null) {
            return new RSPCustomerDTO();
        }

        RSPCustomerDTO rspCustomerDTO = new RSPCustomerDTO();
        rspCustomerDTO.setCustomerId(customerVO.getCustomerId());
        rspCustomerDTO.setCustomerName(customerVO.getCustomerName());
        rspCustomerDTO.setCustomerEmail(customerVO.getCustomerEmail());
        rspCustomerDTO.setCustomerPhoneno(customerVO.getCustomerPhoneno());
        rspCustomerDTO.setCreatedAt(customerVO.getCreatedAt());
        rspCustomerDTO.setCreatedBy(customerVO.getCreatedBy());
        return rspCustomerDTO;
    }

    @Transactional(readOnly = true)
    public RSPCustomerDTO fetchCustomer(Integer customerId) {

        if (customerId == null) {
            return new RSPCustomerDTO();
        }

        CustomerVO fetchCustomerVO = customerRepo.findById(customerId).orElse(new CustomerVO());
        return voToDTOConverter(fetchCustomerVO);
    }

//    @Transactional(readOnly = true)
//    public List<RSPCustomerDTO> fetchAllCustomer(int pageNo) {
//
//        if (pageNo < 1) {
//            pageNo = 1;
//        }
//
//        int elementCount = 25;
//        Pageable pageable = PageRequest.of(pageNo - 1, elementCount, Sort.by("customerId").ascending());
//        Page<CustomerVO> page = customerRepo.findAll(pageable);
//        List<CustomerVO> customerVOList = page.getContent();
//        List<RSPCustomerDTO> rspCustomerDTOList = new ArrayList<>();
//        for (CustomerVO customerVO : customerVOList) {
//            rspCustomerDTOList.add(voToDTOConverter(customerVO));
//        }
//        return rspCustomerDTOList;
//    }

//    public RSPCustomerDTO findCustomerByName(String customerName) {
//
//        if (customerName == null || customerName.trim().isEmpty()) {
//            return new RSPCustomerDTO();
//        }
//
//        CustomerVO getCustomerVO = customerRepo.findByCustomerName(customerName);
//        return voToDTOConverter(getCustomerVO);
//    }

}
