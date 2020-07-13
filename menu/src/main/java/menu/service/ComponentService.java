package auth.menu.service;

import auth.menu.entity.Component;
import auth.menu.exception.InvalidIdException;
import auth.menu.repository.ComponentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComponentService {
    private ComponentRepository componentRepository;

    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    public Component addComponent(Component component) {
        component.getMyClass().setComponent(component);
        return componentRepository.save(component);
    }

    public List<Component> getComponents() {
        return (List<Component>) componentRepository.findAll();
    }

    public Component getComponent(Long id) throws InvalidIdException {
        Optional<Component> component = componentRepository.findById(id);
        if (component.isPresent()) {
            return component.get();
        }
        throw new InvalidIdException("There is no component with such id");
    }

    public void deleteComponent(Long id) {
        componentRepository.delete(getComponent(id));
    }
}
