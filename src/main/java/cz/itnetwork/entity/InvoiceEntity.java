package cz.itnetwork.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity (name = "invoice")
@Getter
@Setter
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Integer invoiceNumber;

    @Column
    private LocalDate issued;

    @Column
    private LocalDate dueDate;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer vat;

    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    private PersonEntity buyer;

    @ManyToOne (fetch = FetchType.LAZY)
    private PersonEntity seller;


}
