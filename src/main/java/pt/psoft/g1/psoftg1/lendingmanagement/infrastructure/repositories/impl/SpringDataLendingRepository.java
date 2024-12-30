package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.*;

public interface SpringDataLendingRepository extends LendingRepository, CrudRepository<Lending, Long> {
    @Override
    @Query("SELECT l " +
            "FROM Lending l " +
            "WHERE l.lendingNumber.lendingNumber = :lendingNumber")
    Optional<Lending> findByLendingNumber(String lendingNumber);

    //http://www.h2database.com/html/commands.html

//    @Override
//    @Query("SELECT l " +
//            "FROM Lending l " +
//            "JOIN Book b ON l.book.pk = b.pk " +
//            "JOIN ReaderDetails r ON l.readerDetails.pk = r.pk " +
//            "WHERE b.isbn.isbn = :isbn " +
//            "AND r.readerNumber.readerNumber = :readerNumber ")
//    List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn);




}

