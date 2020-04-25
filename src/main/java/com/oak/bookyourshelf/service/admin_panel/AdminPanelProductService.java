package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminPanelProductService {

    final
    AdminPanelProductRepository adminPanelProductRepository;

    public AdminPanelProductService(AdminPanelProductRepository adminPanelProductRepository) {
        this.adminPanelProductRepository = adminPanelProductRepository;
    }

    public void save(Product product) {
        adminPanelProductRepository.save(product);
    }

    public Product getByBarcode(int barcode) {
        return adminPanelProductRepository.findProductByBarcode(barcode);
    }

    public Product getByISBN(String isbn) {
        return adminPanelProductRepository.findBookByISBN(isbn);
    }
}
