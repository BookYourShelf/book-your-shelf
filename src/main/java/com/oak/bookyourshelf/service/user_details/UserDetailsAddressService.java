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

    public Address findAddressById(int id) { return userDetailsAddressRepository.findAddressById(id); }

    public void delete(int id) {
        userDetailsAddressRepository.deleteById(id);
    }

    public void save(Address address) {
        userDetailsAddressRepository.save(address);
    }

}
