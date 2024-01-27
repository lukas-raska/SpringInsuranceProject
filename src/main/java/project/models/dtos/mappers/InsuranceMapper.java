package project.models.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.data.entities.insurance.InsuranceEntity;
import project.models.dtos.InsuranceDTO;

@Mapper(componentModel = "spring")
public interface InsuranceMapper {

     @Mapping(target = "insuredClient", ignore = true)
     @Mapping(target="dateOfRegistry", ignore = true)
     InsuranceEntity dtoToEntity (InsuranceDTO source);

     @Mapping(target="clientId", source = "insuredClient.id")
     InsuranceDTO entityToDTO (InsuranceEntity source);
}
