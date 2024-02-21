package alledrogo.data.entity;

import jakarta.persistence.*;

/**
 * ProductEntity class
 */
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @Column(name = "product_name")
    private String name;
    @Column(name = "product_price")
    private Float price;
    @Column(name = "product_description")
    private String description;
    @Column(name = "product_category")
    private String category;
    @ManyToOne
    @JoinColumn(name = "seller")
    private UserEntity seller;
    @ManyToOne
    @JoinColumn(name = "buyer")
    private UserEntity buyer;
    @Column(name = "sold")
    private boolean sold;

    public ProductEntity() {}
    public ProductEntity(String name, Float price, String description, String category, UserEntity seller) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.seller = seller;
        this.buyer = null;
        this.sold = false;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Float getPrice() {return price;}
    public void setPrice(Float price) {this.price = price;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}
    public UserEntity getSeller() {return seller;}
    public void setSeller(UserEntity seller) {this.seller = seller;}
    public UserEntity getBuyer() {return buyer;}
    public void setBuyer(UserEntity buyer) {this.buyer = buyer;}
    public boolean isSold() {return sold;}
    public void setSold(boolean sold) {this.sold = sold;}
}
