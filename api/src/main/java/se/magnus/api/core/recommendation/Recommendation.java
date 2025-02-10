package se.magnus.api.core.recommendation;

import lombok.Builder;

@Builder
public record Recommendation(int productId, int recommendationId, String author,
                             int rate, String content, String serviceAddress) {
}
