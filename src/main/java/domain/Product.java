package domain;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "product.all", query = "SELECT p FROM Product p"),
        @NamedQuery(name = "product.id", query = "SELECT p FROM Product p WHERE p.id=:productId"),
        @NamedQuery(name = "product.findByPrice", query = "SELECT p FROM Product p WHERE p.price BETWEEN :priceFrom AND :priceTo"),
        @NamedQuery(name = "product.findByName", query = "SELECT p FROM Product p WHERE p.name=:productName"),
        @NamedQuery(name = "product.findByCategory", query = "SELECT p FROM Product p WHERE p.category=:productCategory")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;
    private String category;

    @JsonbTransient
    @OneToMany(mappedBy="product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int prize) {
        this.price = prize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
