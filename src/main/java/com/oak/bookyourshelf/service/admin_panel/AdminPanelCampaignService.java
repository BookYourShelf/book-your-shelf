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

    public boolean isDateValid(List<String> date)
    {
        int day = Integer.parseInt(date.get(0));
        int month = Integer.parseInt(date.get(1));
        int year = Integer.parseInt(date.get(2));

        if(!(day >0 && day <31))
            return false;
        if(!(month>0 && month<13))
            return false;
        if(!(year>=2020))
            return false;
        return true;
    }

    public boolean isDateCorrect(List<String> endDate , List<String> startDate)
    {
        int startDay = Integer.parseInt(startDate.get(0));
        int startMonth = Integer.parseInt(startDate.get(1));
        int startYear = Integer.parseInt(startDate.get(2));

        int endDay = Integer.parseInt(endDate.get(0));
        int endMonth = Integer.parseInt(endDate.get(1));
        int endYear = Integer.parseInt(endDate.get(2));

        if(endYear<startDay)
            return false;
        else if(endYear == startYear)
        {
            if(endMonth<startMonth)
                return false;
            else if(endMonth == startMonth)
            {
                if(endDay<startDay)
                    return false;
                else
                    return true;
            }
            else
                return true;
        }
        else
            return true;
    }

    public List<Campaign> sortCampaigns(String sortType)
    {
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

}
