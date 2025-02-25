package br.com.diego.sct.domain.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.diego.sct.domain.model.PayableEntry;
import br.com.diego.sct.infra.repository.PayableEntryRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayableEntryServiceTest {
    @Mock
    PayableEntryRepository payableEntryRepository;

    @InjectMocks
    PayableEntryService payableEntryService;

    @Captor
    ArgumentCaptor<List<PayableEntry>> payableEntriesCaptor;

    @Test
    void testLoadCsvFileContent() {
        LocalDate today = LocalDate.now();
        LocalDate dueDateNextMonth = today.plusMonths(1);
        LocalDate dueDateTomorrow = today.plusDays(1);
        LocalDate dueDateNextWeek = today.plusWeeks(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String fileContent = String.format("""
            Transporte,%s,,OPEN,11100.50
            Manutenção,%s,,PENDING_APPROVAL_OVERDUE,1100.50
            Água,%s,,PENDING_APPROVAL_OVERDUE,1100.50
            Cartão de Crédito,%s,,APPROVED,2000.50
            Transport,%s,,PAID,11100.50
            Cartão de Crédito 1,%s,,CANCELLED,10000.50
            """, dueDateNextMonth.format(formatter),
            dueDateTomorrow.format(formatter),
            today,
            today,
            today,
            dueDateNextWeek.format(formatter));

        System.out.println(fileContent);

        payableEntryService.loadCsvFileContent(fileContent);

        verify(payableEntryRepository).saveAll(payableEntriesCaptor.capture());

        List<PayableEntry> result = payableEntriesCaptor.getValue();

        Assertions.assertEquals(6L, result.size());
        Assertions.assertEquals("Transporte", result.get(0).getDescription());
        Assertions.assertEquals(dueDateNextMonth, result.get(0).getDueDate());
        Assertions.assertNull(result.get(0).getPaymentDate());
        Assertions.assertNotNull(result.get(0).getStatus());
        Assertions.assertEquals("OPEN", result.get(0).getStatus().name());
        Assertions.assertEquals("11100.50", result.get(0).getValue().toString());

        Assertions.assertEquals("Cartão de Crédito 1", result.get(5).getDescription());
        Assertions.assertEquals(dueDateNextWeek, result.get(5).getDueDate());
        Assertions.assertNull(result.get(5).getPaymentDate());
        Assertions.assertNotNull(result.get(5).getStatus());
        Assertions.assertEquals("CANCELLED", result.get(5).getStatus().name());
        Assertions.assertEquals("10000.50", result.get(5).getValue().toString());
    }

}
