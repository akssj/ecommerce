package main.io.response;

import java.io.Serializable;

public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String creator_username;

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

    public String getCreator_username() {return creator_username;}

    public void setCreator_username(String creator_username) {this.creator_username = creator_username;}
}
