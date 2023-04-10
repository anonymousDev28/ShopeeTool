package com.techmaster.shopeetool.controller;

import com.techmaster.shopeetool.dto.ProductDTO;
import com.techmaster.shopeetool.service.ChromeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.security.RolesAllowed;

@RestController
public class ConfirmController {
    @Autowired
    ChromeService chromeService;
    @PostMapping("api/products")
    @RolesAllowed({"ROLE_EDITOR","ROLE_CUSTOMER"})
    public ResponseEntity<?> postProduct(@RequestBody ProductDTO productDTO){
        try {
            return ResponseEntity.ok(chromeService.startFindProduct(productDTO));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
//    @GetMapping("api/products")
//    public ResponseEntity<?> getResponse(){
//        try {
//            return ResponseEntity.ok(chromeService.responseProduct());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
