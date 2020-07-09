package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelCampaignRepository extends CrudRepository<Campaign, Integer> {
    @Query("SELECT t FROM Campaign t WHERE t.name = ?1")
    Campaign findCampaignByName(String name);

    @Query("SELECT t from Campaign t where t.name = ?1")
    List<Campaign> findAllByName(String name);

    @Query("SELECT t from Campaign t where t.productType = ?1")
    List<Campaign> findAllByProductType(Category.ProductType name);

    List<Campaign> findAllByOrderByIdAsc();

    List<Campaign> findAllByOrderByIdDesc();

    List<Campaign> findAllByOrderByRateAsc();

    List<Campaign> findAllByOrderByRateDesc();

    List<Campaign> findAllByOrderByNameAsc();

    List<Campaign> findAllByOrderByNameDesc();

    List<Campaign> findAllByOrderByProductTypeAsc();

    List<Campaign> findAllByOrderByProductTypeDesc();


}
