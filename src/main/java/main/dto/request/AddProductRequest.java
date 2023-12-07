package main.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AddProductRequest implements Serializable {
    @NotBlank
    private String name;
    @NotNull
    private Integer price;
    @NotBlank
    private String description;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Integer getPrice() {return price;}
    public void setPrice(Integer price) {this.price = price;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

}
