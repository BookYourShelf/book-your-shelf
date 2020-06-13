package com.oak.bookyourshelf.service.profile;

import com.oak.bookyourshelf.model.Address;
import com.oak.bookyourshelf.repository.profile.ProfileAddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfileAddressService {
    final ProfileAddressRepository profileAddressRepository;

    public ProfileAddressService(ProfileAddressRepository profileAddressRepository) {
        this.profileAddressRepository = profileAddressRepository;
    }

    public void delete(int id) {
        profileAddressRepository.deleteById(id);
    }

    public void save(Address address) {
        profileAddressRepository.save(address);
    }

    public Iterable<Address> listAll(){ return profileAddressRepository.findAll();}


}
