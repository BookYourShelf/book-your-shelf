package com.oak.bookyourshelf.service;


import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.repository.HotListDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public HotList get(int id) {
        if (hotListDetailsRepository.findById(id).isPresent())
            return hotListDetailsRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        hotListDetailsRepository.removeAllHotListCategories(id);
        hotListDetailsRepository.removeAllHotListSubcategories(id);
        hotListDetailsRepository.removeAllHotListProducts(id);
        hotListDetailsRepository.deleteById(id);
    }

    public void save(HotList user) {
        hotListDetailsRepository.save(user);
    }
}
