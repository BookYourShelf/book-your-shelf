package com.oak.bookyourshelf.repository;

import com.oak.bookyourshelf.model.HotList;
import org.springframework.data.repository.CrudRepository;

public interface HotListDetailsRepository extends CrudRepository<HotList,Integer> {
}
