package com.oak.bookyourshelf.service;

import com.oak.bookyourshelf.model.PhysicalBook;
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


    public String createMediaTypeString(PhysicalBook.MediaType mediaType)
    {
        if(mediaType == PhysicalBook.MediaType.HARDCOVER)
            return "Hardcover";
        else if(mediaType == PhysicalBook.MediaType.MONTHLY)
            return "Monthly";
        else if(mediaType == PhysicalBook.MediaType.POCKET_SIZE)
            return "Pocket Size";
        else if(mediaType == PhysicalBook.MediaType.SOFTCOVER)
            return "Softcover";
        else
            return "Tablet Size";

    }

    public String createPaperTypeString(PhysicalBook.PaperType paperType)
    {
        if(paperType == PhysicalBook.PaperType.AMERICAN_BRISTOL)
            return "American Bristol";
        else if(paperType == PhysicalBook.PaperType.CANVAS)
            return "Canvas";
        else if(paperType == PhysicalBook.PaperType.CELLOPHANE)
            return "Cellophane";
        else if(paperType == PhysicalBook.PaperType.COATED)
            return "Coated";
        else if(paperType == PhysicalBook.PaperType.CRAFT)
            return "Craft";
        else if(paperType == PhysicalBook.PaperType.MATTE_COATED_PLASTER)
            return "Matte Coated Plaster";
        else if(paperType == PhysicalBook.PaperType.TRACING)
            return "Tracing";
        else
            return "Uncoated";
    }

    public String createBindingTypeString(PhysicalBook.BindingType bindingType)
    {
        if(bindingType == PhysicalBook.BindingType.CASE_BINDING)
            return "Case Binding";
        else if(bindingType == PhysicalBook.BindingType.PERFECT_BINDING)
            return "Perfect Binding";
        else if(bindingType == PhysicalBook.BindingType.CASE_WRAPPED)
            return "Case Wrapped";
        else if(bindingType == PhysicalBook.BindingType.PLASTIC_COIL)
            return "Plastic Coil";
        else
            return "Saddle Stitching";
    }


}
