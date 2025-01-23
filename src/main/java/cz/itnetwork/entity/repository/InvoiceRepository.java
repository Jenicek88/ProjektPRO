package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long>, JpaSpecificationExecutor<InvoiceEntity> {



    List<InvoiceEntity> findBySeller(PersonEntity seller);
    List<InvoiceEntity> findByBuyer(PersonEntity buyer);


   ///BigDecimal sumAllPrices();

    @Query(value = "SELECT SUM(price) FROM invoicedatabase.invoice WHERE issued BETWEEN :startDate AND :endDate", nativeQuery = true)
    BigDecimal sumPricesByYearNative(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
