package com.learning.yasminishop.storage;

import com.learning.yasminishop.common.constant.StorageFolder;
import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.storage.dto.response.StorageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@Slf4j
public class StorageController {
    private final StorageService storageService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<StorageResponse> saveProductFile(@RequestParam("file") MultipartFile file) {
        log.info("Saving file: {}", file.getOriginalFilename());
        StorageResponse storageResponse = storageService.saveFile(file, StorageFolder.PRODUCTS);
        return APIResponse.<StorageResponse>builder()
                .result(storageResponse)
                .build();
    }

}
