package com.project.Plant.ItemManagement.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Plant.ItemManagement.DTO.ItemAuthRequest;
import com.project.Plant.ItemManagement.DTO.ItemAuthResponse;
import com.project.Plant.ItemManagement.DTO.ItemDetailResponse;
import com.project.Plant.ItemManagement.DTO.ResponseDTO;
import com.project.Plant.ItemManagement.Entity.Item;
import com.project.Plant.ItemManagement.Service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/add-item", consumes = {"multipart/form-data"})
    public ResponseEntity<ItemAuthResponse> register(
            @RequestParam("item") String itemJson,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        System.out.println("Received item JSON: " + itemJson);
        System.out.println("Image is null: " + (image == null));

        try {
            ItemAuthRequest itemRequest = objectMapper.readValue(itemJson, ItemAuthRequest.class);

            return ResponseEntity.ok(itemService.addItem(itemRequest, image));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ItemAuthResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping(value = "get-items")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteItem(@PathVariable Long id) {
        try {
            boolean deleted = itemService.deleteItem(id);
            if (deleted) {
                return ResponseEntity.ok(
                        ResponseDTO.builder()
                                .status("success")
                                .message("Item deleted successfully")
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseDTO.builder()
                                .status("error")
                                .message("Item not found")
                                .build()
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseDTO.builder()
                            .status("error")
                            .message("Error deleting item: " + e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ItemDetailResponse> getItemById(@PathVariable Long id) {
        try {
            ItemDetailResponse response = itemService.getItemById(id);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ItemDetailResponse.builder()
                            .success(false)
                            .message("Error retrieving item: " + e.getMessage())
                            .build()
            );
        }
    }
}
