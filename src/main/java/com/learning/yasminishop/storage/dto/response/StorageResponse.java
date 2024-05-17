package com.learning.yasminishop.storage.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StorageResponse {

    String id;
    String name;
    String url;
    String type;
    long size;

}
