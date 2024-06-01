package com.learning.yasminishop.user.dto.response;

import com.learning.yasminishop.role.dto.response.RoleResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAdminResponse {
    String id;
    String email;
    String firstName;
    String lastName;
    LocalDate dob;
    Boolean isActive;
    Set<RoleResponse> roles;

    String createdBy;
    LocalDateTime createdDate;
    String lastModifiedBy;
    LocalDateTime lastModifiedDate;
}
