package com.security.demo.service;

import com.security.demo.model.Address;
import com.security.demo.model.User;
import com.security.demo.payload.AddressDTO;
import com.security.demo.repository.AddressRepository;
import com.security.demo.repository.UserRepository;
import com.security.demo.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private  AddressRepository addressRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
     UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO,User userData) {

        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(userData);
        List<Address> addresses = userData.getAddresses();
        addresses.add(address);
        userData.setAddresses(addresses);
        Address SaveAddress = addressRepository.save(address);
        return modelMapper.map(SaveAddress, AddressDTO.class);
    }

    @Override
    public AddressDTO deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id:"+addressId));
        if(address == null){
            throw new RuntimeException("Address not found with id:"+addressId);
        }
            addressRepository.delete(address);
            return modelMapper.map(address, AddressDTO.class);
    }


    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id:"+addressId));
        if(address == null){
            throw new RuntimeException("Address not found with id:"+addressId);
        }
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPincode(addressDTO.getPincode());
        address.setCountry(addressDTO.getCountry());
        address.setPhone(addressDTO.getPhone());
        Address updateAddress = addressRepository.save(address);

        User userData = address.getUser();
        userData.getAddresses().removeIf(address1 -> address1.getId().equals(addressId));
        userData.getAddresses().add(updateAddress);
        userRepository.save(userData);
        return modelMapper.map(updateAddress, AddressDTO.class);
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id:"+addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User userData) {
        List<Address> addresses = userData.getAddresses();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }
}
