package com.learning.yasminishop.storage.mapper;

import com.learning.yasminishop.common.entity.Storage;
import com.learning.yasminishop.storage.dto.response.StorageResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StorageMapper {

    StorageResponse toStorageResponse(Storage storage);
}
