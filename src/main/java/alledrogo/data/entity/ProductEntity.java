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
    @JoinColumn(name = "creator")
    private UserEntity creator;
    @ManyToOne
    @JoinColumn(name = "buyer")
    private UserEntity buyer;

    public ProductEntity() {}
    public ProductEntity(String name, Float price, String description, String category, UserEntity creator) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.creator = creator;
        this.buyer = null;
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
    public UserEntity getCreator() {return creator;}
    public void setCreator(UserEntity creator) {this.creator = creator;}
    public UserEntity getBuyer() {return buyer;}
    public void setBuyer(UserEntity buyer) {this.buyer = buyer;}
}
