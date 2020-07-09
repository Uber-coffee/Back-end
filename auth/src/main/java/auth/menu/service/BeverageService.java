package auth.menu.service;

import auth.menu.entity.Component;
import auth.menu.entity.RecipeCustomerRelationship;
import auth.menu.repository.RecipeCustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeCustomerService {
    private RecipeCustomerRepository recipeCustomerRepository;
    private ComponentService componentService;

    public RecipeCustomerService(RecipeCustomerRepository recipeCustomerRepository, ComponentService componentService) {
        this.recipeCustomerRepository = recipeCustomerRepository;
        this.componentService = componentService;
    }

    public RecipeCustomerRelationship addRecipeCustomerRelationship(RecipeCustomerRelationship recipeCustomerRelationship) {
        recipeCustomerRelationship.getComponents().forEach(component -> {
            long i = 1;
            component.setComponent(componentService.getComponent(i));
            i++;
            component.setRecipeCustomerRelationship(recipeCustomerRelationship);
        });
        return recipeCustomerRepository.save(recipeCustomerRelationship);
    }

    public List<RecipeCustomerRelationship> getRecipeCustomerRelationship() {
        return (List<RecipeCustomerRelationship>)recipeCustomerRepository.findAll();
    }
}
