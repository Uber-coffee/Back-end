package auth.menu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Recipe {
    @EmbeddedId
    @JsonIgnore
    private RecipeId recipeId = new RecipeId();

    @ManyToOne()
    @MapsId("beverageId")
    @JoinColumn(name = "beverage_id")
    @JsonIgnore
    private Beverage beverage;

    @ManyToOne(cascade = CascadeType.MERGE)
    @MapsId("componentId")
    @JoinColumn(name = "component_id")
    private Component component;

    private Long quantity;
}