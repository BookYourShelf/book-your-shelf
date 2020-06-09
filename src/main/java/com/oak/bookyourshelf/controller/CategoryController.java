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

        // Parse parameters
        List<String> languageList = new ArrayList<>(Arrays.asList(languages.orElse("").split(",")));
        List<String> publishersList = new ArrayList<>(Arrays.asList(publishers.orElse("").split(",")));
        List<String> authorList = new ArrayList<>(Arrays.asList(authors.orElse("").split(",")));
        List<String> starList = new ArrayList<>(Arrays.asList(stars.orElse("").split(",")));
        List<String> minPriceList = new ArrayList<>(Arrays.asList(minPrice.orElse("").split(",")));
        List<String> maxPriceList = new ArrayList<>(Arrays.asList(maxPrice.orElse("").split(",")));

        // Get books and create return books object
        Category category = categoryService.get(id);
        List<Book> books = category.getBooks();
        List<Book> booksRet = new ArrayList<>();

        sortBooks(books, currentSort);
        float minP = Float.MAX_VALUE;
        float maxP = -1;


        // Filter languages
        if (!languageList.get(0).equals("")) {
            for (Book book : books) {
                for (String language : languageList) {
                    if (book.getLanguage() != null) {
                        if (book.getLanguage().equals(language)) {
                            if (booksRet.indexOf(book) == -1) {
                                booksRet.add(book);
                            }
                        }
                    }
                }
            }
        }

        // Filter publishers
        if (!publishersList.get(0).equals("")) {
            for (Book book : books) {
                for (String publisher : publishersList) {
                    if (book.getPublishers().indexOf(publisher) != -1) {
                        if (booksRet.indexOf(book) == -1) {
                            booksRet.add(book);
                        }
                    }
                }
            }
        }

        // Filter authors
        if (!authorList.get(0).equals("")) {
            for (Book book : books) {
                for (String author : authorList) {
                    if (book.getAuthors().indexOf(author) != -1) {
                        if (booksRet.indexOf(book) == -1) {
                            booksRet.add(book);
                        }
                    }
                }
            }
        }

        // Filter stars and create star counts

        if (!starList.get(0).equals("")) {
            for (Book book : books) {
                for (String s : starList) {
                    double star = Double.parseDouble(s);

                    if (book.getScoreOutOf5() < star + 1 && book.getScoreOutOf5() >= star) {
                        if (booksRet.indexOf(book) == -1) {
                            booksRet.add(book);
                        }
                    }
                }
            }
        }

        // Find min and max prices
        for (Book b : books) {
            float price = b.getPrice();
            if (price < minP) {
                minP = price;
            }
            if (price > maxP) {
                maxP = price;
            }
        }

        // Set min and max values if parameters given
        if (!minPriceList.get(0).equals("")) {
            minP = Float.parseFloat(minPriceList.get(0));
        }

        if (!maxPriceList.get(0).equals("")) {
            maxP = Float.parseFloat(maxPriceList.get(0));
        }

        // No parameters
        if (languageList.get(0).equals("") && publishersList.get(0).equals("") && authorList.get(0).equals("") &&
                starList.get(0).equals("")) {
            booksRet = books;
        }

        for (Book book : booksRet) {
            if (book.getPrice() < minP || book.getPrice() > maxP) {
                booksRet.remove(book);
            }
        }


        // Find counts of languages, publishers and authors
        HashMap<String, Integer> languagesCount = new HashMap<>();
        HashMap<String, Integer> publishersCount = new HashMap<>();
        HashMap<String, Integer> authorsCount = new HashMap<>();
        HashMap<String, Integer> starCount = new HashMap<>();

        for (Book book : booksRet) {
            String lang = book.getLanguage();
            languagesCount.merge(lang, 1, Integer::sum);
            for (String publisher : book.getPublishers()) {
                publishersCount.merge(publisher, 1, Integer::sum);
            }

            for (String author : book.getAuthors()) {
                authorsCount.merge(author, 1, Integer::sum);
            }

            if (starList.get(0).equals("")) {
                starList.remove(0);
                starList.add("0");
                starList.add("1");
                starList.add("2");
                starList.add("3");
                starList.add("4");
                starList.add("5");
            }
            for (String s : starList) {
                double star = Double.parseDouble(s);

                if (book.getScoreOutOf5() < star + 1 && book.getScoreOutOf5() >= star) {
                    starCount.merge(s, 1, Integer::sum);
                }
            }
        }
        Globals.getPageNumbers(page, size, booksRet, model, "categoryBooks");
        model.addAttribute("category", category);
        model.addAttribute("sort", currentSort);
        model.addAttribute("languages", languagesCount);
        model.addAttribute("publishers", publishersCount);
        model.addAttribute("authors", authorsCount);
        model.addAttribute("stars", starCount);
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
