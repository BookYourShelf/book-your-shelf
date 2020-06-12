package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.repository.CampaignDetailsRepository;
import com.oak.bookyourshelf.repository.CategoryDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CampaignDetailsService {
    final CampaignDetailsRepository campaignDetailsRepository;

    public CampaignDetailsService(CampaignDetailsRepository campaignDetailsRepository) {
        this.campaignDetailsRepository = campaignDetailsRepository;
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
        campaignDetailsRepository.removeAllCampaignCategories(id);
        campaignDetailsRepository.removeAllCampaignSubcategories(id);
        campaignDetailsRepository.deleteById(id);
    }

    public void save(Campaign user) {
        campaignDetailsRepository.save(user);
    }
}
