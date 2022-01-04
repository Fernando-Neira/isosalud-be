package cl.isosalud.service.mapping;

import cl.isosalud.service.dto.*;
import cl.isosalud.service.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Configuration
public class Mapper {

    @Bean
    public Mapper instance() {
        return new Mapper();
    }

    public <T> T map(Object source, Class<T> destinationClass) {
        T destination;

        if (source == null) {
            return null;
        }

        if (source.getClass().isAssignableFrom(Optional.class)) {
            Optional<?> sourceOpt = (Optional<?>) source;

            if (sourceOpt.isEmpty()) {
                return null;
            }

            source = sourceOpt.get();
        }

        if (destinationClass.isAssignableFrom(UserDto.class)) {
            destination = (T) fromUserEntity((UserEntity) source);
        } else if (destinationClass.isAssignableFrom(UserEntity.class)) {
            destination = (T) fromUserDto((UserDto) source);
        } else if (destinationClass.isAssignableFrom(PersonDto.class)) {
            destination = (T) fromPersonEntity((PersonEntity) source);
        } else if (destinationClass.isAssignableFrom(PersonEntity.class)) {
            destination = (T) fromPersonDto((PersonDto) source);
        } else if (destinationClass.isAssignableFrom(AppointmentDto.class)) {
            destination = (T) fromAppointmentEntity((AppointmentEntity) source);
        } else if (destinationClass.isAssignableFrom(AppointmentEntity.class)) {
            destination = (T) fromAppointmentDto((AppointmentDto) source);
        } else if (destinationClass.isAssignableFrom(NameDescriptionObj.class)) {
            destination = (T) fromEntityWithIdNameAndDescription(source);
        } else if (destinationClass.isAssignableFrom(ProductEntity.class)) {
            destination = (T) fromProductDto((ProductDto) source);
        } else if (destinationClass.isAssignableFrom(ProductDto.class)) {
            destination = (T) fromProductEntity((ProductEntity) source);
        } else if (destinationClass.isAssignableFrom(RoleDto.class)) {
            destination = (T) fromRoleEntity((RoleEntity) source);
        } else if (destinationClass.isAssignableFrom(ContactMeanDto.class)) {
            destination = (T) fromContactMeanEntity((ContactMeanEntity) source);
        } else if (destinationClass.isAssignableFrom(LocationDto.class)) {
            destination = (T) fromMapEntryRegionEntityAndComunneEntity((Map.Entry<RegionEntity, List<CommuneEntity>>) source);
        } else {
            throw new RuntimeException("Not implemented yet..");
        }

        return destination;
    }

    private LocationDto fromMapEntryRegionEntityAndComunneEntity(Map.Entry<RegionEntity, List<CommuneEntity>> source) {
        return LocationDto.builder()
                .id(source.getKey().getId())
                .name(source.getKey().getName())
                .abbreviation(source.getKey().getAbbreviation())
                .romanNumber(source.getKey().getAbbreviation())
                .communes(source.getValue().stream().map(this::fromCommuneEntity).toList())
                .build();
    }

