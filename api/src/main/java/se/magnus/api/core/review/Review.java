package se.magnus.api.core.review;

import lombok.Builder;

@Builder
public record Review(int productId, int reviewId, String author, String subject,
                     String content, String serviceAddress) {
}
