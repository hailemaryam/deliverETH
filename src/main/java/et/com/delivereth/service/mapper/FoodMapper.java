package et.com.delivereth.service.mapper;


import et.com.delivereth.domain.*;
import et.com.delivereth.service.dto.FoodDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Food} and its DTO {@link FoodDTO}.
 */
@Mapper(componentModel = "spring", uses = {RestorantMapper.class})
public interface FoodMapper extends EntityMapper<FoodDTO, Food> {

    @Mapping(source = "restorant.id", target = "restorantId")
    @Mapping(source = "restorant.name", target = "restorantName")
    FoodDTO toDto(Food food);

    @Mapping(target = "orderedFoods", ignore = true)
    @Mapping(target = "removeOrderedFood", ignore = true)
    @Mapping(source = "restorantId", target = "restorant")
    Food toEntity(FoodDTO foodDTO);

    default Food fromId(Long id) {
        if (id == null) {
            return null;
        }
        Food food = new Food();
        food.setId(id);
        return food;
    }
}
