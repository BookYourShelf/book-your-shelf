package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CampaignDetailsRepository extends CrudRepository<Campaign, Integer> {
    @Modifying
    @Transactional
    @Query(value = "delete from campaign_categories where campaign_id like ?1", nativeQuery = true)
    void removeAllCampaignCategories(int id);

    @Modifying
    @Transactional
    @Query(value = "delete from campaign_subcategories where campaign_id like ?1", nativeQuery = true)
    void removeAllCampaignSubcategories(int id);

    List<Campaign> getAllByProductType(Category.ProductType productType);
}
