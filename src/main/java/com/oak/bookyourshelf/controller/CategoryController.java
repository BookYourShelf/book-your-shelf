package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Book;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class CategoryController {

    final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public String showCategory(@RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               @RequestParam("sort") Optional<String> sort,
                               @RequestParam("languages") Optional<String> languages,
                               @RequestParam("publishers") Optional<String> publishers,
                               @RequestParam("authors") Optional<String> authors,
                               @RequestParam("stars") Optional<String> stars,
                               @RequestParam("minPrice") Optional<String> minPrice,
                               @RequestParam("maxPrice") Optional<String> maxPrice,
                               Model model, @PathVariable int id) {
        String currentSort = sort.orElse("date-desc");
        List<String> languageList = new ArrayList<>(Arrays.asList(languages.orElse("").split(",")));
        List<String> publishersList = new ArrayList<>(Arrays.asList(publishers.orElse("").split(",")));
        List<String> authorList = new ArrayList<>(Arrays.asList(authors.orElse("").split(",")));
        List<String> starList = new ArrayList<>(Arrays.asList(stars.orElse("").split(",")));
        List<String> minPriceList = new ArrayList<>(Arrays.asList(minPrice.orElse("").split(",")));
        List<String> maxPriceList = new ArrayList<>(Arrays.asList(maxPrice.orElse("").split(",")));


        Category category = categoryService.get(id);
        List<Book> books = category.getBooks();
        sortBooks(books, currentSort);
        float minP = Float.MAX_VALUE;
        float maxP = -1;

        for (Book b : books) {
            float price = b.getPrice();
            if (price < minP) {
                minP = price;
            }
            if (price > maxP) {
                maxP = price;
            }
        }

        if (!languageList.get(0).equals("")) {
            books.removeIf(book -> languageList.indexOf(book.getLanguage()) == -1);
        }

        if (!publishersList.get(0).equals("")) {
            Iterator<Book> bookIterator = books.iterator();
            Iterator<String> publishersIterator = publishersList.iterator();
            while (bookIterator.hasNext()) {
                while (publishersIterator.hasNext()) {
                    if (bookIterator.next().getPublishers().indexOf(publishersIterator.next()) == -1) {
                        bookIterator.remove();
                    }
                }
            }
        }

        if (!authorList.get(0).equals("")) {
            Iterator<Book> bookIterator = books.iterator();
            Iterator<String> authorsIterator = authorList.iterator();
            while (bookIterator.hasNext()) {
                while (authorsIterator.hasNext()) {
                    if (bookIterator.next().getAuthors().indexOf(authorsIterator.next()) == -1) {
                        bookIterator.remove();
                    }
                }
            }
        }

        if (!starList.get(0).equals("")) {
            Iterator<Book> bookIterator = books.iterator();
            while (bookIterator.hasNext()) {
                for (String s : starList) {
                    boolean remove = true;
                    double star = Double.parseDouble(s);
                    Book book = bookIterator.next();
                    if (book.getScoreOutOf5() < star + 1 &&
                            book.getScoreOutOf5() >= star) {
                        remove = false;
                    }
                    if (remove) {
                        bookIterator.remove();
                    }
                }
            }
        }

        if (!minPriceList.get(0).equals("")) {
            minP = Float.parseFloat(minPriceList.get(0));
        }

        if (!maxPriceList.get(0).equals("")) {
            maxP = Float.parseFloat(maxPriceList.get(0));
        }


        HashMap<String, Integer> languagesRet = new HashMap<>();
        HashMap<String, Integer> publishersRet = new HashMap<>();
        HashMap<String, Integer> authorsRet = new HashMap<>();
        for (Book book : books) {
            String lang = book.getLanguage();
            languagesRet.merge(lang, 1, Integer::sum);
            for (String publisher : book.getPublishers()) {
                publishersRet.merge(publisher, 1, Integer::sum);
            }

            for (String author : book.getAuthors()) {
                authorsRet.merge(author, 1, Integer::sum);
            }
        }

        Globals.getPageNumbers(page, size, books, model, "categoryBooks");
        model.addAttribute("category", category);
        model.addAttribute("sort", currentSort);
        model.addAttribute("languages", languagesRet);
        model.addAttribute("publishers", publishersRet);
        model.addAttribute("authors", authorsRet);
        model.addAttribute("stars", starList);
        model.addAttribute("minPrice", minP);
        model.addAttribute("maxPrice", maxP);
        return "/category";
    }

    private void sortBooks(List<Book> books, String sort) {
        switch (sort) {
            case "date-desc":
                books.sort((o1, o2) -> (int) (o1.getUploadDate().getTime() - o2.getUploadDate().getTime()));
                Collections.reverse(books);
                break;

            case "date-asc":
                books.sort((o1, o2) -> (int) (o1.getUploadDate().getTime() - o2.getUploadDate().getTime()));
                break;

            case "name-desc":
                books.sort(Comparator.comparing(Book::getProductName).reversed());
                break;

            case "name-asc":
                books.sort(Comparator.comparing(Book::getProductName));
                break;

            case "price-desc":
                books.sort((o1, o2) -> Float.compare(o1.getPrice(), o2.getPrice()));  // Handle discount
                Collections.reverse(books);
                break;

            case "price-asc":
                books.sort((o1, o2) -> Float.compare(o1.getPrice(), o2.getPrice()));  // Handle discount
                break;

            case "rate-desc":
                books.sort(Comparator.comparingDouble(Product::getScoreOutOf5));
                Collections.reverse(books);
                break;

            case "rate-asc":
                books.sort(Comparator.comparingDouble(Product::getScoreOutOf5));
                break;

            default:
                books.sort((o1, o2) -> (int) (o1.getUploadDate().getTime() - o2.getUploadDate().getTime()));
                break;
        }
    }
}
