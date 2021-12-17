package cl.isosalud.service.service.inventory;

import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.ProductDto;
import cl.isosalud.service.entity.*;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final Mapper mapper;

    @Override
    public ProductDto getById(int productId) {
        return mapper.map(productRepository.findById(productId), ProductDto.class);
    }

    @Override
    public List<ProductDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(p -> mapper.map(p, ProductDto.class))
                .toList();
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        ProductEntity productEntity = mapper.map(productDto, ProductEntity.class);

        ProductTypeEntity productTypeEntity = productTypeRepository.findById(productDto.getProductType().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("ProductType id %s not found", productDto.getProductType().getId()), String.format("ProductType id %s not found", productDto.getProductType().getId())));

        productEntity.setProductType(productTypeEntity);

        return mapper.map(productRepository.save(productEntity), ProductDto.class);
    }

    @Override
    public ProductDto update(Integer id, ProductDto productDto) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Product id %s not found", id), String.format("Product id %s not found", id)));

        if (productDto.getProductType() != null && productDto.getProductType().getId() != null) {
            ProductTypeEntity productTypeEntity = productTypeRepository.findById(productDto.getProductType().getId())
                    .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("ProductType id %s not found", productDto.getProductType().getId()), String.format("ProductType id %s not found", productDto.getProductType().getId())));

            productEntity.setProductType(productTypeEntity);
        }

        if (productDto.getName() != null) {
            productEntity.setName(productDto.getName());
        }

        productEntity.setDescription(productDto.getDescription());

        if (productDto.getPrice() != null) {
            productEntity.setPrice(productDto.getPrice());
        }

        if (productDto.getQuantity() != null) {
            productEntity.setQuantity(productDto.getQuantity());
        }

        return mapper.map(productRepository.save(productEntity), ProductDto.class);
    }

    @Override
    public List<NameDescriptionObj> getAllProductTypes() {
        return productTypeRepository.findAll()
                .stream()
                .map(p -> mapper.map(p, NameDescriptionObj.class))
                .toList();
    }

    @Override
    public ProductDto updateQuantity(Integer id, String action, Integer ammount) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Product id %s not found", id), String.format("Product id %s not found", id)));

        if (action.equals("plus")) {
            productEntity.setQuantity(productEntity.getQuantity() + (ammount != null ? ammount : 1));
        }else {
            productEntity.setQuantity(productEntity.getQuantity() - (ammount != null ? ammount : 1));
        }

        return mapper.map(productRepository.save(productEntity), ProductDto.class);
    }

}
