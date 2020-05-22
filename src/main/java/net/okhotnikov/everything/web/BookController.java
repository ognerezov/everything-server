package net.okhotnikov.everything.web;

import net.okhotnikov.everything.api.in.ChapterRequest;
import net.okhotnikov.everything.service.ElasticService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergey Okhotnikov.
 */
@RestController
@RequestMapping("/book")
public class BookController {

    private final ElasticService elasticService;

    public BookController(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getChapter(@PathVariable String id) throws IOException {
        return elasticService.get(id);
    }

    @PostMapping("/read")
    public List<Map<String,Object>> getChapters (@RequestBody ChapterRequest request) throws IOException{
        request.prepare();
        return elasticService.multiGet(request.numbers);
    }

    @GetMapping("/count")
    public long count () throws IOException {
        return elasticService.countChapters();
    }

    @GetMapping("/rules")
    public List<Map<String, Object>> getRules() throws IOException {
        return elasticService.getRules();
    }
}
