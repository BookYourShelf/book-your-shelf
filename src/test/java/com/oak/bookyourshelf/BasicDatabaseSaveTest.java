package com.oak.bookyourshelf;

import com.oak.bookyourshelf.model.ElectronicBook;
import com.oak.bookyourshelf.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;

@SpringBootTest
class BasicDatabaseSaveTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ElectronicBookRepository electronicBookRepository;

    @Test
    void contextLoads() {
        /* Create new user and set name to test */
        User user = new User();
        user.setName("test");
        /* Save user to database */
        userRepository.save(user);

        /* Find user on database with same id */
        User foundUser = userRepository
                .findById(user.getUserId()).orElse(null);

        Assert.notNull(user, "User can't be null");

        /* Check the names */
        assert foundUser != null;
        assert user.getName().equals(foundUser.getName());

        ElectronicBook electronicBook = new ElectronicBook();
        electronicBook.setPrice(50.0f);
        electronicBook.setIsbn("hede");

        electronicBookRepository.save(electronicBook);

        ElectronicBook foundElectronicBook = electronicBookRepository
                .findById(electronicBook.getId()).orElse(null);

        Assert.notNull(electronicBook, "ElectronicBook can't be null");

        /* Check the names */
        assert foundElectronicBook != null;
        assert electronicBook.getPrice() == foundElectronicBook.getPrice();
        assert electronicBook.getIsbn().equals(foundElectronicBook.getIsbn());


    }

}
