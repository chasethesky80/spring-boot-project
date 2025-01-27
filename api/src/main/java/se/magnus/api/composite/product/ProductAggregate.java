package se.magnus.api.composite.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public record ProductAggregate(int productId, String name, int weight,
                               List<RecommendationSummary> recommendations,
                               List<ReviewSummary> reviews,
                               ServiceAddresses serviceAddresses) {
}
