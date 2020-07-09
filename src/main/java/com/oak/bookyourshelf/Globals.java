package com.oak.bookyourshelf;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Image;
import com.oak.bookyourshelf.model.Subcategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

public class Globals {

    public static int PAGINATION = 10;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ORDER_CODE_LENGTH = 8;
    public static final Map<String, String> productTypeNames = Map.ofEntries(
            Map.entry("BOOK", "Book"),
            Map.entry("E_BOOK", "E-Book"),
            Map.entry("AUDIO_BOOK", "Audio Book"),
            Map.entry("E_BOOK_READER", "E-Book Reader"),
            Map.entry("E_BOOK_CASE", "E-Book Case"),
            Map.entry("BOOK_CASE", "Book Case")
    );

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

    static void traverseSubcategories(Subcategory subcategory, ArrayList<String> subcategories) {
        Set<Subcategory> inSubcategory = subcategory.getSubcategories();
        subcategories.add(subcategory.getName());
        if (subcategory.getSubcategories().size() != 0) {
            for (Subcategory sub : inSubcategory) {
                traverseSubcategories(sub, subcategories);
            }
        }
    }

    public static ArrayList<String> getAllSubcategories(Category category) {
        ArrayList<String> subcategories = new ArrayList<>();
        Set<Subcategory> inCategory = category.getSubcategories();
        for (Subcategory sub : inCategory) {
            traverseSubcategories(sub, subcategories);
        }
        return subcategories;
    }

    static void traverseChildSubcategories(Subcategory subcategory, ArrayList<Subcategory> subcategories) {
        Set<Subcategory> inSubcategory = subcategory.getSubcategories();
        if (subcategory.getSubcategories().size() == 0) {
            subcategories.add(subcategory);
        } else {
            for (Subcategory sub : inSubcategory) {
                traverseChildSubcategories(sub, subcategories);
            }
        }
    }

    public static ArrayList<Subcategory> getAllChildSubcategories(Category category) {
        ArrayList<Subcategory> subcategories = new ArrayList<>();
        Set<Subcategory> inCategory = category.getSubcategories();
        for (Subcategory sub : inCategory) {
            traverseChildSubcategories(sub, subcategories);
        }
        return subcategories;
    }

    public static List<String> encodeAllImages(List<Image> images) {
        List<String> ret = new ArrayList<>();

        for (Image img : images) {
            ret.add(Base64.getEncoder().encodeToString(img.getImage()));
        }
        return ret;
    }

    public static String generateOrderCode() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < ORDER_CODE_LENGTH; i++) {
            int char_idx = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(char_idx));
        }
        return builder.toString();
    }

    public static void productDetailsTraverseSubcategories(Subcategory subcategory, ArrayList<Subcategory> subcategories) {
        Set<Subcategory> inSubcategory = subcategory.getSubcategories();
        subcategories.add(subcategory);
        if (subcategory.getSubcategories().size() != 0) {
            for (Subcategory sub : inSubcategory) {
                productDetailsTraverseSubcategories(sub, subcategories);
            }
        }
    }

    public static ArrayList<Subcategory> productDetailsGetAllSubcategories(Category category) {
        ArrayList<Subcategory> subcategories = new ArrayList<>();
        Set<Subcategory> inCategory = category.getSubcategories();
        for (Subcategory sub : inCategory) {
            productDetailsTraverseSubcategories(sub, subcategories);
        }
        return subcategories;
    }

    public static ArrayList<Subcategory> traversePathBetweenSubcategories(Subcategory subcategory, Subcategory find) {
        ArrayList<Subcategory> ret = new ArrayList<>();
        ret.add(subcategory);
        for (Subcategory sub : subcategory.getSubcategories()) {
            if (sub.equals(find)) {
                ret.add(sub);
            } else {
                traversePathBetweenSubcategories(sub, find);
            }
        }
        if (ret.size() > 1) {
            return ret;
        }
        ret.remove(0);
        return ret;
    }

    public static ArrayList<Subcategory> findPathBetweenSubcategoryAndCategory(Category category, Subcategory subcategory) {
        ArrayList<Subcategory> ret = new ArrayList<>();
        for (Subcategory sub : category.getSubcategories()) {
            if (sub.equals(subcategory)) {
                ret.add(sub);
            } else {
                ret.addAll(traversePathBetweenSubcategories(sub, subcategory));
            }
        }

        return ret;
    }
}
