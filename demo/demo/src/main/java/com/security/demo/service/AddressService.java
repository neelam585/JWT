package com.security.demo.service;

import com.security.demo.model.User;
import com.security.demo.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User userData);
    AddressDTO deleteAddress(Long addressId);
    AddressDTO updateAddress(AddressDTO addressDTO,Long addressId);
    AddressDTO getAddressById(Long addressId);
    List<AddressDTO> getUserAddresses(User userData);
}
