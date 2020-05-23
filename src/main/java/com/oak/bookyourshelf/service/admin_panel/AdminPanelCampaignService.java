package com.oak.bookyourshelf.service.admin_panel;


import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelCampaignRepository;
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
public class AdminPanelCampaignService {
    final AdminPanelCampaignRepository adminPanelCampaignRepository;

    public AdminPanelCampaignService(AdminPanelCampaignRepository adminPanelCampaignRepository) {
        this.adminPanelCampaignRepository = adminPanelCampaignRepository;
    }

    public Iterable<Campaign> listAll() {
        return adminPanelCampaignRepository.findAll();
    }

    public void save(Campaign campaign) {
        adminPanelCampaignRepository.save(campaign);
    }

    public Campaign get(int id) {
        return adminPanelCampaignRepository.findById(id).get();
    }

    public Campaign getByName(String name) {
        return adminPanelCampaignRepository.findCampaignByName(name);
    }

    public List<Campaign> getAllByName(String name) {
        return adminPanelCampaignRepository.findAllByName(name);
    }

    public void delete(int id) {
        adminPanelCampaignRepository.deleteById(id);
    }

    public Page<Campaign> findPaginated(Pageable pageable) {
        List campaigns = (List)adminPanelCampaignRepository.findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Campaign> list;

        if (campaigns.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, campaigns.size());
            list = campaigns.subList(startItem, toIndex);
        }

        Page<Campaign> campaignPage
                = new PageImpl<Campaign>(list, PageRequest.of(currentPage, pageSize), campaigns.size());

        return campaignPage;
    }
}
