package cl.isosalud.service.controller;

import cl.isosalud.service.dto.AppointmentDto;
import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.ProductDto;
import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.service.appointment.AppointmentService;
import cl.isosalud.service.service.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/inventory", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InventoryController {

    private final InventoryService inventoryService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<ProductDto>> getAllProducts() {
        return new ResponseListWrapper<>(inventoryService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public ProductDto getProduct(@RequestParam Integer id) {
        return inventoryService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "")
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return inventoryService.create(productDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/update")
    public ProductDto updateProduct(@RequestParam Integer id, @RequestBody ProductDto productDto) {
        return inventoryService.update(id, productDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/update-quantity")
    public ProductDto updateProduct(@RequestParam Integer id, @RequestParam String action, @RequestParam(required = false) Integer ammount) {
        return inventoryService.updateQuantity(id, action, ammount);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/product-types")
    public ResponseListWrapper<List<NameDescriptionObj>> getAllProductTypes() {
        return new ResponseListWrapper<>(inventoryService.getAllProductTypes());
    }

}
