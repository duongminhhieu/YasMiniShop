package com.learning.yasminishop.product.dto.payload;

import com.learning.yasminishop.common.validator.FieldNotEmpty.FieldNotEmptyConstraint;
import com.learning.yasminishop.common.validator.FieldNotNull.FieldNotNullConstraint;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterProductPayload {


    @FieldNotEmptyConstraint(field = "categorySlug", message = "FIELD_NOT_EMPTY")
    String categorySlug;


    @FieldNotNullConstraint(field = "page", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "page", message = "FIELD_NOT_EMPTY")
    @Positive( message = "PAGE_MUST_BE_POSITIVE")
    Integer page;

    @FieldNotNullConstraint(field = "page", message = "FIELD_NOT_NULL")
    @FieldNotEmptyConstraint(field = "page", message = "FIELD_NOT_EMPTY")
    @Positive( message = "ITEMS_PER_PAGE_MUST_BE_POSITIVE")
    Integer itemsPerPage;
}
