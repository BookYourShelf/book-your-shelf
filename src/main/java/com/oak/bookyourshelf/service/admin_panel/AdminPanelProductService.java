package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelProductRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminPanelProductService {

    final AdminPanelProductRepository adminPanelProductRepository;

    public AdminPanelProductService(AdminPanelProductRepository adminPanelProductRepository) {
        this.adminPanelProductRepository = adminPanelProductRepository;
    }

    public Iterable<Product> listAll() {
        return adminPanelProductRepository.findAll();
    }

    public void save(Product product) {
        adminPanelProductRepository.save(product);
    }

    public Product getByBarcode(String barcode) {
        return adminPanelProductRepository.findProductByBarcode(barcode);
    }

    public Product getByISBN(String isbn) {
        return adminPanelProductRepository.findBookByISBN(isbn);
    }
}
