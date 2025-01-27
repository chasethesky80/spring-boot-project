package se.magnus.api.core.recommendation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public record Recommendation(int productId, int recommendationId, String author,
                             int rate, String content, String serviceAddress) {
}
