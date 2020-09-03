package net.okhotnikov.everything.web;

import net.okhotnikov.everything.service.ElasticService;
import net.okhotnikov.everything.util.DataUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Sergey Okhotnikov.
 */
@RestController
@RequestMapping("/free")
public class FreeBookController {

    private final ElasticService elasticService;

    public FreeBookController(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    @GetMapping("/quotations")
    public List<Map<String, Object>> getQuotations() throws IOException {
        return elasticService.getQuotations();
    }

    @GetMapping("/day")
    public List<Map<String, Object>> getNumberOfTheDay() throws IOException {
        return elasticService.getNumberOfTheDay();
    }
}
