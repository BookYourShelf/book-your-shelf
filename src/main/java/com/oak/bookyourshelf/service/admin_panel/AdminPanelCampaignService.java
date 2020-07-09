package com.oak.bookyourshelf.service.admin_panel;


import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.repository.ProductRepository;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelCampaignRepository;
import com.oak.bookyourshelf.service.ProductService;
import com.oak.bookyourshelf.service.SubcategoryDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class AdminPanelCampaignService {
    final AdminPanelCampaignRepository adminPanelCampaignRepository;
    final ProductRepository productRepository;
    final ProductService productService;
    final SubcategoryDetailsService subcategoryDetailsService;

    public AdminPanelCampaignService(AdminPanelCampaignRepository adminPanelCampaignRepository, ProductRepository productRepository, ProductService productService, SubcategoryDetailsService subcategoryDetailsService) {
        this.adminPanelCampaignRepository = adminPanelCampaignRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.subcategoryDetailsService = subcategoryDetailsService;
    }

    public Iterable<Campaign> listAll() {
        return adminPanelCampaignRepository.findAll();
    }

    public void save(Campaign campaign) {
        adminPanelCampaignRepository.save(campaign);
    }

    public Campaign get(int id) {
        return adminPanelCampaignRepository.findById(id).get();
    }

    public Campaign getByName(String name) {
        return adminPanelCampaignRepository.findCampaignByName(name);
    }

    public List<Campaign> getAllByName(String name) {
        return adminPanelCampaignRepository.findAllByName(name);
    }

    public void delete(int id) {
        adminPanelCampaignRepository.deleteById(id);
    }

    public List<Campaign> findAllByProductType(Category.ProductType type) {
        return adminPanelCampaignRepository.findAllByProductType(type);
    }

    public Set<Book> createProductSet(Set<Subcategory> subcategories) {
        Set<Book> allProducts = new HashSet<>();
        for (Subcategory sub : subcategories) {
            sub.setInCampaign(true);
            allProducts.addAll(sub.getBooks());
            subcategoryDetailsService.save(sub);
        }
        return allProducts;
    }


    public void setProductsRate(Set<Book> allProducts, int rate) {
        for (Product p : allProducts) {
            p.setOnDiscount(true);
            p.setDiscountRate((float) rate / 100);
            productRepository.save(p);
        }

    }

    public void setOtherProductsRate(String typeName, int rate) {
        List<Product> products = new ArrayList<>();
        if (typeName.equals("E_BOOK_READER"))
            products = productService.filterProducts((List<Product>) productRepository.findAll(), "E-Book Reader");
        else if (typeName.equals("E_BOOK_READER_CASE"))
            products = productService.filterProducts((List<Product>) productRepository.findAll(), "E-Book Reader Case");
        else if (typeName.equals("BOOK_CASE"))
            products = productService.filterProducts((List<Product>) productRepository.findAll(), "Book Case");


        for (Product p : products) {
            p.setOnDiscount(true);
            p.setDiscountRate((float) rate / 100);
        }
    }

    public boolean isDateValid(List<String> date) {
        int day = Integer.parseInt(date.get(0));
        int month = Integer.parseInt(date.get(1));
        int year = Integer.parseInt(date.get(2));

        if (!(day > 0 && day < 31))
            return false;
        if (!(month > 0 && month < 13))
            return false;
        if (!(year >= 2020))
            return false;
        return true;
    }

    public boolean isDateCorrect(List<String> endDate, List<String> startDate) {
        LocalDate now = LocalDate.now();
        int yearNow =now.getYear();
        int monthNow =now.getMonthValue();
        int dayNow = now.getDayOfMonth();
        int startDay = Integer.parseInt(startDate.get(0));
        int startMonth = Integer.parseInt(startDate.get(1));
        int startYear = Integer.parseInt(startDate.get(2));

        int endDay = Integer.parseInt(endDate.get(0));
        int endMonth = Integer.parseInt(endDate.get(1));
        int endYear = Integer.parseInt(endDate.get(2));

        if (endYear < startDay)
            return false;
        else if (endYear == startYear) {
            if (endMonth < startMonth)
                return false;
            else if (endMonth == startMonth) {
                if (endDay < startDay)
                    return false;
                else
                    return true;
            } else
                return true;
        } else
            return true;
    }

    public boolean isDateInPast(List<String> startDate)
    {
        LocalDate now = LocalDate.now();
        int yearNow =now.getYear();
        int monthNow =now.getMonthValue();
        int dayNow = now.getDayOfMonth();

        int startDay = Integer.parseInt(startDate.get(0));
        int startMonth = Integer.parseInt(startDate.get(1));
        int startYear = Integer.parseInt(startDate.get(2));

        if(startYear<yearNow)
            return false;
        else if(startYear == yearNow)
        {
            if(startMonth<monthNow)
                return false;
            else if(startMonth == monthNow)
            {
                if(startDay<dayNow)
                    return false;
                else
                    return true;
            }
            else
                return true;
        }
        else return true;

    }

    public List<Campaign> sortCampaigns(String sortType) {
        switch (sortType) {
            case "ID-desc":
                return adminPanelCampaignRepository.findAllByOrderByIdDesc();

            case "ID-asc":
                return adminPanelCampaignRepository.findAllByOrderByIdAsc();

            case "Name-desc":
                return adminPanelCampaignRepository.findAllByOrderByNameDesc();

            case "Name-asc":
                return adminPanelCampaignRepository.findAllByOrderByNameAsc();

            case "Rate-desc":
                return adminPanelCampaignRepository.findAllByOrderByRateDesc();

            case "Rate-asc":
                return adminPanelCampaignRepository.findAllByOrderByRateAsc();

            case "Product-Type-desc":
                return adminPanelCampaignRepository.findAllByOrderByProductTypeDesc();

            case "Product-Type-asc":
                return adminPanelCampaignRepository.findAllByOrderByProductTypeAsc();

            default:        // id
                return adminPanelCampaignRepository.findAllByOrderByIdAsc();


        }

    }

    public String createProductType(Campaign campaign) {
        if (campaign.getProductType() == Category.ProductType.BOOK)
            return "Book";
        else if (campaign.getProductType() == Category.ProductType.AUDIO_BOOK)
            return "Audio Book";
        else if (campaign.getProductType() == Category.ProductType.E_BOOK)
            return "E Book";
        else if (campaign.getProductType() == Category.ProductType.BOOK_CASE)
            return "Book Case";
        else if (campaign.getProductType() == Category.ProductType.E_BOOK_READER_CASE)
            return "E Book Reader Case";
        else
            return "E Book Reader";
    }

}
