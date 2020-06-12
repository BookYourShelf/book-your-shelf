package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelHotListRepository extends CrudRepository<HotList, Integer> {

    @Query("SELECT t FROM HotList t WHERE t.hotListType = ?1")
    HotList findHotListByHotListType(String name);

    @Query("SELECT t from HotList t where t.hotListType = ?1")
    List<HotList> findAllByHotListType(String name);

    @Query("SELECT t from HotList t where t.productType = ?1")
    List<HotList> findAllByProductType(Category.ProductType name);

    List<HotList> findAllByOrderByIdAsc();

    List<HotList> findAllByOrderByIdDesc();

    List<HotList> findAllByOrderByProductNumAsc();

    List<HotList> findAllByOrderByProductNumDesc();

    List<HotList> findAllByOrderByProductTypeAsc();

    List<HotList> findAllByOrderByProductTypeDesc();


    List<HotList> findAllByOrderByStartDateAsc();

    List<HotList> findAllByOrderByStartDateDesc();

    List<HotList> findAllByOrderByEndDateAsc();

    List<HotList> findAllByOrderByEndDateDesc();

    List<HotList> findAllByOrderByStartTimeAsc();

    List<HotList> findAllByOrderByStartTimeDesc();

    List<HotList> findAllByOrderByEndTimeAsc();

    List<HotList> findAllByOrderByEndTimeDesc();

    List<HotList> findAllByOrderByHotListTypeAsc();

    List<HotList> findAllByOrderByHotListTypeDesc();


}
