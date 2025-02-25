package br.com.diego.sct.domain.mapper;


import org.mapstruct.Mapper;

import br.com.diego.sct.domain.model.PayableEntry;
import br.com.diego.sct.infra.dto.PayableEntryDto;

@Mapper(componentModel = "spring")
public interface PayableEntryMapper {
    PayableEntry toPayableEntry(PayableEntryDto payableEntryDto);
    PayableEntryDto toPayableEntryDto(PayableEntry payableEntry);
}
