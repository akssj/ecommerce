package main.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class DeleteProductRequest implements Serializable {
    @NotNull
    private Long item_id;
    @NotBlank
    private String creator_username;

    public Long getItem_id() {return item_id;}
    public String getCreator_username() {return creator_username;}

}
