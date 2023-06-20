package main.dto.request;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class DeleteProductRequest implements Serializable {
    @NotBlank
    private Long item_id;
    @NotBlank
    private String creator_username;

    public Long getItem_id() {return item_id;}
    public void setItem_id(Long item_id) {this.item_id = item_id;}
    public String getCreator_username() {return creator_username;}
    public void setCreator_username(String creator_username) {this.creator_username = creator_username;}

}
