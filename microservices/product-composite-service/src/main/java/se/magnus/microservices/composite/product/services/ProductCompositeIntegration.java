package se.magnus.microservices.composite.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.magnus.api.core.exceptions.InvalidInputException;
import se.magnus.api.core.exceptions.NotFoundException;
import se.magnus.api.core.product.Product;
import se.magnus.api.core.product.ProductService;
import se.magnus.api.core.recommendation.Recommendation;
import se.magnus.api.core.recommendation.RecommendationService;
import se.magnus.api.core.review.Review;
import se.magnus.api.core.review.ReviewService;
import se.magnus.util.http.HttpErrorInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    @Autowired
    public ProductCompositeIntegration(
        RestTemplate restTemplate,
        ObjectMapper objectMapper,
        @Value("${app.product-service.host}")  String productServiceHost,
        @Value("${app.product-service.port}") int productServicePort,
        @Value("${app.review-service.host}")  String reviewServiceHost,
        @Value("${app.review-service.port}") int reviewServicePort,
        @Value("${app.recommendation-service.host}")  String recommendationServiceHost,
        @Value("${app.recommendation-service.port}") int recommendationServicePort
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.productServiceUrl = String.format("http:// %s:%s/product/", productServiceHost, productServicePort);
        this.recommendationServiceUrl = String.format("http:// %s:%s/recommendation?productId=", recommendationServiceHost,
                recommendationServicePort);
        this.reviewServiceUrl = String.format("http:// %s:%s/review?productId=", reviewServiceHost, reviewServicePort);
    }

    public Product getProduct(int productId) {
        try {
            final String url = productServiceUrl + productId;
            LOG.debug("Will call getProduct API on URL: {}", url);
            final Product product = restTemplate.getForObject(url, Product.class);
            LOG.debug("Found a product with id {}", productId);
            return product;
        } catch (HttpClientErrorException ex) {
            switch (HttpStatus.resolve(ex.getStatusCode().value())) {
                case NOT_FOUND -> throw new NotFoundException(getErrorMessage(ex));
                case UNPROCESSABLE_ENTITY ->
                    throw new InvalidInputException(getErrorMessage(ex));
                default ->
                    LOG.warn("Received an unexpected error: {} will rethrow it",
                            ex.getStatusCode());
            }
        }
        return null;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        try {
            String url = recommendationServiceUrl + productId;
            LOG.debug("Will call recommendations API on url {}", url);
            final List<Recommendation> recommendations = restTemplate.
                    exchange(url, GET, null,
                            new ParameterizedTypeReference<List<Recommendation>>() {
                    }).getBody();
            LOG.debug("Found {} recommendations for a product with id: {}",
                      recommendations.size(), productId);
            return recommendations;
        } catch (Exception ex) {
            LOG.warn("Got an exception when retrieving recommendations " +
                    "returning zero recommendations: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Review> getReviews(int productId) {
        try {
            String url = reviewServiceUrl + productId;
            LOG.debug("Will call reviews API on url {}", url);
            final List<Review> reviews = restTemplate.
                    exchange(url, GET, null,
                            new ParameterizedTypeReference<List<Review>>() {
                            }).getBody();
            LOG.debug("Found {} reviews for a product with id: {}",
                    reviews.size(), productId);
            return reviews;
        } catch (Exception ex) {
            LOG.warn("Got an exception when retrieving reviews " +
                    "returning zero reviews: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class)
                    .message();
        } catch (IOException ioe) {
            return ioe.getMessage();
        }
    }
}
