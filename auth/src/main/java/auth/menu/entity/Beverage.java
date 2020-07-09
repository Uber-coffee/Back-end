package auth.menu.entity;



import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class RecipeCustomerRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @Column(name = "beverage_name", nullable = false)
    private String beverageName;

    @Column(nullable = false)
    private Double price;

    @Column(name = "customer_id")
    private Long customerId;

    @OneToMany(mappedBy = "recipeCustomerRelationship", cascade = CascadeType.ALL)
    private List<Recipe> components = new ArrayList<>();
}
