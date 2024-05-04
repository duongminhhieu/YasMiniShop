package com.learning.springsecurity.auth.dto.response;

import lombok.*;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {

    @Builder.Default
    private int internalCode = 1000;
    private String message;
    private T result;
}
