package com.project.Plant.WishlistManagement.Service;

import com.project.Plant.ItemManagement.Entity.Item;
import com.project.Plant.ItemManagement.Repository.ItemRepository;
import com.project.Plant.UserManagement.Entity.UserEntity;
import com.project.Plant.UserManagement.Repository.UserRepository;
import com.project.Plant.WishlistManagement.DTO.WishlistDTO;
import com.project.Plant.WishlistManagement.Entity.Wishlist;
import com.project.Plant.WishlistManagement.Repository.WishlistRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public WishlistService(
            WishlistRepository wishlistRepository,
            UserRepository userRepository,
            ItemRepository itemRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public WishlistDTO.WishlistResponse addToWishlist(WishlistDTO.WishlistRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + request.getUserId()));

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + request.getItemId()));

        // Check if item is already in wishlist
        if (wishlistRepository.findByUserIdAndItemId(request.getUserId(), request.getItemId()).isPresent()) {
            throw new IllegalStateException("Item is already in wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setItem(item);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return convertToResponseDTO(savedWishlist);
    }

    public List<WishlistDTO.WishlistResponse> getWishlistByUserId(Long userId) {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

        List<Wishlist> wishlistItems = wishlistRepository.findByUserId(userId);
        return wishlistItems.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long itemId) {
        // Check if item exists in the user's wishlist
        Wishlist wishlist = wishlistRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found in wishlist"));

        wishlistRepository.delete(wishlist);
    }

    private WishlistDTO.WishlistResponse convertToResponseDTO(Wishlist wishlist) {
        Item item = wishlist.getItem();

        return new WishlistDTO.WishlistResponse(
                wishlist.getId(),
                wishlist.getUser().getId(),
                item.getItemId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getCategory(),
                item.getImage()
        );
    }
}