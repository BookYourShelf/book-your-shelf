package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminPanelCategoryService {

    final AdminPanelCategoryRepository adminPanelCategoryRepository;

    public AdminPanelCategoryService(AdminPanelCategoryRepository adminPanelCategoryRepository) {
        this.adminPanelCategoryRepository = adminPanelCategoryRepository;
    }

    public Iterable<Category> listAll() {
        return adminPanelCategoryRepository.findAll();
    }

    public Iterable<Category> getAllByCategory(String productType) {
        switch (productType) {
            case "Book":
                return adminPanelCategoryRepository.getAllByCategory(Category.ProductType.values()[0]);
            case "E-Book":
                return adminPanelCategoryRepository.getAllByCategory(Category.ProductType.values()[1]);
            case "Audio Book":
                return adminPanelCategoryRepository.getAllByCategory(Category.ProductType.values()[2]);
        }
        return null;
    }

    public void save(Category category) {
        adminPanelCategoryRepository.save(category);
    }

    public Category get(int id) {
        return adminPanelCategoryRepository.findById(id).get();
    }

    public Category getByName(String name) {
        return adminPanelCategoryRepository.findCategoryByName(name);
    }

    public List<Category> getAllByName(String name) {
        return adminPanelCategoryRepository.findAllByName(name);
    }

    static Subcategory traverseSubcategories(Subcategory subcategory, String name) {
        if (subcategory.getName().equals(name)) {
            return subcategory;
        }
        for (Subcategory sub : subcategory.getSubcategories()) {
            traverseSubcategories(sub, name);
        }
        return null;
    }

    public Subcategory getSubcategory(Category category, String name) {
        List<Subcategory> l = category.getSubcategories().stream().flatMap(elt -> elt.getSubcategories().stream())
                .collect(Collectors.toList());
        l.addAll(category.getSubcategories());
        Subcategory ret = new Subcategory();
        for (Subcategory sub : l) {
            if (sub.getName().equals(name)) {
                ret = sub;
            }
        }
        ;
        return ret;
    }

    public ArrayList<String> getAllSubcategoriesName(Category category) {
        return Globals.getAllSubcategories(category);
    }

    public void delete(int id) {
        adminPanelCategoryRepository.deleteById(id);
    }

    public List<Category> sortCategories(String sortType) {
        switch (sortType) {
            case "ID-desc":
                return adminPanelCategoryRepository.findAllByOrderByIdDesc();

            case "ID-asc":
                return adminPanelCategoryRepository.findAllByOrderByIdAsc();

            case "name-desc":
                return adminPanelCategoryRepository.findAllByOrderByNameDesc();

            case "name-asc":
                return adminPanelCategoryRepository.findAllByOrderByNameAsc();

            case "total-desc":
                return adminPanelCategoryRepository.findAllByOrderByBooksDesc();

            case "total-asc":
                return adminPanelCategoryRepository.findAllByOrderByBooksAsc();

            case "subcategory-desc":
                return adminPanelCategoryRepository.findAllByOrderBySubcategoriesDesc();

            case "subcategory-asc":
                return adminPanelCategoryRepository.findAllByOrderBySubcategoriesAsc();

            default:
                return adminPanelCategoryRepository.findAllByOrderByIdAsc();
        }
    }
}
