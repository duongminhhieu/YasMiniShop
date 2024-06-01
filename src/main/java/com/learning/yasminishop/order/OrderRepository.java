package com.learning.yasminishop.order;

import com.learning.yasminishop.common.entity.Order;
import com.learning.yasminishop.common.entity.User;
import com.learning.yasminishop.common.enumeration.EOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByUserOrderByLastModifiedDateDesc(User user);

    Optional<Order> findByIdAndUser(String id, User user);

    Long countByStatus(EOrderStatus status);

    List<Order> findTop10ByOrderByCreatedDateDesc();

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status = :status")
    BigDecimal sumTotalAmountByStatus(EOrderStatus status);
}
