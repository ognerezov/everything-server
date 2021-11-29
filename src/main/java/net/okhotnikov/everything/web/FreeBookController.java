package net.okhotnikov.everything.web;

import net.okhotnikov.everything.api.out.GenericResponse;
import net.okhotnikov.everything.service.ElasticService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergey Okhotnikov.
 */
@RestController
@RequestMapping("/free")
public class FreeBookController {

    private final int max;
    private final ElasticService elasticService;

    public FreeBookController(@Value("${app.max}") int max, ElasticService elasticService) {
        this.max = max;
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

    @GetMapping("max")
    public GenericResponse<Integer> getMaxNumber(){
        return new GenericResponse<>(max);
    }
}
