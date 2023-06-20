package main.dto.request;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class BuyOutProductRequest implements Serializable {
    @NotBlank
    private Long item_id;
    @NotBlank
    private String buyer_username;

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }

    public String getBuyer_username() {
        return buyer_username;
    }

    public void setBuyer_username(String buyer_username) {
        this.buyer_username = buyer_username;
    }

}
