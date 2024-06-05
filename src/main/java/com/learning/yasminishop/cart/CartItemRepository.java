package com.learning.yasminishop.cart;

import com.learning.yasminishop.common.entity.CartItem;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String>{
    Optional<CartItem> findByProductAndUser(Product product, User user);
    List<CartItem> findAllByUserOrderByLastModifiedDateDesc(User user);
    List<CartItem> findAllByUserAndProductIsAvailable(User user, Boolean isAvailable);
}
