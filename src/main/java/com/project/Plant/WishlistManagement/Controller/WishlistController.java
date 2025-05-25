package com.project.Plant.WishlistManagement.Controller;

import com.project.Plant.WishlistManagement.DTO.WishlistDTO;
import com.project.Plant.WishlistManagement.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/add")
    public ResponseEntity<WishlistDTO.WishlistResponse> addToWishlist(@RequestBody WishlistDTO.WishlistRequest request) {
        try {
            WishlistDTO.WishlistResponse response = wishlistService.addToWishlist(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // Item already in wishlist
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistDTO.WishlistResponse>> getWishlistByUserId(@PathVariable Long userId) {
        try {
            List<WishlistDTO.WishlistResponse> wishlistItems = wishlistService.getWishlistByUserId(userId);
            return new ResponseEntity<>(wishlistItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}/{itemId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long userId, @PathVariable Long itemId) {
        try {
            wishlistService.removeFromWishlist(userId, itemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}