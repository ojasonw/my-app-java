package br.com.joga_together.mapper;

import br.com.joga_together.dto.CreateGroupRequestDto;
import br.com.joga_together.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "description", source = "dto.description")
    Group dtoToEntity(CreateGroupRequestDto dto);
}
