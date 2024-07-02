package com.learning.yasminishop.cart;

import com.learning.yasminishop.cart.dto.request.CartItemRequest;
import com.learning.yasminishop.cart.dto.request.CartItemUpdate;
import com.learning.yasminishop.cart.dto.response.CartItemResponse;
import com.learning.yasminishop.cart.mapper.CartItemMapper;
import com.learning.yasminishop.common.entity.CartItem;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;

    @Transactional
    public CartItemResponse create(CartItemRequest cartItemRequest) {

        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // check if the product is already in the cart
        Optional<CartItem> cartItem = cartItemRepository.findByProductAndUser(product, user);
        if (cartItem.isPresent()) {
            return updateExistingCartItem(cartItem.get(), cartItemRequest.getQuantity(), product);
        }

        // if the product is not in the cart
        validateProductStock(product, cartItemRequest.getQuantity());

        CartItem newCartItem = cartItemMapper.toCartItem(cartItemRequest);
        newCartItem.setProduct(product);
        newCartItem.setUser(user);
        newCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(newCartItem.getQuantity())));

        return cartItemMapper.toCartResponse(cartItemRepository.save(newCartItem));
    }


    public List<CartItemResponse> getAll() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<CartItem> cartItems = cartItemRepository.findAllByUserOrderByLastModifiedDateDesc(user);

        return cartItems.stream()
                .map(cartItemMapper::toCartResponse)
                .toList();
    }

    @Transactional
    public CartItemResponse update(String cartId, CartItemUpdate cartItemUpdate) {

        CartItem cartItem = cartItemRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        // verify the user is the owner of the cart
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!cartItem.getUser().getEmail().equals(email)) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        Product product = cartItem.getProduct();

        validateProductStock(product, cartItemUpdate.getQuantity());

        cartItem.setQuantity(cartItemUpdate.getQuantity());
        cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemUpdate.getQuantity())));
        cartItem = cartItemRepository.save(cartItem);

        return cartItemMapper.toCartResponse(cartItem);
    }

    @Transactional
    public void delete(List<String> cartIds) {

        List<CartItem> cartItems = cartItemRepository.findAllById(cartIds);

        if(cartItems.size() != cartIds.size()) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        // verify the user is the owner of the cart
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(cartItems.stream().anyMatch(cartItem -> !cartItem.getUser().getEmail().equals(email))) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        cartItemRepository.deleteAll(cartItems);
    }

    @Transactional
    public List<CartItemResponse> getCartByIds(List<String> cartIds) {
        List<CartItem> cartItems = cartItemRepository.findAllById(cartIds);

        if(cartItems.size() != cartIds.size()) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        // verify the user is the owner of the cart
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(cartItems.stream().anyMatch(cartItem -> !cartItem.getUser().getEmail().equals(email))) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        return cartItems.stream()
                .map(cartItemMapper::toCartResponse)
                .toList();
    }



    private CartItemResponse updateExistingCartItem(CartItem existingCartItem, int additionalQuantity, Product product) {
        int newQuantity = existingCartItem.getQuantity() + additionalQuantity;

        validateProductStock(product, newQuantity);
        existingCartItem.setQuantity(newQuantity);

        // update price
        existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));

        return cartItemMapper.toCartResponse(cartItemRepository.save(existingCartItem));
    }

    private void validateProductStock(Product product, int requestedQuantity) {
        if (product.getQuantity() < requestedQuantity) {
            throw new AppException(ErrorCode.PRODUCT_STOCK_NOT_ENOUGH);
        }
    }

}
