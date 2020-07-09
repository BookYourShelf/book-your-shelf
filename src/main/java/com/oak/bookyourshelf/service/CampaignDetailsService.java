package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.repository.CampaignDetailsRepository;
import com.oak.bookyourshelf.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import java.util.List;
import java.util.Set;

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

    public List<Campaign> getAllByProductType(Category.ProductType productType) {
        return campaignDetailsRepository.getAllByProductType(productType);
    }

    public Campaign get(int id) {
        if (campaignDetailsRepository.findById(id).isPresent())
            return campaignDetailsRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        campaignDetailsRepository.removeAllCampaignSubcategories(id);
        campaignDetailsRepository.removeAllCampaignCategories(id);

        campaignDetailsRepository.deleteById(id);
    }

    public void deleteReaderOrCase(int id) {

        campaignDetailsRepository.deleteById(id);
    }

    public void save(Campaign user) {
        campaignDetailsRepository.save(user);
    }

    public Set<Subcategory> removeDiscount(Set<Subcategory> subcategories) {

        for (Subcategory subcategory : subcategories) {
            subcategory.setInCampaign(false);
            for (Book b : subcategory.getBooks()) {
                b.setOnDiscount(false);
                b.setDiscountRate(0);
            }
        }
        return subcategories;
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
