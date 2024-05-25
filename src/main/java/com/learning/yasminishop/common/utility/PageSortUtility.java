package com.learning.yasminishop.common.utility;

import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PageSortUtility {

    public List<Sort.Order> createSortOrders(String[] orderBy, String sortBy) {
        List<Sort.Order> orders = new ArrayList<>();
        Optional<Sort.Direction> direction = parseSortDirection(sortBy);

        if (direction.isPresent() && orderBy != null) {
            for (String order : orderBy) {
                orders.add(new Sort.Order(direction.get(), order));
            }
        }

        return orders;
    }

    public Pageable createPageable(Integer page, Integer itemsPerPage, String sortBy, String[] orderBy) {

        if (page == null || itemsPerPage == null) {
            throw new AppException(ErrorCode.INVALID_PAGEABLE);
        }

        List<Sort.Order> orders = createSortOrders(orderBy, sortBy);

        return PageRequest.of(page - 1, itemsPerPage, Sort.by(orders));
    }

    public Optional<Sort.Direction> parseSortDirection(String sortBy) {
        try {
            return Optional.of(Sort.Direction.fromString(sortBy));
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_SORT_DIRECTION);
        }
    }
}
