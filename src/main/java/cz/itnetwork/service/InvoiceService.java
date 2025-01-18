package cz.itnetwork.service;

import cz.itnetwork.controller.InvoiceController;
import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.InvoiceStatisticsDTO;
import cz.itnetwork.dto.PersonDTO;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;


public interface InvoiceService {

    InvoiceDTO addInvoice(InvoiceDTO invoiceDTO);

    List<InvoiceDTO> getAll();

    InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO);

    List<InvoiceDTO> getFilteredInvoices(Long buyerID, Long sellerID, String product, BigInteger minPrice, BigInteger maxPrice, Integer limit);

    List<InvoiceDTO> getInvoicesBySeller(String identificationNumber);

    List<InvoiceDTO> getInvoicesByBuyer(String identificationNumber);

    InvoiceDTO getInvoiceDetail(Long id);



    void deleteInvoice(Long id);

    InvoiceStatisticsDTO getInvoiceStatistics();
}
