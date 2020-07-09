package menu.service;

import menu.entity.Component;
import menu.repository.ComponentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentService {
    private ComponentRepository componentRepository;

    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    public Component addComponent(Component component) {
        return componentRepository.save(component);
    }

    public List<Component> getComponents() {
        return (List<Component>) componentRepository.findAll();
    }
}
