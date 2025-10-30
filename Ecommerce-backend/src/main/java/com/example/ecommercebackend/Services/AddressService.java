package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.DTOs.AddressRequestDTO;
import com.example.ecommercebackend.Modules.Address;
import com.example.ecommercebackend.Modules.AppUser;

import java.util.List;

public interface AddressService {
    public AddressRequestDTO createAddress(AddressRequestDTO addressRequestDTO, AppUser appUser);
    public List<AddressRequestDTO> getAllAddresses();
    public AddressRequestDTO getAddressById(Long id);
    public List<AddressRequestDTO> getAddressByUser(AppUser appUser);
    public AddressRequestDTO updateAddress(Long id, AddressRequestDTO addressRequestDTO);
    public String deleteAddress(Long id);
}
