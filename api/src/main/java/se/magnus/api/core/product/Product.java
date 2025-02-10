package se.magnus.api.core.product;

import lombok.Builder;

@Builder
public record Product(int productId, String name, int weight, String serviceAddress) {
}
