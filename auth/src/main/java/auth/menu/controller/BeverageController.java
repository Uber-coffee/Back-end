package auth.menu.controller;

import auth.menu.entity.Beverage;
import auth.menu.service.BeverageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/w/beverages")
public class BeverageController {
    private BeverageService beverageService;

    public BeverageController(BeverageService beverageService) {
        this.beverageService = beverageService;
    }

    @PostMapping
    public ResponseEntity<Beverage> addBeverage(@RequestBody Beverage beverage) {
        return new ResponseEntity<>(beverageService.addBeverage(beverage), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beverage> getBeverage(@PathVariable Long id) {
        return new ResponseEntity<>(beverageService.getBeverage(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Beverage>> getBeverages() {
        return new ResponseEntity<>(beverageService.getBeverages(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBeverage(@PathVariable Long id) {
        beverageService.deleteBeverage(id);
    }
}
