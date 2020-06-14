package com.oak.bookyourshelf.service;


import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.model.Subcategory;
import com.oak.bookyourshelf.repository.HotListDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HotListDetailsService {
    final HotListDetailsRepository hotListDetailsRepository;

    public HotListDetailsService(HotListDetailsRepository hotListDetailsRepository) {
        this.hotListDetailsRepository = hotListDetailsRepository;
    }

    public Iterable<HotList> listAll() {
        return hotListDetailsRepository.findAll();
    }

    public List<HotList> getAllByProductType(Category.ProductType productType) {
        return hotListDetailsRepository.getAllByProductType(productType);
    }

    public HotList get(int id) {
        if (hotListDetailsRepository.findById(id).isPresent())
            return hotListDetailsRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        //hotListDetailsRepository.removeAllHotListProducts(id);
        //hotListDetailsRepository.removeAllHotListSubcategories(id);
        //hotListDetailsRepository.removeAllHotListCategories(id);

        hotListDetailsRepository.deleteById(id);
    }

    public void save(HotList user) {
        hotListDetailsRepository.save(user);
    }


}
