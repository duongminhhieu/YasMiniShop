package com.learning.yasminishop.yasminiai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.generativeai.*;
import com.learning.yasminishop.common.entity.Product;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.product.ProductRepository;
import com.learning.yasminishop.product.dto.response.ProductResponse;
import com.learning.yasminishop.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class YasMiniAIService {
    private final ChatSession chatSession;

    private final GenerativeModel generativeModel;
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;


    @PreAuthorize("hasRole('USER')")
    public String generateText(String prompt){
        try {
            GenerateContentResponse generateContentResponse = chatSession.sendMessage(prompt);
            return ResponseHandler.getText(generateContentResponse);
        } catch (Exception e) {
            log.error("Error generating text for prompt: {}", prompt, e);
            throw new AppException(ErrorCode.GENERATIVE_AI_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER')")
    public List<String> generateTextWithHistory(String prompt){
        try {
            this.chatSession.sendMessage(prompt);

            List<Content> history = this.chatSession.getHistory();

            return history.stream().flatMap(h -> h.getPartsList().stream()).map(Part::getText).toList();
        } catch (Exception e) {
            log.error("Error generating text for prompt: {}", prompt, e);
            throw new AppException(ErrorCode.GENERATIVE_AI_ERROR);
        }
    }

   @PreAuthorize("hasRole('USER')")
    public List<ProductResponse> findCarByImage(MultipartFile file){
        try {
            var prompt = "Extract the name car to a list keyword and output them in JSON. If you don't find any information about the car, please output the list empty.\nExample response: [\"rolls\", \"royce\", \"wraith\"]";
            var content = this.generativeModel.generateContent(
                    ContentMaker.fromMultiModalData(
                            PartMaker.fromMimeTypeAndData(Objects.requireNonNull(file.getContentType()), file.getBytes()),
                            prompt
                    )
            );

            String jsonContent = ResponseHandler.getText(content);
            log.info("Extracted keywords from image: {}", jsonContent);
            List<String> keywords = convertJsonToList(jsonContent).stream()
                    .map(String::toLowerCase)
                    .toList();

            Set<Product> results = new HashSet<>();
            for (String keyword : keywords) {
                List<Product> products = productRepository.searchByKeyword(keyword);
                results.addAll(products);
            }

            return results.stream()
                    .map(productMapper::toProductResponse)
                    .toList();

        } catch (Exception e) {
            log.error("Error finding car by image", e);
            return List.of();
        }
    }

    private List<String> convertJsonToList(String markdown) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String parseJson = markdown;
        if(markdown.contains("```json")){
            parseJson = extractJsonFromMarkdown(markdown);
        }
        return objectMapper.readValue(parseJson, List.class);
    }

    private String extractJsonFromMarkdown(String markdown) {
        return markdown.replace("```json\n", "").replace("\n```", "");
    }
}
