package auth.menu.service;

import auth.menu.entity.Beverage;
import auth.menu.exception.InvalidIdException;
import auth.menu.repository.BeverageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeverageService {
    private BeverageRepository beverageRepository;
    private ComponentService componentService;

    public BeverageService(BeverageRepository beverageRepository, ComponentService componentService) {
        this.beverageRepository = beverageRepository;
        this.componentService = componentService;
    }

    public Beverage addBeverage(Beverage beverage) {
        if (beverage.getId() != null) {
            deleteBeverage(beverage.getId());
        }

        beverage.getRecipe().forEach(component -> {
            component.setComponent(componentService.getComponent(component.getComponent().getId()));
            component.setBeverage(beverage);
        });

        return beverageRepository.save(beverage);
    }

    public List<Beverage> getBeverages() {
        return beverageRepository.findAll();
    }

    public Beverage getBeverage(Long id) throws InvalidIdException {
        Optional<Beverage> beverage = beverageRepository.findById(id);
        if (beverage.isPresent()) {
            return beverage.get();
        }

        throw new InvalidIdException("There is no beverage with such id");
    }

    public void deleteBeverage(Long id) {
        beverageRepository.delete(getBeverage(id));
    }
}
