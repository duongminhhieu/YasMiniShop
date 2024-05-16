package com.learning.yasminishop.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResponse<T> {
    private Long total;
    private int page;
    private int itemsPerPage;
    private List<T> data;
}
