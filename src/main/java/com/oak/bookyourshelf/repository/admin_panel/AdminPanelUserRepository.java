package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelUserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.role = 0")
    Iterable<User> findAllCustomers();


    List<User> findAllByOrderByUserIdAsc();

    List<User> findAllByOrderByUserIdDesc();

    List<User> findAllByOrderByNameAsc();

    List<User> findAllByOrderByNameDesc();

    List<User> findAllByOrderBySurnameAsc();

    List<User> findAllByOrderBySurnameDesc();

    List<User> findAllByOrderByEmailAsc();

    List<User> findAllByOrderByEmailDesc();


}
