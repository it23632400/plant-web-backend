package com.project.Plant.ItemManagement.Service;
import com.project.Plant.ItemManagement.DTO.ItemAuthRequest;
import com.project.Plant.ItemManagement.DTO.ItemAuthResponse;
import com.project.Plant.ItemManagement.DTO.ItemDetailResponse;
import com.project.Plant.ItemManagement.Entity.Item;
import com.project.Plant.ItemManagement.Repository.ItemRepository;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service

@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public ItemAuthResponse addItem(ItemAuthRequest request, MultipartFile image) {
        System.out.println("Image : " + (image != null ? image.getSize() : "null"));

        try {
            Item item = new Item();
            item.setName(request.getName());
            item.setDescription(request.getDescription());
            item.setQuantity(request.getQuantity());
            item.setPrice(request.getPrice());
            item.setCategory(request.getCategory());


            if (image != null && !image.isEmpty()) {
                item.setImage(image.getBytes());
            }

            itemRepository.save(item);




            return new ItemAuthResponse( true,"Item Added successful", item);
        } catch (IOException e) {
            return new ItemAuthResponse(false,"Error processing image: " + e.getMessage(), null);
        } catch (Exception e) {
            return new ItemAuthResponse(false, "Error during registration: " + e.getMessage(), null);
        }
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Transactional
    public boolean deleteItem(Long id) {
        try {
            Optional<Item> itemOptional = itemRepository.findById(id);

            if (itemOptional.isPresent()) {
                itemRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete item", e);
        }
    }

    @Transactional(readOnly = true)
    public ItemDetailResponse getItemById(Long itemId) {
        try {
            Optional<Item> itemOptional = itemRepository.findById(itemId);

            if (itemOptional.isEmpty()) {
                return ItemDetailResponse.builder()
                        .success(false)
                        .message("Item not found with ID: " + itemId)
                        .build();
            }

            Item item = itemOptional.get();


//            String image1Base64 = item.getImage1() != null ? Base64.getEncoder().encodeToString(item.getImage1()) : null;
//            String image2Base64 = item.getImage2() != null ? Base64.getEncoder().encodeToString(item.getImage2()) : null;
//            String image3Base64 = item.getImage3() != null ? Base64.getEncoder().encodeToString(item.getImage3()) : null;
//            String image4Base64 = item.getImage4() != null ? Base64.getEncoder().encodeToString(item.getImage4()) : null;

            return ItemDetailResponse.builder()
                    .success(true)
                    .message("Item retrieved successfully")
                    .item(item)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ItemDetailResponse.builder()
                    .success(false)
                    .message("Error retrieving item: " + e.getMessage())
                    .build();
        }
    }
}
