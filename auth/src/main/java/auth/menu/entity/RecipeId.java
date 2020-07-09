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
        if (o == null || getClass() != o.getClass()) return false;
        RecipeId recipeId = (RecipeId) o;
        return Objects.equals(beverageId, recipeId.beverageId) &&
                Objects.equals(componentId, recipeId.componentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beverageId, componentId);
    }
}
