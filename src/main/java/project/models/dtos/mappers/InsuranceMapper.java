package project.models.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.data.entities.InsuranceEntity;
import project.models.dtos.InsuranceDTO;

@Mapper(componentModel = "spring")
public interface InsuranceMapper {

     @Mapping(target = "insuredClient", ignore = true)
     @Mapping(target="dateOfRegistry", ignore = true)
     InsuranceEntity dtoToEntity (InsuranceDTO source);

     @Mapping(target="clientId", source = "insuredClient.id")
     InsuranceDTO entityToDTO (InsuranceEntity source);

     @Mapping(target = "dateOfRegistry", ignore = true)
     void updateInsuranceDTO (InsuranceDTO source, @MappingTarget InsuranceDTO target);

     @Mapping(target="dateOfRegistry", ignore = true)
     @Mapping(target="insuredClient", ignore = true)
     void updateInsuranceEntity (InsuranceDTO source, @MappingTarget InsuranceEntity target);
}
