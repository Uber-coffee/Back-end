package auth.menu.repository;

import auth.menu.entity.RecipeCustomerRelationship;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeCustomerRepository extends CrudRepository<RecipeCustomerRelationship, Long> {
}
