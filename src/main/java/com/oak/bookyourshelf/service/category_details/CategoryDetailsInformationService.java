package com.oak.bookyourshelf.service.category_details;

import com.oak.bookyourshelf.model.Book;
import com.oak.bookyourshelf.model.Campaign;
import com.oak.bookyourshelf.model.Category;
import com.oak.bookyourshelf.model.HotList;
import com.oak.bookyourshelf.repository.ProductRepository;
import com.oak.bookyourshelf.repository.category_details.CategoryDetailsInformationRepository;
import com.oak.bookyourshelf.service.CampaignDetailsService;
import com.oak.bookyourshelf.service.HotListDetailsService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryDetailsInformationService {

    final CategoryDetailsInformationRepository categoryDetailsInformationRepository;
    final ProductRepository productRepository;
    final ProductDetailsInformationService productDetailsInformationService;
    final HotListDetailsService hotListDetailsService;
    final CampaignDetailsService campaignDetailsService;

    public CategoryDetailsInformationService(CategoryDetailsInformationRepository categoryDetailsInformationRepository, ProductRepository productRepository, ProductDetailsInformationService productDetailsInformationService, HotListDetailsService hotListDetailsService, CampaignDetailsService campaignDetailsService) {
        this.categoryDetailsInformationRepository = categoryDetailsInformationRepository;
        this.productRepository = productRepository;
        this.productDetailsInformationService = productDetailsInformationService;
        this.hotListDetailsService = hotListDetailsService;
        this.campaignDetailsService = campaignDetailsService;
    }

    public Iterable<Category> listAll() {
        return categoryDetailsInformationRepository.findAll();
    }

    public Category get(int id) {
        if (categoryDetailsInformationRepository.findById(id).isPresent())
            return categoryDetailsInformationRepository.findById(id).get();
        else
            return null;
    }

    public void delete(int id) {
        Category category = get(id);
        for (Book book : category.getBooks()) {
            productDetailsInformationService.deleteProduct(book.getProductId());
        }
        for (HotList hotList : hotListDetailsService.listAll()) {
            if (hotList.getCategories().indexOf(category) != -1) {
                hotListDetailsService.delete(hotList.getId());
            }
        }
        for (Campaign campaign : campaignDetailsService.listAll()) {
            if (campaign.getCategories().indexOf(category) != -1) {
                campaignDetailsService.delete(campaign.getId());
            }
        }
        categoryDetailsInformationRepository.deleteById(id);
    }

    public void save(Category user) {
        categoryDetailsInformationRepository.save(user);
    }
}
