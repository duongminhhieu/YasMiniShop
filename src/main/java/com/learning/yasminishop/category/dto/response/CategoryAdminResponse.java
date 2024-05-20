package com.learning.yasminishop.category.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryAdminResponse {
    private String id;
    private String name;
    private String slug;
    private String description;
    private Boolean isAvailable;
    private Integer productCount;
}
