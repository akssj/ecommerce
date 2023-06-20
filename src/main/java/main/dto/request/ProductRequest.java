package main.dto.request;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class ProductRequest implements Serializable {
    @NotBlank
    private String name;
    @NotBlank
    private Integer price;
    @NotBlank
    private String description;
    @NotBlank
    private String creator_username;


    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
    public Integer getPrice() {return price;}
    public void setPrice(Integer price) {this.price = price;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getCreator_username() {return creator_username;}
    public void setCreator_username(String creator_username) {this.creator_username = creator_username;}
}
