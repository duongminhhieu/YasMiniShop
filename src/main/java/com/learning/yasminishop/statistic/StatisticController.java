package com.learning.yasminishop.statistic;

import com.learning.yasminishop.common.dto.APIResponse;
import com.learning.yasminishop.statistic.dto.response.StatisticResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatisticController {

    private final StatisticService statisticService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<StatisticResponse> getStatistics() {

        var statistics = statisticService.getStatistics();

        return APIResponse.<StatisticResponse>builder()
                .result(statistics)
                .build();
    }

}
