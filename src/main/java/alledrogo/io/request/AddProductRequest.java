package alledrogo.io.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Request class, provides object instead of raw data for api params
 */
public class AddProductRequest{
    @NotBlank
    private String newProductName;
    @NotNull
    private Float newProductPrice;
    @NotBlank
    private String newProductDescription;
    @NotBlank
    private String newProductCategory;


    public String getNewProductName() {return newProductName;}
    public void setNewProductName(String newProductName) {this.newProductName = newProductName;}
    public Float getNewProductPrice() {return newProductPrice;}
    public void setNewProductPrice(Float newProductPrice) {this.newProductPrice = newProductPrice;}
    public String getNewProductDescription() {return newProductDescription;}
    public void setNewProductDescription(String newProductDescription) {this.newProductDescription = newProductDescription;}
    public String getNewProductCategory() {return newProductCategory;}
    public void setNewProductCategory(String newProductCategory) {this.newProductCategory = newProductCategory;}
}
