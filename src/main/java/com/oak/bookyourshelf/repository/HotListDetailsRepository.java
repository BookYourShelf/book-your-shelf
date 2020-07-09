package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.HotList;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HotListDetailsRepository extends CrudRepository<HotList, Integer> {
    @Modifying
    @Transactional
    @Query(value = "delete from hot_list_categories where hot_list_id like ?1", nativeQuery = true)
    void removeAllHotListCategories(int id);

    @Modifying
    @Transactional
    @Query(value = "delete from hot_list_subcategories where hot_list_id like ?1", nativeQuery = true)
    void removeAllHotListSubcategories(int id);

    @Modifying
    @Transactional
    @Query(value = "delete from hot_list_products where hot_list_id like ?1", nativeQuery = true)
    void removeAllHotListProducts(int id);

    List<HotList> getAllByProductType(Category.ProductType productType);
}
