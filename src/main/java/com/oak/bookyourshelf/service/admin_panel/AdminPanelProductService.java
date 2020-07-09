package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminPanelProductService {

    public final AdminPanelProductRepository adminPanelProductRepository;

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

    public List<Product> sortProducts(String sortType) {
        switch (sortType) {
            case "date-asc":
                return adminPanelProductRepository.findAllByOrderByUploadDateAsc();

            case "ID-desc":
                return adminPanelProductRepository.findAllByOrderByProductIdDesc();

            case "ID-asc":
                return adminPanelProductRepository.findAllByOrderByProductIdAsc();

            case "name-desc":
                return adminPanelProductRepository.findAllByOrderByProductNameDesc();

            case "name-asc":
                return adminPanelProductRepository.findAllByOrderByProductNameAsc();

            case "price-desc":
                return adminPanelProductRepository.findAllByOrderByPriceDesc();

            case "price-asc":
                return adminPanelProductRepository.findAllByOrderByPriceAsc();

            case "barcode-desc":
                return adminPanelProductRepository.findAllByOrderByBarcodeDesc();

            case "barcode-asc":
                return adminPanelProductRepository.findAllByOrderByBarcodeAsc();

            case "stock-desc":
                return adminPanelProductRepository.findAllByOrderByStockDesc();

            case "stock-asc":
                return adminPanelProductRepository.findAllByOrderByStockAsc();

            default:        // date-desc
                return adminPanelProductRepository.findAllByOrderByUploadDateDesc();
        }
    }
}
