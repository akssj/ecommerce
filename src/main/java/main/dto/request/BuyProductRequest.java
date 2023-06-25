package main.dto.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class BuyProductRequest implements Serializable {
    @NotNull
    private Long item_id;
    @NotBlank
    private String buyer_username;

    public Long getItem_id() {
        return item_id;
    }
    public String getBuyer_username() {
        return buyer_username;
    }

}
