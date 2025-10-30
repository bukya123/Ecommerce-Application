package com.example.ecommercebackend.Services;

import com.example.ecommercebackend.ControllerAdivce.APIException;
import com.example.ecommercebackend.ControllerAdivce.ResourceNotFoundException;
import com.example.ecommercebackend.DTOs.AddressRequestDTO;
import com.example.ecommercebackend.Modules.Address;
import com.example.ecommercebackend.Modules.AppUser;
import com.example.ecommercebackend.Repositories.AddressRepo;
import com.example.ecommercebackend.Repositories.AppUserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImp implements AddressService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AddressRepo addressRepo;

    @Autowired
    AppUserRepo appUserRepo;

    @Override
    public AddressRequestDTO createAddress(AddressRequestDTO addressRequestDTO, AppUser appUser) {
        Address address=modelMapper.map(addressRequestDTO, Address.class);
        address.setAppUser(appUser);

        List<Address> addressList=appUser.getAddresses();
        addressList.add(address);
        appUser.setAddresses(addressList);

        Address address1=addressRepo.save(address);
        AddressRequestDTO addressRequestDTO1=modelMapper.map(address1,AddressRequestDTO.class);
        return addressRequestDTO1;
    }

    @Override
    public List<AddressRequestDTO> getAllAddresses() {
        List<Address> addresses=addressRepo.findAll();
        List<AddressRequestDTO> addressRequestDTOList=new ArrayList<>();
        addresses.forEach(address->{
            AddressRequestDTO addressRequestDTO1=modelMapper.map(address,AddressRequestDTO.class);
            addressRequestDTOList.add(addressRequestDTO1);
        });
        return addressRequestDTOList;
    }

    @Override
    public AddressRequestDTO getAddressById(Long id) {
       Optional<Address> address=addressRepo.findById(id);
       if(!address.isPresent()){
           throw new ResourceNotFoundException("Address","addressId",id);
       }
       AddressRequestDTO addressRequestDTO1=modelMapper.map(address.get(),AddressRequestDTO.class);
       return addressRequestDTO1;

    }

    @Override
    public List<AddressRequestDTO> getAddressByUser(AppUser appUser) {
        List<Address> addressList=appUser.getAddresses();
        List<AddressRequestDTO> addressRequestDTOList=new ArrayList<>();
        addressList.forEach(address->{
            AddressRequestDTO addressRequestDTO1=modelMapper.map(address,AddressRequestDTO.class);
            addressRequestDTOList.add(addressRequestDTO1);
        });
        return addressRequestDTOList;
    }

    @Override
    public AddressRequestDTO updateAddress(Long id,AddressRequestDTO addressRequestDTO) {
        Optional<Address> OptionalAddress=addressRepo.findById(id);
        if(!OptionalAddress.isPresent()){
            throw new ResourceNotFoundException("Address","addressId",id);
        }
        Address address=OptionalAddress.get();
        address.setCity(addressRequestDTO.getCity());
        address.setCountry(addressRequestDTO.getCountry());
        address.setStreet(addressRequestDTO.getStreet());
        address.setPincode(addressRequestDTO.getPincode());
        address.setBuildingName(addressRequestDTO.getBuildingName());
        Address updatedAddress=addressRepo.save(address);

        AppUser appUser=address.getAppUser();
        List<Address> addressList=appUser.getAddresses();
        for(int i=0;i<addressList.size();i++){
            Address address1=addressList.get(i);
            if(address1.getAddressId()==id){
                addressList.remove(address1);
            }
        }

        addressList.add(updatedAddress);
        appUser.setAddresses(addressList);
        appUserRepo.save(appUser);

        AddressRequestDTO addressRequestDTO1=modelMapper.map(address,AddressRequestDTO.class);
        return addressRequestDTO1;

    }

    @Override
    public String deleteAddress(Long id) {
        Optional<Address> address=addressRepo.findById(id);
        if(!address.isPresent()){
            throw new ResourceNotFoundException("Address","addressId",id);
        }
        addressRepo.deleteById(id);
        AppUser appUser=address.get().getAppUser();
        List<Address> addressList=appUser.getAddresses();
        addressList.forEach(address1->{
            if(address1.getAddressId()== id){
                addressList.remove(address1);
            }
        });
        appUser.setAddresses(addressList);
        appUserRepo.save(appUser);
        return "Address with this "+id+" has been deleted";
    }


}
