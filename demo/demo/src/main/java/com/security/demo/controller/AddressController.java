package com.security.demo.controller;

import com.security.demo.model.User;
import com.security.demo.payload.AddressDTO;
import com.security.demo.service.AddressService;
import com.security.demo.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/public/address")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO){
        User userData = authUtil.getLoggdInUser();
        AddressDTO savedAddress = addressService.createAddress(addressDTO, userData);
        return ResponseEntity.ok(savedAddress);
    }

    @GetMapping("/public/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO address = addressService.getAddressById(addressId);
        return ResponseEntity.ok(address);
    }

    @DeleteMapping("public/address/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddress(@PathVariable Long addressId){
        AddressDTO deletedAddress = addressService.deleteAddress(addressId);
        return ResponseEntity.ok(deletedAddress);
    }

    @PutMapping("/public/address/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,@RequestBody AddressDTO addressDTO){
        AddressDTO updatedAddress = addressService.updateAddress(addressDTO, addressId);
        return ResponseEntity.ok(updatedAddress);
    }

    @GetMapping("/public/address")
    public ResponseEntity<List<AddressDTO>> getUserAddress(){
        User userData = authUtil.getLoggdInUser();
        List<AddressDTO> getAllAddress = addressService.getUserAddresses(userData);
        return ResponseEntity.ok(getAllAddress);
    }
}
