package alledrogo.service;

import org.springframework.beans.factory.annotation.Value;


public interface OrderProjection {

    @Value("#{target.product.name}")
    String getName();

    @Value("#{target.product.seller.username}")
    String getSeller();

    @Value("#{target.product.description}")
    String getDescription();

    @Value("#{target.product.price}")
    Double getPrice();

    String getCountry();
    String getCity();
    String getStreet();
    String getPostalCode();
    String getPaymentMethod();

    @Value("#{target.orderDate}")
    java.util.Date getPurchaseDate();
}
