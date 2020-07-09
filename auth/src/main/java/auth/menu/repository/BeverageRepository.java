package auth.menu.repository;

import auth.menu.entity.Beverage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeverageRepository extends CrudRepository<Beverage, Long> {
}
