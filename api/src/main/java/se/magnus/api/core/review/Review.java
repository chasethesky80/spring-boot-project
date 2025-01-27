package se.magnus.api.core.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public record Review(int productId, int reviewId, String author, String subject,
                     String content, String serviceAddress) {
}
