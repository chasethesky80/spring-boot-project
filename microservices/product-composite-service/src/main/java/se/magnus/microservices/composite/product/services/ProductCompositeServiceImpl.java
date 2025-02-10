package se.magnus.microservices.composite.product.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import se.magnus.api.composite.product.*;
import se.magnus.api.core.exceptions.NotFoundException;
import se.magnus.api.core.product.Product;
import se.magnus.api.core.recommendation.Recommendation;
import se.magnus.api.core.review.Review;
import se.magnus.util.http.ServiceUtil;

import java.util.List;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {

    private final ServiceUtil serviceUtil;

    private final ProductCompositeIntegration productCompositeIntegration;

    @Autowired
    public ProductCompositeServiceImpl(final ServiceUtil serviceUtil,
                                final ProductCompositeIntegration productCompositeIntegration) {
        this.serviceUtil = serviceUtil;
        this.productCompositeIntegration = productCompositeIntegration;
    }

    @Override
    public ProductAggregate getProduct(int productId) {
        final Product product = productCompositeIntegration.getProduct(productId);
        if (product == null) {
            throw new NotFoundException("No product found for productId "+ productId);
        }
        final List<Recommendation> recommendationList =
                productCompositeIntegration.getRecommendation(productId);
        final List<Review> reviews = productCompositeIntegration.getReviews(productId);
        return createProductAggregate(product, recommendationList, reviews,
                serviceUtil.getServiceAddress());
    }

    private ProductAggregate createProductAggregate(final Product product,
                                                    final List<Recommendation> recommendations,
                                                    final List<Review> reviews,
                                                    final String serviceAddress) {
        int productId = product.productId();
        String name = product.name();
        int weight = product.weight();

        final List<RecommendationSummary> recommendationSummaries = recommendations.stream()
                .map(recommendation -> new RecommendationSummary(
                        recommendation.recommendationId(), recommendation.author(),
                        recommendation.rate())).toList();
        final List<ReviewSummary> reviewSummaries = reviews.stream().map(review ->
                new ReviewSummary(review.reviewId(), review.author(), review.subject())).toList();
        final String productAddress = product.serviceAddress();
        final String reviewAddress = !reviews.isEmpty() ? reviews.get(0).serviceAddress() : "";
        final String recommendationAddress = !recommendations.isEmpty() ? recommendations.get(0).serviceAddress() : "";
        final ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress,
                reviewAddress, recommendationAddress);
        return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries,
                serviceAddresses);

    }
}
