package auth.menu.controller;

import auth.config.swagger2.SwaggerMethodToDocument;
import auth.menu.entity.Beverage;
import auth.menu.service.BeverageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/w/beverages")
@Api

public class BeverageController {
    private BeverageService beverageService;

    public BeverageController(BeverageService beverageService) {
        this.beverageService = beverageService;
    }

    @PostMapping
    @SwaggerMethodToDocument
    @ApiOperation(
            value = "Update or create new beverage",
            authorizations = @Authorization(value="jwtToken")
    )
    public ResponseEntity<Beverage> addBeverage(@RequestBody Beverage beverage) {
        return new ResponseEntity<>(beverageService.addBeverage(beverage), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @SwaggerMethodToDocument
    @ApiOperation(
            value = "Get beverage with current id",
            authorizations = @Authorization(value="jwtToken")
    )
    public ResponseEntity<Beverage> getBeverage(@PathVariable Long id) {
        return new ResponseEntity<>(beverageService.getBeverage(id), HttpStatus.OK);
    }

    @GetMapping
    @SwaggerMethodToDocument
    @ApiOperation(
            value = "Get set of all beverages",
            authorizations = @Authorization(value="jwtToken")
    )
    public ResponseEntity<List<Beverage>> getBeverages() {
        return new ResponseEntity<>(beverageService.getBeverages(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @SwaggerMethodToDocument
    @ApiOperation(
            value = "Delete beverage with current id",
            authorizations = @Authorization(value="jwtToken")
    )
    public void deleteBeverage(@PathVariable Long id) {
        beverageService.deleteBeverage(id);
    }
}
