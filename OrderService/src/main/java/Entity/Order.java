package Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "user_id")
    Long userId;
    @Column(name = "order_date")
    LocalDateTime orderDate;
    @NotBlank
    @Column(name = "order_status")
    String orderStatus;
    @Column(name = "order_amount")
    BigDecimal orderAmount;
    @Column(name = "product_id")
    Long productId;
    @Column(name = "product_quantity")
    Long productQuantity;
    @Column(name = "total_product_price", precision = 10, scale = 2)
    BigDecimal totalPriceOnProduct;
    @Column(name = "product_price", precision = 10, scale = 2)
    BigDecimal productPrice;
    @Column(name = "total_price", precision = 10, scale = 2)
    BigDecimal totalPriceOnOrder;
}
