package com.example.ecommercebackend.Controllers;


import com.example.ecommercebackend.DTOs.AddressRequestDTO;
import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Services.AddressService;
import com.example.ecommercebackend.Util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/addresses")
    public ResponseEntity<AddressRequestDTO> createAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        AppUser appUser=authUtil.loggedInUser();
       AddressRequestDTO addressRequestDTO1= addressService.createAddress(addressRequestDTO,appUser);
       return new  ResponseEntity<>(addressRequestDTO1, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressRequestDTO>> getAllAddresses() {
        List<AddressRequestDTO> addressRequestDTO=addressService.getAllAddresses();
        return new ResponseEntity<>(addressRequestDTO, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressesId}")
    public ResponseEntity<AddressRequestDTO> getAddressById(@PathVariable Long addressesId) {
        AddressRequestDTO addressRequestDTO=addressService.getAddressById(addressesId);
        return new ResponseEntity<>(addressRequestDTO, HttpStatus.OK);
    }
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressRequestDTO>> getAddressByUser(){
        AppUser appUser=authUtil.loggedInUser();
        List<AddressRequestDTO> addressRequestDTO=addressService.getAddressByUser(appUser);
        return new ResponseEntity<>(addressRequestDTO, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressesId}")
    public ResponseEntity<AddressRequestDTO> updateAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO,@PathVariable Long addressesId) {
        AddressRequestDTO addressRequestDTO1=addressService.updateAddress(addressesId,addressRequestDTO);
        return new ResponseEntity<>(addressRequestDTO1, HttpStatus.OK);
    }
    @DeleteMapping("/addresses/{addressesId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressesId) {
        String s=addressService.deleteAddress(addressesId);
        return new ResponseEntity<>(s,HttpStatus.OK);
    }
}
