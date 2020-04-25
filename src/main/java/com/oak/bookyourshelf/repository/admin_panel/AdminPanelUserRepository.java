package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;



public interface AdminPanelUserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.role = 0")
    Iterable<User> findAllCustomers();
}
