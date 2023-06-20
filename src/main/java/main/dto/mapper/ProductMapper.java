package main.dto.mapper;

import main.dto.request.ProductRequest;
import main.dto.response.ProductResponse;
import main.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);
    ProductEntity fromRequestToEntity(ProductRequest productRequest);
    ProductResponse fromEntityToResponse(ProductEntity productEntity);
}
