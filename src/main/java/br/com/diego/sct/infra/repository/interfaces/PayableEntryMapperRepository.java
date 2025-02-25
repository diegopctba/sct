package br.com.diego.sct.infra.repository.interfaces;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import br.com.diego.sct.domain.entity.TbPayableEntry;
import br.com.diego.sct.domain.model.PayableEntry;
import br.com.diego.sct.infra.repository.Pagination;

@Mapper(componentModel = "spring")
public interface PayableEntryMapperRepository {
    TbPayableEntry toTbPayableEntry(PayableEntry payableEntry);
    PayableEntry toPayableEntry(TbPayableEntry account);
    Pagination<PayableEntry> mapPageToPayableEntryPage(Page<TbPayableEntry> page);
}
