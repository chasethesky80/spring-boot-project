package se.magnus.api.core.recommendation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface RecommendationService {

    @GetMapping(value="/recommendations", produces = "application/json")
    List<Recommendation> getRecommendation(
            @RequestParam(value = "productId", required = true) int productId
    );
}
