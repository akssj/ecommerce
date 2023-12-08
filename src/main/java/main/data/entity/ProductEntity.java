package main.data.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "t_product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private Integer price;
    @Column(name = "description")
    private String description;
    @Column(name = "creator")
    private String creator;
    @Column(name = "buyer")
    private String buyer;

    public ProductEntity() {}
    public ProductEntity(String name, Integer price, String description, String creator) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.creator = creator;
        this.buyer = "";
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Integer getPrice() {return price;}
    public void setPrice(Integer price) {this.price = price;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getBuyer() {return buyer;}
    public void setBuyer(String buyer) {this.buyer = buyer;}
}
