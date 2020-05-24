package com.oak.bookyourshelf.service.admin_panel;

import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelHotListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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

    public Page<HotList> findPaginated(Pageable pageable) {
        List hotlists = (List)adminPanelHotListRepository.findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<HotList> list;

        if (hotlists.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, hotlists.size());
            list = hotlists.subList(startItem, toIndex);
        }

        Page<HotList> hotListPage
                = new PageImpl<HotList>(list, PageRequest.of(currentPage, pageSize), hotlists.size());

        return hotListPage;
    }
}
