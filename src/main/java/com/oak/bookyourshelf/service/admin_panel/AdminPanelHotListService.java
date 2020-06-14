package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.repository.ProductRepository;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelHotListRepository;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelProductRepository;
import com.oak.bookyourshelf.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class AdminPanelHotListService {
    final AdminPanelHotListRepository adminPanelHotListRepository;
    final AdminPanelProductRepository adminPanelProductRepository;
    final ProductRepository productRepository;
    final ProductService productService;


    public AdminPanelHotListService(AdminPanelHotListRepository adminPanelHotListRepository, AdminPanelProductRepository adminPanelProductRepository, ProductRepository productRepository, ProductService productService) {
        this.adminPanelHotListRepository = adminPanelHotListRepository;

        this.adminPanelProductRepository = adminPanelProductRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public Iterable<HotList> listAll() {
        return adminPanelHotListRepository.findAll();
    }

    public void save(HotList hotList) {
        adminPanelHotListRepository.save(hotList);
    }

    public HotList get(int id) {
        return adminPanelHotListRepository.findById(id).get();
    }

    public HotList getByHotListType(String name) {
        return adminPanelHotListRepository.findHotListByHotListType(name);
    }

    public List<HotList> getAllByName(String name) {
        return adminPanelHotListRepository.findAllByHotListType(name);
    }

    public void delete(int id) {
        adminPanelHotListRepository.deleteById(id);
    }

    public List<HotList> findAllByProductType(Category.ProductType type) {
        return adminPanelHotListRepository.findAllByProductType(type);
    }


    public Set<Book> createProductSet(Set<Subcategory> subcategories) {
        Set<Book> allProducts = new HashSet<>();
        for (Subcategory sub : subcategories) {
            sub.setInHotList(true);
            allProducts.addAll(sub.getBooks());
        }
        return allProducts;
    }

    public void setProductByType(HotList hotList, Set<Book> books) {
        List<Product> allProducts = new ArrayList<Product>();
        List<Integer> productIds = new ArrayList<Integer>();
        for (Book b : books) {
            productIds.add(b.getProductId());
        }
        if (hotList.getHotListType() == HotList.HotListType.BEST_SELLERS) {
            allProducts.addAll(adminPanelProductRepository.findAllByOrderBySalesNumDesc());
        } else if (hotList.getHotListType() == HotList.HotListType.NEW_RELEASES) {
            allProducts.addAll(adminPanelProductRepository.findAllByOrderByUploadDateDesc());
        }
        Set<Product> result = new HashSet<>();
        if (hotList.getProductNum() <= books.size()) {

            int elementNum = 0;
            for (Product p : allProducts) {
                if ((productIds.contains(p.getProductId())) && elementNum < hotList.getProductNum()) {
                    result.add(p);
                    elementNum++;
                }
            }
            hotList.setProducts(result);
        } else if (books.size() != 0) {
            Set<Product> all = new HashSet<>(books);
            hotList.setProducts(all);
            hotList.setProductNum(allProducts.size());
        } else {
            Set<Product> all = new HashSet<>(books);
            for (Product product : productService.getAllProduct()) {
                if (all.size() < hotList.getProductNum()) {
                    if (product.getProductTypeName() == "Book" && hotList.getProductType() == Category.ProductType.BOOK) {
                        all.add(product);
                    } else if (product.getProductTypeName() == "E-Book" && hotList.getProductType() == Category.ProductType.E_BOOK) {
                        all.add(product);
                    } else if (product.getProductTypeName() == "Audio Book" && hotList.getProductType() == Category.ProductType.AUDIO_BOOK) {
                        all.add(product);
                    }
                } else {
                    break;
                }
            }
            hotList.setProducts(all);
            hotList.setProductNum(all.size());
        }


    }

    public void setProductByTypeOtherTypes(HotList hotList) {
        List<Product> allProducts = new ArrayList<Product>();
        List<Product> products = new ArrayList<>();

        if (hotList.getProductType() == Category.ProductType.E_BOOK_READER)
            allProducts = productService.filterProducts((List<Product>) productRepository.findAll(), "E-Book Reader");
        else if (hotList.getProductType() == Category.ProductType.E_BOOK_READER_CASE)
            allProducts = productService.filterProducts((List<Product>) productRepository.findAll(), "E-Book Reader Case");
        else if (hotList.getProductType() == Category.ProductType.BOOK_CASE)
            allProducts = productService.filterProducts((List<Product>) productRepository.findAll(), "Book Case");
        List<Integer> productIds = new ArrayList<Integer>();

        if (hotList.getHotListType() == HotList.HotListType.BEST_SELLERS) {
            products.addAll(adminPanelProductRepository.findAllByOrderBySalesNumDesc());
        } else if (hotList.getHotListType() == HotList.HotListType.NEW_RELEASES) {
            products.addAll(adminPanelProductRepository.findAllByOrderByUploadDateDesc());
        }
        Set<Product> result = new HashSet<>();
        if (hotList.getProductNum() <= allProducts.size()) {

            int elementNum = 0;
            for (Product p : products) {
                if ((allProducts.contains(p)) && elementNum < hotList.getProductNum()) {
                    result.add(p);
                    elementNum++;
                }
            }
            hotList.setProducts(result);
        } else {
            Set<Product> all = new HashSet<>(allProducts);
            hotList.setProducts(all);
            hotList.setProductNum(allProducts.size());
        }


    }


    public boolean isDateValid(List<String> date) {
        int day = Integer.parseInt(date.get(0));
        int month = Integer.parseInt(date.get(1));
        int year = Integer.parseInt(date.get(2));

        if (!(day > 0 && day < 31))
            return false;
        if (!(month > 0 && month < 13))
            return false;
        if (!(year >= 2020))
            return false;
        return true;
    }

    public boolean isDateCorrect(List<String> endDate, List<String> startDate) {
        int startDay = Integer.parseInt(startDate.get(0));
        int startMonth = Integer.parseInt(startDate.get(1));
        int startYear = Integer.parseInt(startDate.get(2));

        int endDay = Integer.parseInt(endDate.get(0));
        int endMonth = Integer.parseInt(endDate.get(1));
        int endYear = Integer.parseInt(endDate.get(2));

        if (endYear < startDay)
            return false;
        else if (endYear == startYear) {
            if (endMonth < startMonth)
                return false;
            else if (endMonth == startMonth) {
                if (endDay < startDay)
                    return false;
                else
                    return true;
            } else
                return true;
        } else
            return true;
    }

    public boolean isTimeValid(List<String> time) {
        int minute = Integer.parseInt(time.get(1));
        int hour = Integer.parseInt(time.get(0));

        if (minute > 59)
            return false;
        if (hour > 23)
            return false;
        else
            return true;
    }

    public boolean isTimeCorrect(List<String> startTime, List<String> endTime) {
        int startMinute = Integer.parseInt(startTime.get(1));
        int startHour = Integer.parseInt(startTime.get(0));

        int endMinute = Integer.parseInt(endTime.get(1));
        int endHour = Integer.parseInt(endTime.get(0));

        if (endHour < startHour)
            return false;
        else if (endHour == startHour) {
            if (endMinute < startMinute)
                return false;
            else
                return true;

        } else
            return true;
    }

    public boolean isDateInPast(List<String> startDate)
    {
        LocalDate now = LocalDate.now();
        int yearNow =now.getYear();
        int monthNow =now.getMonthValue();
        int dayNow = now.getDayOfMonth();

        int startDay = Integer.parseInt(startDate.get(0));
        int startMonth = Integer.parseInt(startDate.get(1));
        int startYear = Integer.parseInt(startDate.get(2));

        if(startYear<yearNow)
            return false;
        else if(startYear == yearNow)
        {
            if(startMonth<monthNow)
                return false;
            else if(startMonth == monthNow)
            {
                if(startDay<dayNow)
                    return false;
                else
                    return true;
            }
            else
                return true;
        }
        else return true;

    }


    public List<HotList> sortHotlists(String sortType) {
        switch (sortType) {

            case "ID-desc":
                return adminPanelHotListRepository.findAllByOrderByIdDesc();

            case "ID-asc":
                return adminPanelHotListRepository.findAllByOrderByIdAsc();

            case "Product-Num-desc":
                return adminPanelHotListRepository.findAllByOrderByProductNumDesc();

            case "Product-Num-asc":
                return adminPanelHotListRepository.findAllByOrderByProductNumAsc();

            case "Product-Type-desc":
                return adminPanelHotListRepository.findAllByOrderByProductTypeDesc();

            case "Product-Type-asc":
                return adminPanelHotListRepository.findAllByOrderByProductTypeAsc();

            case "Hot-List-Type-desc":
                return adminPanelHotListRepository.findAllByOrderByHotListTypeDesc();

            case "Hot-List-Type-asc":
                return adminPanelHotListRepository.findAllByOrderByHotListTypeAsc();

            default:        // id
                return adminPanelHotListRepository.findAllByOrderByIdDesc();
        }
    }


    public String createProductType(HotList hotList) {
        if (hotList.getProductType() == Category.ProductType.BOOK)
            return "Book";
        else if (hotList.getProductType() == Category.ProductType.AUDIO_BOOK)
            return "Audio Book";
        else if (hotList.getProductType() == Category.ProductType.E_BOOK)
            return "E Book";
        else if (hotList.getProductType() == Category.ProductType.BOOK_CASE)
            return "Book Case";
        else if (hotList.getProductType() == Category.ProductType.E_BOOK_READER_CASE)
            return "E Book Reader Case";
        else
            return "E Book Reader";
    }

    public String createHotListType(HotList hotList) {
        if (hotList.getHotListType() == HotList.HotListType.NEW_RELEASES)
            return "New Releases";
        else
            return "Best Sellers";
    }


}
