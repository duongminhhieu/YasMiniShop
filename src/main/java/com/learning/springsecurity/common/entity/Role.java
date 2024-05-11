package com.learning.springsecurity.common.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "t_role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(name = "Role.permissions", attributeNodes = @NamedAttributeNode("permissions"))
public class Role extends AuditEntity<String> {

    @Id
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Permission> permissions;


}
