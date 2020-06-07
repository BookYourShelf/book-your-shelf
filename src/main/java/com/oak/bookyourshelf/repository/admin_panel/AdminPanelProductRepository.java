package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelProductRepository extends CrudRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.barcode = ?1")
    Product findProductByBarcode(String barcode);

    @Query("SELECT p FROM Book p WHERE p.isbn = ?1")
    Product findBookByISBN(String isbn);

    List<Product> findAllByOrderByUploadDateDesc();

    List<Product> findAllByOrderByUploadDateAsc();

    List<Product> findAllByOrderByProductIdDesc();

    List<Product> findAllByOrderByProductIdAsc();

    List<Product> findAllByOrderByProductNameDesc();

    List<Product> findAllByOrderByProductNameAsc();

    List<Product> findAllByOrderByPriceDesc();

    List<Product> findAllByOrderByPriceAsc();

    List<Product> findAllByOrderByBarcodeDesc();

    List<Product> findAllByOrderByBarcodeAsc();

    List<Product> findAllByOrderByStockDesc();

    List<Product> findAllByOrderByStockAsc();

    List<Product> findAllByOrderBySalesNumDesc();
}
