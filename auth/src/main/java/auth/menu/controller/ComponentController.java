package menu.controller;

import menu.entity.Component;
import menu.service.ComponentService;
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

    @GetMapping
    public ResponseEntity<List<Component>> getClasses() {
        return new ResponseEntity<>(componentService.getComponents(), HttpStatus.OK);
    }
}
