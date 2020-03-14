package com.oak.bookyourshelf;

import com.oak.bookyourshelf.model.ElectronicBook;
import org.springframework.data.repository.CrudRepository;

public interface ElectronicBookRepository extends CrudRepository<ElectronicBook, Integer> {
}
