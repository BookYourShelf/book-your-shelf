package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.Product;
import com.oak.bookyourshelf.repository.CompareProductsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CompareProductsService {
    final CompareProductsRepository compareProductsRepository;

    public CompareProductsService(CompareProductsRepository compareProductsRepository) {
        this.compareProductsRepository = compareProductsRepository;
    }
    public Product get(int id) {
        return compareProductsRepository.findById(id).get();
    }

    public String createString(List<String > elemetList)
    {
        String result=elemetList.get(0);
        for(int i =1 ;i<elemetList.size();++i)
        {
            result += "-";
            result += elemetList.get(i);
        }
        return result;

    }

    public float calculateNetPrice(Product p)
    {
        float rate = p.getDiscountRate()*100;
        return (p.getPrice()*(100-rate)/100);
    }


}
