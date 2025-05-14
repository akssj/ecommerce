package alledrogo.io.response;

import java.io.Serializable;

/**
 * Response class for api to return data in object form instead of raw data.
 */
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String seller_username;

    public ProductResponse(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeller_username() {return seller_username;}

    public void setSeller_username(String seller_username) {this.seller_username = seller_username;}
}