    private LocationDto.ComunneDto fromCommuneEntity(CommuneEntity source) {
        return LocationDto.ComunneDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

    private RoleDto fromRoleEntity(RoleEntity source) {
        return RoleDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .spanishName(source.getSpanishName())
                .build();
    }

    private ContactMeanDto fromContactMeanEntity(ContactMeanEntity source) {
        return ContactMeanDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .spanishName(source.getSpanishName())
                .build();
    }

    private ProductDto fromProductEntity(ProductEntity source) {
        return ProductDto.builder()
                .id(source.getId())
                .productType(fromEntityWithIdNameAndDescription(source.getProductType()))
                .description(source.getDescription())
                .price(source.getPrice())
                .quantity(source.getQuantity())
                .name(source.getName())
                .build();
    }

    private Object fromProductDto(ProductDto source) {
        return ProductEntity.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .quantity(source.getQuantity())
                .price(source.getPrice())
                .build();
    }

    public PersonEntity fromPersonDto(PersonDto personDto) {
        return PersonEntity.builder()
                .rut(personDto.getRut())
                .firstName(personDto.getFirstName())
                .lastName(personDto.getLastName())
                .email(personDto.getEmail())
                .phone(personDto.getPhone())
                .cellphone(personDto.getCellphone())
                .dateOfBirth(personDto.getDateOfBirth())
                .dateCreated(personDto.getDateCreated())
                .dateUpdated(personDto.getDateUpdated())
                .build();
    }

    public PersonDto fromPersonEntity(PersonEntity personEntity) {
        return PersonDto.builder()
                .id(personEntity.getId())
                .rut(personEntity.getRut())
                .firstName(personEntity.getFirstName())
                .lastName(personEntity.getLastName())
                .email(personEntity.getEmail())
                .addressInfo(fromAddressEntity(personEntity.getAddressEntity()))
                .gender(personEntity.getGenderEntity().getName())
                .prevision(personEntity.getPrevisionEntity().getName())
                .phone(personEntity.getPhone())
                .cellphone(personEntity.getCellphone())
                .dateOfBirth(personEntity.getDateOfBirth())
                .dateCreated(personEntity.getDateCreated())
                .dateUpdated(personEntity.getDateUpdated())
                .build();
    }

    public UserDto fromUserEntity(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .personInfo(fromPersonEntity(userEntity.getPersonEntity()))
                .roleName(userEntity.getRoleEntity().getName())
                .dateCreated(userEntity.getDateCreated())
                .preferredContactMeanName(userEntity.getPreferredContactMeanEntity().getName())
                .lastLogin(userEntity.getLastLogin())
                .profileImgUri(userEntity.getProfileImgUri())
                .dateUpdated(userEntity.getPersonEntity().getDateUpdated())
                .status(userEntity.getUserStateEntity().getName())
                .build();
    }

    public UserEntity fromUserDto(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
//                .personEntity(fromPersonEntity(userDto.getPersonEntity()))
//                .roleName(userDto.getRoleEntity().getName())
                .dateCreated(userDto.getDateCreated())
//                .preferredContactMeanName(userDto.getPreferredContactMeanEntity().getName())
                .lastLogin(userDto.getLastLogin())
                .profileImgUri(userDto.getProfileImgUri())
//                .dateUpdated(userDto.getPersonEntity().getDateUpdated())
                .build();
    }

    public AddressDto fromAddressEntity(AddressEntity addressEntity) {
        return AddressDto.builder()
                .id(addressEntity.getId())
                .communeId(addressEntity.getCommuneEntity().getId())
                .commune(addressEntity.getCommuneEntity().getName())
                .street(addressEntity.getAddress())
                .region(fromRegionEntity(addressEntity.getCommuneEntity().getRegion()))
                .build();
    }

    public AddressDto.RegionDto fromRegionEntity(RegionEntity regionEntity) {
        return AddressDto.RegionDto.builder()
                .id(regionEntity.getId())
                .name(regionEntity.getName())
                .abbreviation(regionEntity.getAbbreviation())
                .romanNumber(regionEntity.getRomanNumber())
                .build();
    }

    private AppointmentDto fromAppointmentEntity(AppointmentEntity source) {
        return AppointmentDto.builder()
                .id(source.getId())
                .title(source.getTitle())
                .startDate(source.getDateStart())
                .endDate(source.getDateEnd())
                .box(fromEntityWithIdNameAndDescription(source.getBox()))
                .comment(source.getComment())
                .patient(fromUserEntity(source.getPatientUserEntity()))
                .medic(fromUserEntity(source.getMedicUserEntity()))
                .status(fromEntityWithIdNameAndDescription(source.getAppointmentState()))
                .type(fromEntityWithIdNameAndDescription(source.getAppointmentType()))
                .treatment(source.getTreatmentEntity() != null ? fromTreatmentEntity(source.getTreatmentEntity()) : null)
                .build();
    }

    private TreatmentDto fromTreatmentEntity(TreatmentEntity target) {
        return TreatmentDto.builder()
                .id(target.getId())
                .medic(fromUserEntity(target.getMedicUser()))
                .patient(fromUserEntity(target.getPatientUser()))
                .comment(target.getComment())
                .specialization(map(target.getTreatmentSpecializationEntity()))
                .state(map(target.getTreatmentState()))
                .dateCreated(target.getDateCreated())
                .typeOdontograma(target.getTypeOdontograma())
                .build();
    }

    private TreatmentSpecializationDto map(TreatmentSpecializationEntity target) {
        return TreatmentSpecializationDto.builder()
                .id(target.getId())
                .name(target.getName())
                .description(target.getDescription())
                .build();
    }

    private TreatmentStateDto map(TreatmentStateEntity target) {
        return TreatmentStateDto.builder()
                .id(target.getId())
                .name(target.getName())
                .description(target.getDescription())
                .build();
    }

    private AppointmentEntity fromAppointmentDto(AppointmentDto source) {
        return AppointmentEntity.builder()
                .id(source.getId())
//                .patientPersonEntity()
//                .medicPersonEntity()
                .title(source.getTitle())
                .comment(source.getComment())
                .dateStart(source.getStartDate())
                .dateEnd(source.getEndDate())
//                .treatmentId(source.getTreatmentId())
//                .box()
//                .appointmentState()
                .build();
    }

    private NameDescriptionObj fromEntityWithIdNameAndDescription(Object source) {
        int id;
        String name, description;

        try {
            id = (int) source.getClass().getMethod("getId").invoke(source);
            name = (String) source.getClass().getMethod("getName").invoke(source);
            description = (String) source.getClass().getMethod("getDescription").invoke(source);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error al obtener campo por reflection => " + e.getMessage());
        }

        return NameDescriptionObj.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

}
