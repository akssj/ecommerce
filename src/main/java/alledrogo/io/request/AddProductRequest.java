package alledrogo.io.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Request class, provides object instead of raw data for api params
 */
public class AddProductRequest{
    @NotBlank
    private String name;
    @NotNull
    private Integer price;
    @NotBlank
    private String description;
    @NotBlank
    private String category;


    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Integer getPrice() {return price;}
    public void setPrice(Integer price) {this.price = price;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}
}
