package cl.isosalud.service.service.inventory;

import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.ProductDto;

import java.util.List;

public interface InventoryService {

    ProductDto getById(int productId);

    ProductDto create(ProductDto productDto);

    List<ProductDto> getAll();

    ProductDto update(Integer id, ProductDto productDto);

    List<NameDescriptionObj> getAllProductTypes();

    ProductDto updateQuantity(Integer id, String action, Integer ammount);
}
