package com.oak.bookyourshelf.service.user_details;


import com.oak.bookyourshelf.model.Address;
import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.user_details.UserDetailsAddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserDetailsAddressService {
    final UserDetailsAddressRepository userDetailsAddressRepository;

    public UserDetailsAddressService(UserDetailsAddressRepository userDetailsAddressRepository) {
        this.userDetailsAddressRepository = userDetailsAddressRepository;
    }

     public Iterable<Address> listAll() {return userDetailsAddressRepository.findAll();}

    public Address get(int id) {
        return userDetailsAddressRepository.findById(id).get();
    }

    public void delete(int id) {
        userDetailsAddressRepository.deleteById(id);
    }

    public void save(Address address) {
        userDetailsAddressRepository.save(address);
    }

   // public List<Integer> findAllIds() {return userDetailsAddressRepository.findAllIds();}

}
