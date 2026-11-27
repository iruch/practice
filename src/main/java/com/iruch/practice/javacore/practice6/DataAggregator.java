package com.iruch.practice.javacore.practice6;

import com.iruch.practice.javacore.practice6.service.DatabaseIntegration;
import com.iruch.practice.javacore.practice6.service.IntegrationService;
import com.iruch.practice.javacore.practice6.service.database.DescriptionDB;
import com.iruch.practice.javacore.practice6.service.database.PriceDB;
import com.iruch.practice.javacore.practice6.service.database.RatingDB;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataAggregator implements AutoCloseable {

    private final ExecutorService executor;
    private final IntegrationService<Double> priceService;
    private final IntegrationService<String> descriptionService;
    private final IntegrationService<Double> ratingService;

    public DataAggregator() {
        this.executor = Executors.newFixedThreadPool(4);
        this.priceService = new IntegrationService<>(PriceDB.getInstance());
        this.descriptionService = new IntegrationService<>(DescriptionDB.getInstance());
        this.ratingService = new IntegrationService<>(RatingDB.getInstance());
    }

    public ProductInfo aggregateProductInfo(String productName) {

        CompletableFuture<Double> priceFuture = CompletableFuture
                .supplyAsync(() -> getProductValue(priceService, productName), executor).exceptionally(ex -> 0.00);
        CompletableFuture<String> descriptionFuture = CompletableFuture
                .supplyAsync(() -> getProductValue(descriptionService, productName), executor).exceptionally(ex -> "Нет данных");
        CompletableFuture<Double> ratingFuture = CompletableFuture
                .supplyAsync(() -> getProductValue(ratingService, productName), executor).exceptionally(ex -> 0.0);

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(priceFuture, descriptionFuture, ratingFuture);
        CompletableFuture<ProductInfo> productInfo = allFutures
                .thenApply(v -> {
                            Double price = priceFuture.join();
                            String description = descriptionFuture.join();
                            Double rating = ratingFuture.join();
                            return new ProductInfo(productName, description, price, rating);
                        }
                );
        return productInfo.join();
    }

    private <T> T getProductValue(DatabaseIntegration<T> service, String productName) {
        try {
            return service.fetchValue(productName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        executor.close();
    }
}
