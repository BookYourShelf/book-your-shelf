package com.oak.bookyourshelf.repository.user_details;

import com.oak.bookyourshelf.model.Address;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDetailsAddressRepository extends CrudRepository<Address, Integer> {

    @Query("SELECT a FROM Address a WHERE a.addressId = ?1")
    Address findAddressById(int id);

}
