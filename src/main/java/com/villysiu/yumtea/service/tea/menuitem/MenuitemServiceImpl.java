package com.villysiu.yumtea.service.tea.menuitem;

import com.villysiu.yumtea.dto.request.MenuitemDto;
import com.villysiu.yumtea.models.tea.*;

import com.villysiu.yumtea.repo.tea.MenuitemRepo;
import com.villysiu.yumtea.service.tea.category.CategoryService;
import com.villysiu.yumtea.service.tea.milk.MilkService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


@Service
public class MenuitemServiceImpl implements MenuitemService {

    private final CategoryService categoryService;
    private final MenuitemRepo menuitemRepo;
    private final MilkService milkService;

    @Autowired
    MenuitemServiceImpl(MenuitemRepo menuitemRepo, CategoryService categoryService, MilkService milkService) {
        this.menuitemRepo = menuitemRepo;
        this.categoryService = categoryService;
        this.milkService = milkService;
    }

    private static final Logger logger = LoggerFactory.getLogger(MenuitemServiceImpl.class);

    @Value("${file.upload-dir}")
    private String uploadDir;
//    Create
    @Override
    public Menuitem createMenuitem(MenuitemDto menuitemDto) {
        logger.info("Create menuitem");

        Category category = categoryService.getCategoryById(menuitemDto.getCategoryId());
        Milk milk = milkService.getMilkById(menuitemDto.getMilkId());

        Menuitem menuitem = new Menuitem();

        menuitem.setTitle(menuitemDto.getTitle());
        menuitem.setDescription(menuitemDto.getDescription());
        menuitem.setImageUrl(menuitemDto.getImageUrl());
        menuitem.setPrice(menuitemDto.getPrice());

        menuitem.setCategory(category);
        menuitem.setMilk(milk);
        menuitem.setTemperature(menuitemDto.getTemperature());
        menuitem.setSugar(menuitemDto.getSugar());

        logger.info("Saving menuitem");
        menuitemRepo.save(menuitem);
        logger.info("Saved Menuitem");
        return menuitem;
    }

//    Update
    @Override
    public Menuitem updateMenuitem(Long id, Map<String, Object> menuitemDto) throws EntityNotFoundException {
        logger.info("Update menuitem");
        Menuitem menuitem = menuitemRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menuitem not found."));

        for (Map.Entry<String, Object> entry : menuitemDto.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            System.out.println(key + " = " + value);
            switch (key) {
                case "title":
                    menuitem.setTitle((String) value);
                    break;
                case "description":
                    menuitem.setDescription((String) value);
                    break;
                case "imageUrl":
                    menuitem.setImageUrl((String) value);
                    break;
                case "price":
                    Double updatePrice = Double.parseDouble(value.toString());
                    menuitem.setPrice(updatePrice);

                    break;
                case "categoryId":
                    Integer catInt = (Integer) value;
                    Long categoryId = catInt.longValue();
                    Category category = categoryService.getCategoryById(categoryId);
                    menuitem.setCategory(category);
                    break;
                case "milkId":
                    Integer mkInt = (Integer) value;
                    Long milkId = mkInt.longValue();

                    Milk milk = milkService.getMilkById(milkId);

                    menuitem.setMilk(milk);
                    break;
                case "temperature":
                    Temperature temperature = Temperature.valueOf((String) value);
                    menuitem.setTemperature(temperature);
                    break;
                case "sugar":
                    Sugar sugar = Sugar.valueOf((String) value);
                    menuitem.setSugar(sugar);
                    break;
                default:
                    break;
            }
        }

        logger.info("Saving menuitem");
        menuitemRepo.save(menuitem);
        logger.info("Saved Menuitem");
        return menuitem;
    }

//    Read
    @Override
    public List<Menuitem> getMenuitems(){
        return menuitemRepo.findAll();
    }
    @Override
    public Menuitem getMenuitemById(Long id){
        return menuitemRepo.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Menuitem not found."));
    }
    @Override
    public List<Menuitem> getMenuitemsByCategoryId(Long categoryId){
        return menuitemRepo.findByCategoryIdQuery(categoryId);
//        return menuitemRepo.findByCategoryId(categoryId);
    }
//    @Override
//    public List<BestSellerDto> getBestsellers(){
//        Pageable pageable = PageRequest.of(0, 3);
//        List<Object[]> bestsellers = menuitemRepo.findBestSellers(pageable);
//
//        List<BestSellerDto> bestSellerDtos = new ArrayList<>();
//        for (Object[] row : bestsellers) {
//            BestSellerDto dto = new BestSellerDto();
//            dto.setMenuitemId((Long) row[0]);
//            dto.setMenuitemTitle((String) row[1]);
//            dto.setCount((Long) row[2]);
//            bestSellerDtos.add(dto);
//
//        }
//
//        return bestSellerDtos;
//    }

    //    Delete
    @Override
    public void deleteMenuitem(Long id) throws RuntimeException {
        if (!menuitemRepo.existsById(id)) {
            throw new EntityNotFoundException("Entity not found with id: " + id);
        }
        logger.info("Deleting menuitem");
        menuitemRepo.deleteById(id);
        logger.info("Deleted Menuitem");

    }

    @Override
    public String saveImage(MultipartFile file) throws IOException {
        logger.info("Saving image service");
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Create the directory if it doesn't exist
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        // Save the file
        Path filePath = path.resolve(fileName);
        logger.info("ready to save image: "+filePath);
        file.transferTo(filePath.toFile());

       logger.info("Image saved in /images/" + fileName);

        // Return the file path or URL
        return "/images/" + fileName;
    }
    @Override
    public void toggleActiveMenuitem(Long id){
        logger.info("Toggle  menuitem visibility");
        Menuitem menuitem = menuitemRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Menuitem not found."));
        menuitem.setActive(!menuitem.getActive());
        menuitemRepo.save(menuitem);
    }



}


