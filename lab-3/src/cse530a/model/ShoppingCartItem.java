package cse530a.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "shopping_cart_items")
@SequenceGenerator(name = "SHOPPING_CART_ITEM_SEQ", sequenceName = "shopping_cart_items_item_id_seq")
public class ShoppingCartItem implements Serializable {
    
	private static final long serialVersionUID = 1255764954799931790L;
	
	private Long id;
    private ShoppingCart shoppingCart;
    private Book book;
    private Integer quantity;

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SHOPPING_CART_ITEM_SEQ")
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn (name = "cart_id")
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @ManyToOne
    @JoinColumn (name = "book_id")
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    
    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
