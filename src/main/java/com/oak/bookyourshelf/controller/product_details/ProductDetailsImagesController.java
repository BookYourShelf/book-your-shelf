package com.oak.bookyourshelf.controller.product_details;

import com.oak.bookyourshelf.Globals;
import com.oak.bookyourshelf.model.*;
import com.oak.bookyourshelf.service.admin_panel.AdminPanelProductService;
import com.oak.bookyourshelf.service.product_details.ProductDetailsInformationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class ProductDetailsImagesController {

    final ProductDetailsInformationService productDetailsInformationService;
    final AdminPanelProductService adminPanelProductService;

    public ProductDetailsImagesController(ProductDetailsInformationService productDetailsInformationService,
                                          AdminPanelProductService adminPanelProductService) {
        this.productDetailsInformationService = productDetailsInformationService;
        this.adminPanelProductService = adminPanelProductService;
    }

    @RequestMapping(value = "/product-details/images/{id}", method = RequestMethod.GET)
    public String tab(Model model, @PathVariable int id) {

        Product product = productDetailsInformationService.get(id);
        List<Image> imageList = product.getImages();

        model.addAttribute("cover_image", imageList.get(0));
        model.addAttribute("product_images", imageList.subList(1, imageList.size()));
        model.addAttribute("product", product);
        return "product_details/_images";
    }

    @RequestMapping(value = "/product-details/images/{id}", method = RequestMethod.POST)
    public ResponseEntity<String> saveProduct(@RequestParam String type, @PathVariable int id, Integer imageId,
                                              @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
                                              @RequestParam(value = "productImages", required = false) MultipartFile[] productImages) throws IOException {

        Product product = productDetailsInformationService.get(id);
        List<Image> images = product.getImages();

        switch (type) {
            case "update":
                if (productImages.length + images.size() - 1 > 10) {
                    return ResponseEntity.badRequest().body("At most 10 product images can be uploaded.");
                }

                if (coverImage != null && !coverImage.isEmpty()) {
                    InputStream coverImageStream = coverImage.getInputStream();
                    Image img = new Image();
                    img.setImage(coverImageStream.readAllBytes());
                    images.set(0, img);
                }

                for (MultipartFile file : productImages) {
                    InputStream productImageStream = file.getInputStream();
                    Image img = new Image();
                    img.setImage(productImageStream.readAllBytes());
                    images.add(img);
                }

                product.setImages(images);
                adminPanelProductService.save(product);
                return ResponseEntity.ok("");

            case "delete":
                deleteImage(images, imageId);
                adminPanelProductService.save(product);
                return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body("An error occurred.");
    }

    public void deleteImage(List<Image> images, int imageId) {
        images.removeIf(i -> i.getId() == imageId);
    }
}
