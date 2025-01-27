package se.magnus.api.core.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public record Product(int productId, String name, int weight, String serviceAddress) {
}
