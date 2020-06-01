package com.oak.bookyourshelf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Subcategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

public class Globals {

    public static int PAGINATION = 3;

    public static void getPageNumbers(Optional<Integer> page, Optional<Integer> size, List objects, Model model, String attrName) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(PAGINATION);
        Page<Object> productPage = findPaginated(objects, PageRequest.of(currentPage - 1, pageSize));
        int totalPages = productPage.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("currentPage", currentPage);
        model.addAttribute(attrName, productPage);
    }

    public static Page<Object> findPaginated(List objects, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Object> list;

        if (objects.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, objects.size());
            list = objects.subList(startItem, toIndex);
        }

        Page<Object> objectPage = new PageImpl<Object>(list, PageRequest.of(currentPage, pageSize), objects.size());

        return objectPage;
    }


    static void traverseSubcategories(Subcategory subcategory, ArrayList<Subcategory> subcategories) {
        List<Subcategory> inSubcategory = subcategory.getSubcategories();
        subcategories.add(subcategory);
        if (subcategory.getSubcategories().size() != 0) {
            for (Subcategory sub : inSubcategory) {
                traverseSubcategories(sub, subcategories);
            }
        }
    }

    public static ArrayList<Subcategory> getAllSubcategories(Category category) {
        ArrayList<Subcategory> subcategories = new ArrayList<>();
        List<Subcategory> inCategory = category.getSubcategories();
        for (Subcategory sub : inCategory) {
            traverseSubcategories(sub, subcategories);
        }
        return subcategories;
    }

    static void traverseChildSubcategories(Subcategory subcategory, ArrayList<Subcategory> subcategories) {
        List<Subcategory> inSubcategory = subcategory.getSubcategories();
        if (subcategory.getSubcategories().size() == 0) {
            subcategories.add(subcategory);
        } else {
            for (Subcategory sub : inSubcategory) {
                traverseSubcategories(sub, subcategories);
            }
        }
    }

    public static ArrayList<Subcategory> getAllChildSubcategories(Category category) {
        ArrayList<Subcategory> subcategories = new ArrayList<>();
        List<Subcategory> inCategory = category.getSubcategories();
        for (Subcategory sub : inCategory) {
            traverseSubcategories(sub, subcategories);
        }
        return subcategories;
    }
}
