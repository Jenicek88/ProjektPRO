package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.InvoiceStatisticsDTO;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import cz.itnetwork.entity.repository.InvoiceRepository;
import cz.itnetwork.entity.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private PersonRepository personRepository;

    public InvoiceDTO addInvoice(InvoiceDTO invoiceDTO) {
        InvoiceEntity entity = invoiceMapper.toEntity(invoiceDTO);
        entity.setBuyer(personRepository.getReferenceById(invoiceDTO.getBuyer().getId()));
        entity.setSeller(personRepository.getReferenceById(invoiceDTO.getSeller().getId()));
        entity = invoiceRepository.save(entity);

        return invoiceMapper.toDTO(entity);
    }
    @Override
    public List<InvoiceDTO> getAll() {
        return invoiceRepository.findAll()
                .stream()
                .map(i -> invoiceMapper.toDTO(i))
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        InvoiceEntity existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));

        // Mapujeme hodnoty z DTO na existující entitu
        //invoiceMapper.updateEntityFromDto(invoiceDTO, existingInvoice);

        // Uložíme změny do databáze
        InvoiceEntity updatedInvoice = invoiceRepository.save(existingInvoice);

        // Vracíme aktualizovanou fakturu jako DTO
        return invoiceMapper.toDTO(updatedInvoice);
    }

    @Override
    public List<InvoiceDTO> getFilteredInvoices(Long buyerID, Long sellerID, String product, BigInteger minPrice, BigInteger maxPrice, Integer limit) {
        // Sestavení specifikace pro filtrování
        Specification<InvoiceEntity> spec = Specification.where(null);

        if (buyerID != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("buyer").get("id"), buyerID));
        }

        if (sellerID != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("seller").get("id"), sellerID));
        }

        if (product != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("product"), "%" + product + "%"));
        }

        if (minPrice != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        Pageable pageable = PageRequest.of(0, limit);
        List<InvoiceEntity> entities = invoiceRepository.findAll(spec, pageable).getContent();

        return entities.stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> getInvoicesBySeller(String identificationNumber) {
        // Najdeme dodavatele podle IČ
        PersonEntity seller = (PersonEntity) personRepository.findByIdentificationNumber(identificationNumber).orElseThrow(() -> new EntityNotFoundException("Seller not found with identification number: " + identificationNumber));

        // Najdeme faktury, které byly vystaveny tímto dodavatelem
        List<InvoiceEntity> invoices = invoiceRepository.findBySeller(seller);

        // Mapujeme faktury na DTO
        return invoices.stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> getInvoicesByBuyer(String identificationNumber) {
        // Najdeme odběratele podle IČ
        PersonEntity buyer = (PersonEntity) personRepository.findByIdentificationNumber(identificationNumber).orElseThrow(() -> new EntityNotFoundException("Buyer not found with identification number: " + identificationNumber));

        // Najdeme faktury, které byly přijaté tímto odběratelem
        List<InvoiceEntity> invoices = invoiceRepository.findByBuyer(buyer);

        // Mapujeme faktury na DTO
        return invoices.stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO getInvoiceDetail(Long id){
        InvoiceEntity invoiceEntity = invoiceRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Invoice not found with id: " + id));

        return invoiceMapper.toDTO(invoiceEntity);

    }

    @Override
    public void deleteInvoice(Long id) {
        // Ověření, zda faktura existuje
        InvoiceEntity invoiceEntity = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));

        // Smazání faktury
        invoiceRepository.delete(invoiceEntity);
    }

    @Override
    public InvoiceStatisticsDTO getInvoiceStatistics() {
        // Počet faktur
        Long invoicesCount = invoiceRepository.count();

        // Součet cen za aktuální rok
        BigDecimal currentYearSum = invoiceRepository.sumPricesByYear(LocalDate.now().getYear());

        // Součet cen za všechny roky
        BigDecimal allTimeSum = invoiceRepository.sumAllPrices();

        // Sestavení DTO
        InvoiceStatisticsDTO statistics = new InvoiceStatisticsDTO();
        statistics.setInvoicesCount(invoicesCount);
        statistics.setCurrentYearSum(currentYearSum != null ? currentYearSum : BigDecimal.ZERO);
        statistics.setAllTimeSum(allTimeSum != null ? allTimeSum : BigDecimal.ZERO);

        return statistics;
    }

}