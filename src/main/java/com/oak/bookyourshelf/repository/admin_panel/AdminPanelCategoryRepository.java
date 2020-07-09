package com.oak.bookyourshelf.repository.admin_panel;

import com.oak.bookyourshelf.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminPanelCategoryRepository extends CrudRepository<Category, Integer> {
    @Query("SELECT t FROM Category t WHERE t.name = ?1")
    Category findCategoryByName(String name);

    @Query("SELECT t from Category t where t.name = ?1")
    List<Category> findAllByName(String name);

    @Query("SELECT t from Category t where t.productType = ?1")
    Iterable<Category> getAllByCategory(Category.ProductType productType);

    List<Category> findAllByOrderByIdDesc();

    List<Category> findAllByOrderByIdAsc();

    List<Category> findAllByOrderByNameAsc();

    List<Category> findAllByOrderByNameDesc();

    @Query(value = "select category from Category category order by size(category.books) asc")
    List<Category> findAllByOrderByBooksAsc();

    @Query(value = "select category from Category category order by size(category.books) desc")
    List<Category> findAllByOrderByBooksDesc();

    @Query(value = "select category from Category category order by size(category.subcategories) asc")
    List<Category> findAllByOrderBySubcategoriesAsc();

    @Query(value = "select category from Category category order by size(category.subcategories) desc ")
    List<Category> findAllByOrderBySubcategoriesDesc();

}
