package com.oak.bookyourshelf.repository.user_details;

import com.oak.bookyourshelf.model.Address;
import com.oak.bookyourshelf.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDetailsAddressRepository extends CrudRepository<Address, Integer>{
  //  @Query("SELECT t FROM User t WHERE t.email = ?1")
  //  User findUserByEmail(String email);

 //  @Query("SELECT userId FROM User  WHERE role =0")
 //   List<Integer> findAllIds();

}
