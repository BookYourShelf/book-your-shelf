package com.oak.bookyourshelf.service.admin_panel;


import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.repository.admin_panel.AdminPanelCampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
