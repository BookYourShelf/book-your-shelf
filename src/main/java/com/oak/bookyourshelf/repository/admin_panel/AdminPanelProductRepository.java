package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdminPanelProductRepository extends CrudRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.barcode = ?1")
    Product findProductByBarcode(String barcode);

    @Query("SELECT p FROM Book p WHERE p.isbn = ?1")
    Product findBookByISBN(String isbn);

    @Query("SELECT p FROM Product p")
    Iterable<Product> findAllProducts();
}
