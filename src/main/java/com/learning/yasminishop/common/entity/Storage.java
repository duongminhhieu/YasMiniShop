package com.learning.yasminishop.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_storage")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String type;
    private Long size;
    private String url;
}
