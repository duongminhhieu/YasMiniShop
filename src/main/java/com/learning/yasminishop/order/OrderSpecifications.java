package com.learning.yasminishop.order;


import com.learning.yasminishop.common.entity.Order;
import com.learning.yasminishop.common.enumeration.EOrderStatus;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecifications {

    public static Specification<Order> hasStatus(EOrderStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }


    private OrderSpecifications() {
    }


}
