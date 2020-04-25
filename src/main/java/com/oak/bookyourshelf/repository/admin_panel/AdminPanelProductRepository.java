package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdminPanelProductRepository extends CrudRepository<Product, Integer> {
    @Query("SELECT t FROM Product t WHERE t.barcode = ?1")
    Product findProductByBarcode(int barcode);

    @Query("SELECT t FROM Book t WHERE t.isbn = ?1")
    Product findBookByISBN(String isbn);
}
