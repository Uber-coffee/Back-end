package auth.menu.controller;

import auth.menu.entity.Recipe;
import auth.menu.entity.RecipeCustomerRelationship;
import auth.menu.repository.RecipeCustomerRepository;
import auth.menu.service.RecipeCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/w/beverages")
public class RecipeCustomerController {
    private RecipeCustomerService recipeCustomerService;

    public RecipeCustomerController(RecipeCustomerService recipeCustomerService) {
        this.recipeCustomerService = recipeCustomerService;
    }

    @PostMapping
    public ResponseEntity<RecipeCustomerRelationship> addRecipe(@RequestBody RecipeCustomerRelationship recipeCustomerRelationship) {
        return new ResponseEntity<>(recipeCustomerService.addRecipeCustomerRelationship(recipeCustomerRelationship), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RecipeCustomerRelationship>> getRecipes() {
        return new ResponseEntity<>(recipeCustomerService.getRecipeCustomerRelationship(), HttpStatus.OK);
    }
}
