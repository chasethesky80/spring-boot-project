package se.magnus.api.composite.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public record RecommendationSummary(
        int recommendationId, String author, int rate)
{}
