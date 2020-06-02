package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelHotListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AdminPanelHotListService {
    final AdminPanelHotListRepository adminPanelHotListRepository;

    public AdminPanelHotListService(AdminPanelHotListRepository adminPanelHotListRepository) {
        this.adminPanelHotListRepository = adminPanelHotListRepository;
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


    public Set<Product> createProductSet(List<Subcategory> subcategories)
    {
        Set<Product> allProducts = Collections.emptySet();
        for(Subcategory sub :subcategories)
        {
            sub.setInHotList(true);
            for(Product p:sub.getBooks())
            {
                allProducts.add(p);
            }
        }
        return allProducts;
    }

    public void setProductByType(HotList hotList,String hotListType,Set<Product> products)
    {
        List<Product> allProducts;
        allProducts = new ArrayList<>(products);
        if(hotListType.equals("BEST_SELLERS"))
        {
            allProducts.sort(Comparator.comparing(Product::getSalesNum));
        }
        if(hotList.getProductNum()<=allProducts.size()) {
            List<Product> result = new ArrayList<>();
            int elementNum = allProducts.size() - 1;
            for (int i = 0; i < hotList.getProductNum(); ++i) {
                result.add(allProducts.get(elementNum));
                elementNum--;
            }
            hotList.setProducts(result);
        }
        else
        {
            hotList.setProducts(allProducts);
            hotList.setProductNum(allProducts.size());
        }


    }

    public boolean isDateValid(List<String> date)
    {
        int day = Integer.parseInt(date.get(0));
        int month = Integer.parseInt(date.get(1));
        int year = Integer.parseInt(date.get(2));

        if(!(day >0 && day <31))
            return false;
        if(!(month>0 && month<13))
            return false;
        if(!(year>=2020))
            return false;
        return true;
    }

    public boolean isDateCorrect(List<String> endDate , List<String> startDate)
    {
        int startDay = Integer.parseInt(startDate.get(0));
        int startMonth = Integer.parseInt(startDate.get(1));
        int startYear = Integer.parseInt(startDate.get(2));

        int endDay = Integer.parseInt(endDate.get(0));
        int endMonth = Integer.parseInt(endDate.get(1));
        int endYear = Integer.parseInt(endDate.get(2));

        if(endYear<startDay)
            return false;
        else if(endYear == startYear)
        {
            if(endMonth<startMonth)
                return false;
            else if(endMonth == startMonth)
            {
                if(endDay<startDay)
                    return false;
                else
                    return true;
            }
            else
                return true;
        }
        else
            return true;
    }

    public boolean isTimeValid(List<String> time)
    {
        int minute =Integer.parseInt(time.get(1));
        int hour = Integer.parseInt(time.get(0));

        if(minute>59)
            return false;
        if(hour>23)
            return false;
        else
            return true;
    }

    public boolean isTimeCorrect(List<String> startTime , List<String> endTime)
    {
        int startMinute =Integer.parseInt(startTime.get(1));
        int startHour = Integer.parseInt(startTime.get(0));

        int endMinute =Integer.parseInt(endTime.get(1));
        int endHour = Integer.parseInt(endTime.get(0));

        if(endHour<startHour)
            return false;
        else if(endHour == startHour)
        {
            if(endMinute<startMinute)
                return false;
            else
                return true;

        }
        else
            return true;
    }




}
