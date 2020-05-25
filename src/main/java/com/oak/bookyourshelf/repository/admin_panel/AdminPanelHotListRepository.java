package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.HotList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelHotListRepository extends CrudRepository<HotList, Integer> {

    @Query("SELECT t FROM HotList t WHERE t.hotListType = ?1")
    HotList findHotListByHotListType(String name);

    @Query("SELECT t from HotList t where t.hotListType = ?1")
    List<HotList> findAllByHotListType(String name);
}
