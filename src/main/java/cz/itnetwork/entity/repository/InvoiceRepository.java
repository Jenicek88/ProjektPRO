package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long>, JpaSpecificationExecutor<InvoiceEntity> {
    List<InvoiceEntity> findBySeller(PersonEntity seller);
    List<InvoiceEntity> findByBuyer(PersonEntity buyer);

    @Query("SELECT SUM(i.price) FROM InvoiceEntity i WHERE YEAR(i.issued) = :year")
    BigDecimal sumPricesByYear(@Param("year") int year);

    @Query("SELECT SUM(i.price) FROM InvoiceEntity i")
    BigDecimal sumAllPrices();
}
