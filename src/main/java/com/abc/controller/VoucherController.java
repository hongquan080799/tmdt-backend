package com.abc.controller;

import com.abc.entity.Donhang;
import com.abc.entity.Khachhang;
import com.abc.entity.Voucher;
import com.abc.repository.DonhangRepository;
import com.abc.repository.KhachhangRepository;
import com.abc.repository.VoucherRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class VoucherController {
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    DonhangRepository donhangRepository;
    @Autowired
    KhachhangRepository khachhangRepository;

    @GetMapping("/voucher")
    public List<Voucher> getVoucherList(
            @RequestParam(name = "sortType", required = false) String sortType,
            @RequestParam(name = "sortField", required = false) String sortField
    ){
        if(sortField != null  && sortType != null){
            Sort sort = Sort.by(sortType.equalsIgnoreCase("asc")? Sort.Direction.ASC: Sort.Direction.DESC, sortField);
            return voucherRepository.findAll(sort);
        }
        return voucherRepository.findAll();
    }

    @GetMapping("/checkVoucher")
    public ResponseEntity<?> checkVoucher(@RequestParam("voucherId") String voucherId, Principal principal){
        String username = principal.getName();
        Khachhang khachhang = khachhangRepository.getKhachhangByUsername(username);
        List<Donhang> listDH = donhangRepository.findAllByKhachhang(khachhang);
        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        if(!voucherOptional.isPresent())
            return new ResponseEntity<>(new JSONObject().put("message", "Invalid voucher id").toMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        Voucher voucher = voucherOptional.get();
        for(Donhang dh : listDH){
            if(dh.getVoucher() != null)
                if(dh.getVoucher().getId().equalsIgnoreCase(voucherId))
                    return new ResponseEntity<>(new JSONObject().put("message", "Invalid voucher id").toMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Long currentDate = System.currentTimeMillis();
        if(voucher.getStartDate().getTime() <= currentDate && voucher.getEndDate().getTime() >= currentDate){
            return new ResponseEntity<>(new JSONObject().put("message", "Voucher is okay !")
                    .put("discount" ,voucher.getDiscount())
                    .toMap(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new JSONObject().put("message", "Invalid voucher id, voucher is outdated !").toMap(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @PostMapping("/voucher")
    public ResponseEntity<?> insertVoucher(@RequestBody @Validated Voucher voucher){
        String idVoucher = "VC" + System.currentTimeMillis();
        voucher.setId(idVoucher);
        try {
            voucherRepository.save(voucher);
            return new ResponseEntity<>(new JSONObject().put("Mes   sage", "Insert voucher successfully !!!").toMap(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new JSONObject().put("Message", "Insert voucher failed !!!").toMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/voucher")
    public ResponseEntity<?> deleteVoucher(@RequestParam("voucherId") String voucherId){
        try {
            voucherRepository.deleteById(voucherId);
            return new ResponseEntity<>(new JSONObject().put("Message", "Delete voucher successfully !!!").toMap(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new JSONObject().put("Message", "Delete voucher failed !!!").toMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
