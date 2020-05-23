package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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

    public Page<Product> findPaginated(Pageable pageable) {
        List products = (List) adminPanelProductRepository.findAllProducts();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Product> list;

        if (products.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, products.size());
            list = products.subList(startItem, toIndex);
        }

        Page<Product> productPage
                = new PageImpl<Product>(list, PageRequest.of(currentPage, pageSize), products.size());

        return productPage;
    }
}
