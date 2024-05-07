package com.learning.springsecurity.role;


import com.learning.springsecurity.permission.Permission;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "t_role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    private String name;

    private String description;

    @ManyToMany
    private Set<Permission> permissions;


}
