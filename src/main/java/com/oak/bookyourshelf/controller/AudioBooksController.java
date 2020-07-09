package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.Book;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.service.CategoryService;
import com.oak.bookyourshelf.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class AudioBooksController {

    final CategoryService categoryService;
    final ProductService productService;

    public AudioBooksController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @RequestMapping(value = "/audiobooks", method = RequestMethod.GET)
    public String showCategory(@RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               @RequestParam("sort") Optional<String> sort,
                               @RequestParam("languages") Optional<String> languages,
                               @RequestParam("translators") Optional<String> translators,
                               @RequestParam("publishers") Optional<String> publishers,
                               @RequestParam("authors") Optional<String> authors,
                               @RequestParam("discount") Optional<String> discount,
                               @RequestParam("stars") Optional<String> stars,
                               @RequestParam("minPrice") Optional<String> minPrice,
                               @RequestParam("maxPrice") Optional<String> maxPrice,
                               Model model) {

        String currentSort = sort.orElse("date-desc");

        // Parse parameters
        List<String> languageList = new ArrayList<>(Arrays.asList(languages.orElse("").split(",")));
        List<String> translatorsList = new ArrayList<>(Arrays.asList(translators.orElse("").split(",")));
        List<String> publishersList = new ArrayList<>(Arrays.asList(publishers.orElse("").split(",")));
        List<String> authorList = new ArrayList<>(Arrays.asList(authors.orElse("").split(",")));
        List<String> discountList = new ArrayList<>(Arrays.asList(discount.orElse("").split(",")));
        List<String> starList = new ArrayList<>(Arrays.asList(stars.orElse("").split(",")));
        List<String> minPriceList = new ArrayList<>(Arrays.asList(minPrice.orElse("").split(",")));
        List<String> maxPriceList = new ArrayList<>(Arrays.asList(maxPrice.orElse("").split(",")));

        // Get books and create return books object
        List<Book> books = new ArrayList<>();
        List<Category> categories = (List<Category>) categoryService.listAll();
        categories.removeIf(category -> category.getProductType() != Category.ProductType.AUDIO_BOOK);
        for (Category c : categories) {
            books.addAll(c.getBooks());
        }


        sortBooks(books, currentSort);
        float minP = Float.MAX_VALUE;
        float maxP = -1;

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

        // Filter languages
        List<Book> booksLanguage = new ArrayList<>();
        if (!languageList.get(0).equals("")) {
            for (Book book : books) {
                for (String language : languageList) {
                    if (book.getLanguage() != null) {
                        if (book.getLanguage().equals(language)) {
                            booksLanguage.add(book);
                        }
                    }
                }
            }
        } else {
            booksLanguage.addAll(books);
        }

        // Filter translators
        List<Book> booksTranslator = new ArrayList<>();
        if (!translatorsList.get(0).equals("")) {
            for (Book book : booksLanguage) {
                for (String language : translatorsList) {
                    if (book.getTranslators().indexOf(language) != -1) {
                        booksTranslator.add(book);
                    }
                }
            }
        } else {
            booksTranslator.addAll(booksLanguage);
        }

        // Filter publishers
        List<Book> booksPublisher = new ArrayList<>();
        if (!publishersList.get(0).equals("")) {
            for (Book book : booksTranslator) {
                for (String publisher : publishersList) {
                    if (book.getPublishers().indexOf(publisher) != -1) {
                        booksPublisher.add(book);
                    }
                }
            }
        } else {
            booksPublisher.addAll(booksTranslator);
        }

        // Filter authors
        List<Book> booksAuthor = new ArrayList<>();
        if (!authorList.get(0).equals("")) {
            for (Book book : booksPublisher) {
                for (String author : authorList) {
                    if (book.getAuthors().indexOf(author) != -1) {
                        booksAuthor.add(book);
                    }
                }
            }
        } else {
            booksAuthor.addAll(booksPublisher);
        }

        // Filter discount
        List<Book> booksDiscount = new ArrayList<>();
        if (!discountList.get(0).equals("")) {
            for (Book book : booksAuthor) {
                for (String d : discountList) {
                    if (d.equals("No Discount") && !book.isOnDiscount()) {
                        booksDiscount.add(book);
                    } else if (d.equals("Discount") && book.isOnDiscount()) {
                        booksDiscount.add(book);
                    }
                }
            }
        } else {
            booksDiscount.addAll(booksAuthor);
        }

        // Filter stars and create star counts
        List<Book> booksStar = new ArrayList<>();
        if (!starList.get(0).equals("")) {
            for (Book book : booksDiscount) {
                for (String s : starList) {
                    double star = Double.parseDouble(s);

                    if (book.getScoreOutOf5() < star + 1 && book.getScoreOutOf5() >= star) {
                        booksStar.add(book);
                    }
                }
            }
        } else {
            booksStar.addAll(booksDiscount);
        }

        // Set min and max values if parameters given
        List<Book> booksMin = new ArrayList<>();
        if (!minPriceList.get(0).equals("")) {
            minP = Float.parseFloat(minPriceList.get(0));
            for (Book book : booksStar) {
                if (book.getPrice() >= minP) {
                    booksMin.add(book);
                }
            }
        } else {
            booksMin.addAll(booksStar);
        }

        List<Book> booksMax = new ArrayList<>();
        if (!maxPriceList.get(0).equals("")) {
            maxP = Float.parseFloat(maxPriceList.get(0));
            for (Book book : booksMin) {
                if (book.getPrice() <= maxP) {
                    booksMax.add(book);
                }
            }
        } else {
            booksMax.addAll(booksMin);
        }


        // Find counts of languages, publishers and authors
        HashMap<String, Integer> languagesCount = new HashMap<>();
        HashMap<String, Integer> translatorsCount = new HashMap<>();
        HashMap<String, Integer> publishersCount = new HashMap<>();
        HashMap<String, Integer> authorsCount = new HashMap<>();
        HashMap<String, Integer> starCount = new HashMap<>();

        for (Book book : booksMax) {
            String lang = book.getLanguage();
            languagesCount.merge(lang, 1, Integer::sum);

            for (String translator : book.getTranslators()) {
                translatorsCount.merge(translator, 1, Integer::sum);
            }

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
        Globals.getPageNumbers(page, size, booksMax, model, "categoryBooks");
        model.addAttribute("categories", categories);
        model.addAttribute("sort", currentSort);
        model.addAttribute("languages", languagesCount);
        model.addAttribute("translators", translatorsCount);
        model.addAttribute("publishers", publishersCount);
        model.addAttribute("authors", authorsCount);
        model.addAttribute("stars", starCount);
        model.addAttribute("minPrice", minP);
        model.addAttribute("maxPrice", maxP);
        return "audiobooks";
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
