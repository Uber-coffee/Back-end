package auth.menu.controller;

import auth.menu.entity.Component;
import auth.menu.service.ComponentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/w/components")
public class ComponentController {
    ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @PostMapping
    public ResponseEntity<Component> addComponent(@RequestBody Component component) {
        return new ResponseEntity<>(componentService.addComponent(component), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Component> getComponent(@PathVariable Long id) {
        return new ResponseEntity<>(componentService.getComponent(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Component>> getComponents() {
        return new ResponseEntity<>(componentService.getComponents(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteComponent(@PathVariable Long id) {
        componentService.deleteComponent(id);
    }
}
