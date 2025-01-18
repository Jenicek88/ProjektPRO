package cz.itnetwork.controller;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.InvoiceStatisticsDTO;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;



@RestController
@RequestMapping("/api")
public class InvoiceController {


    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/invoices")
    public InvoiceDTO addInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        return  invoiceService.addInvoice(invoiceDTO);
    }

    @GetMapping("/invoices")
    public List<InvoiceDTO> getInvoices() {
        return invoiceService.getAll();
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(@PathVariable Long id, @Valid @RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
        return ResponseEntity.ok(updatedInvoice);
    }

    @GetMapping("/invoicesFind")
    public ResponseEntity<List<InvoiceDTO>> getFilteredInvoices(
            @RequestParam(required = false) Long buyerID,
            @RequestParam(required = false) Long sellerID,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) BigInteger minPrice,
            @RequestParam(required = false) BigInteger maxPrice,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {

        List<InvoiceDTO> invoices = invoiceService.getFilteredInvoices(buyerID, sellerID, product, minPrice, maxPrice, limit);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/identification/{identificationNumber}/sales")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesBySeller(
            @PathVariable String identificationNumber) {
        List<InvoiceDTO> invoices = invoiceService.getInvoicesBySeller(identificationNumber);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/identification/{identificationNumber}/purchases")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByBuyer(
            @PathVariable String identificationNumber) {
        List<InvoiceDTO> invoices = invoiceService.getInvoicesByBuyer(identificationNumber);
        return ResponseEntity.ok(invoices);
    }
    @GetMapping("/invoices/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceDetail(@PathVariable Long id) {
        InvoiceDTO invoiceDetail = invoiceService.getInvoiceDetail(id);
        return ResponseEntity.ok(invoiceDetail);
    }

    @GetMapping("/invoices/statistics")
    public ResponseEntity<InvoiceStatisticsDTO> getInvoiceStatistics() {
        InvoiceStatisticsDTO statistics = invoiceService.getInvoiceStatistics();
        return ResponseEntity.ok(statistics);
    }

}
