package com.learning.yasminishop.order;

import com.learning.yasminishop.common.entity.Order;
import com.learning.yasminishop.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByUserOrderByLastModifiedDateDesc(User user);
}
