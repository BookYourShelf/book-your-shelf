package com.oak.bookyourshelf.service.admin_panel;


import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelCampaignRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AdminPanelCampaignService {
    final AdminPanelCampaignRepository adminPanelCampaignRepository;

    public AdminPanelCampaignService(AdminPanelCampaignRepository adminPanelCampaignRepository) {
        this.adminPanelCampaignRepository = adminPanelCampaignRepository;
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

    public Set<Product> createProductSet(List<Subcategory> subcategories)
    {
        Set<Product> allProducts = Collections.emptySet();
        for(Subcategory sub :subcategories)
        {
            for(Product p:sub.getBooks())
            {
                allProducts.add(p);
            }
        }
        return allProducts;
    }


    public void setProductsRate(Set<Product> allProducts,int rate)
    {
        for(Product p :allProducts)
        {
            p.setOnDiscount(true);
            p.setDiscountRate((float)rate);
        }

    }

}
