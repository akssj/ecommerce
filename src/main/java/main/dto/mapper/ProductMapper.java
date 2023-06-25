package main.dto.mapper;

import main.dto.request.AddProductRequest;
import main.dto.response.ProductResponse;
import main.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);
    ProductEntity fromRequestToEntity(AddProductRequest addProductRequest);
    ProductResponse fromEntityToResponse(ProductEntity productEntity);
}
