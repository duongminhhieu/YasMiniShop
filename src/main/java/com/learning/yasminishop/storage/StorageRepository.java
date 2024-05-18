package com.learning.yasminishop.storage;

import com.learning.yasminishop.common.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, String>{
}
