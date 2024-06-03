package com.learning.yasminishop.yasminiai;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class YasMiniAIController {

    private final YasMiniAIService yasMiniAIService;


    @PostMapping
    public APIResponse<List<ProductResponse>> findCar(@RequestParam("file") MultipartFile file) {

        var response = yasMiniAIService.findCarByImage(file);
        return APIResponse.<List<ProductResponse>>builder()
                .result(response)
                .build();
    }


    @GetMapping("/{text}")
    public APIResponse<String> chat(@PathVariable String text) {
       String chatResponse = yasMiniAIService.generateText(text);
       return APIResponse.<String>builder()
               .result(chatResponse)
               .build();
    }

    @GetMapping("history/{text}")
    public APIResponse<List<String>> getChatHistory(@PathVariable String text) {

        var response = yasMiniAIService.generateTextWithHistory(text);
        return APIResponse.<List<String>>builder()
                .result(response)
                .build();
    }
}
