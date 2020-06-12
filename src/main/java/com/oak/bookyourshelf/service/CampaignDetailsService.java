package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.repository.CampaignDetailsRepository;
import com.oak.bookyourshelf.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CampaignDetailsService {
    final CampaignDetailsRepository campaignDetailsRepository;
    final ProductService productService;
    final ProductRepository productRepository;

    public CampaignDetailsService(CampaignDetailsRepository campaignDetailsRepository, ProductService productService, ProductRepository productRepository) {
        this.campaignDetailsRepository = campaignDetailsRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }


    public Iterable<Campaign> listAll() {
        return campaignDetailsRepository.findAll();
    }

    public Campaign get(int id) {
        if (campaignDetailsRepository.findById(id).isPresent())
            return campaignDetailsRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        campaignDetailsRepository.removeAllCampaignCategories(id);
        campaignDetailsRepository.removeAllCampaignSubcategories(id);
        campaignDetailsRepository.deleteById(id);
    }

    public void save(Campaign user) {
        campaignDetailsRepository.save(user);
    }

    public  void removeDiscount(Campaign campaign)
    {
        List<Subcategory> subcategoryList = campaign.getSubcategories();
        for(Subcategory subcategory : subcategoryList)
        {
            subcategory.setInCampaign(false);
            for(Book b : subcategory.getBooks())
            {
                b.setOnDiscount(false);
                b.setDiscountRate(0);
            }
        }
    }

    public void removeDiscountOtherProducts(Campaign campaign)
    {
        List<Product> products =new ArrayList<>();
        if(campaign.getProductType()== Category.ProductType.E_BOOK_READER)
            products = productService.filterProducts((List<Product>) productRepository.findAll(),"E-Book Reader");
        else if(campaign.getProductType() == Category.ProductType.E_BOOK_READER_CASE)
            products = productService.filterProducts((List<Product>) productRepository.findAll(),"E-Book Reader Case");
        else if(campaign.getProductType() == Category.ProductType.BOOK_CASE)
            products = productService.filterProducts((List<Product>) productRepository.findAll(),"Book Case");

        for(Product p: products)
        {
            p.setDiscountRate(0);
            p.setOnDiscount(false);
        }

    }
}
