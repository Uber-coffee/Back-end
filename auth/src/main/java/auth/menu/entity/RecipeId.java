package auth.menu.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class RecipeId implements Serializable {
    private Long beverageId;
    private Long componentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeId)) return false;
        RecipeId recipeId = (RecipeId) o;
        return getBeverageId().equals(recipeId.getBeverageId()) &&
                getComponentId().equals(recipeId.getComponentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBeverageId(), getComponentId());
    }
}
