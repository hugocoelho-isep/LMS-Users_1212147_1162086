package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.services.ForbiddenNameService;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@PropertySource({ "classpath:config/library.properties" })
@Order(2)
public class Bootstrapper implements CommandLineRunner {

    private final GenreRepository genreRepository;

    private final ForbiddenNameService forbiddenNameService;

    private final LendingRepository lendingRepository;

    private final ReaderRepository readerRepository;

    @Override
    @Transactional
    public void run(final String... args) {

        createGenres();
        createLendings();
    }


    private void createGenres() {
        if (genreRepository.findByString("Fantasia").isEmpty()) {
            final Genre g1 = new Genre("Fantasia");
            genreRepository.save(g1);
        }
        if (genreRepository.findByString("Informação").isEmpty()) {
            final Genre g2 = new Genre("Informação");
            genreRepository.save(g2);
        }
        if (genreRepository.findByString("Romance").isEmpty()) {
            final Genre g3 = new Genre("Romance");
            genreRepository.save(g3);
        }
        if (genreRepository.findByString("Infantil").isEmpty()) {
            final Genre g4 = new Genre("Infantil");
            genreRepository.save(g4);
        }
        if (genreRepository.findByString("Thriller").isEmpty()) {
            final Genre g5 = new Genre("Thriller");
            genreRepository.save(g5);
        }
    }


    private void createLendings() {
        int i;
        int seq = 0;

        List<String> books = new ArrayList<>();
        books.add("9789720706386");
        books.add("9789723716160");
        books.add("9789895612864");
        books.add("9782722203402");
        books.add("9789722328296");
        books.add("9789895702756");
        books.add("9789897776090");
        books.add("9789896379636");
        books.add("9789896378905");
        books.add("9789896375225");

        int year = Year.now().getValue();
        final var readerDetails1 = readerRepository.findByReaderNumber(year + "/1");
        final var readerDetails2 = readerRepository.findByReaderNumber(year + "/2");
        final var readerDetails3 = readerRepository.findByReaderNumber(year + "/3");
        final var readerDetails4 = readerRepository.findByReaderNumber(year + "/4");
        final var readerDetails5 = readerRepository.findByReaderNumber(year + "/5");
        final var readerDetails6 = readerRepository.findByReaderNumber(year + "/6");

        List<ReaderDetails> readers = new ArrayList<>();
        if(readerDetails1.isPresent() && readerDetails2.isPresent() && readerDetails3.isPresent()){
            readers = List.of(new ReaderDetails[]{readerDetails1.get(), readerDetails2.get(), readerDetails3.get(),
                    readerDetails4.get(), readerDetails5.get(), readerDetails6.get()});
        }

        LocalDate startDate;
        LocalDate returnedDate;
        Lending lending;

        //Lendings 1 through 3 (late, returned)
        for(i = 0; i < 3; i++){
            ++seq;
            if(lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()){
                startDate = LocalDate.of(2024, 1,31-i);
                returnedDate = LocalDate.of(2024,2,15+i);
                lending = Lending.newBootstrappingLending(books.get(i), readers.get(i*2), 2024, seq, startDate, returnedDate);
                lendingRepository.save(lending);
            }
        }

        //Lendings 4 through 6 (overdue, not returned)
        for(i = 0; i < 3; i++){
            ++seq;
            if(lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()){
                startDate = LocalDate.of(2024, 3,25+i);
                lending = Lending.newBootstrappingLending(books.get(1+i), readers.get(1+i*2), 2024, seq, startDate, null);
                lendingRepository.save(lending);
            }
        }
        //Lendings 7 through 9 (late, overdue, not returned)
        for(i = 0; i < 3; i++){
            ++seq;
            if(lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()){
                startDate = LocalDate.of(2024, 4,(1+2*i));
                lending = Lending.newBootstrappingLending(books.get(3/(i+1)), readers.get(i*2), 2024, seq, startDate, null);
                lendingRepository.save(lending);
            }
        }

        //Lendings 10 through 12 (returned)
        for(i = 0; i < 3; i++){
            ++seq;
            if(lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()){
                startDate = LocalDate.of(2024, 5,(i+1));
                returnedDate = LocalDate.of(2024,5,(i+2));
                lending = Lending.newBootstrappingLending(books.get(3-i), readers.get(1+i*2), 2024, seq, startDate, returnedDate);
                lendingRepository.save(lending);
            }
        }

        //Lendings 13 through 18 (returned)
        for(i = 0; i < 6; i++){
            ++seq;
            if(lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()){
                startDate = LocalDate.of(2024, 5,(i+2));
                returnedDate = LocalDate.of(2024,5,(i+2*2));
                lending = Lending.newBootstrappingLending(books.get(i), readers.get(i), 2024, seq, startDate, returnedDate);
                lendingRepository.save(lending);
            }
        }

        //Lendings 19 through 23 (returned)
        for(i = 0; i < 6; i++){
            ++seq;
            if(lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()){
                startDate = LocalDate.of(2024, 5,(i+8));
                returnedDate = LocalDate.of(2024,5,(2*i+8));
                lending = Lending.newBootstrappingLending(books.get(i), readers.get(1+i%4), 2024, seq, startDate, returnedDate);
                lendingRepository.save(lending);
            }
        }

        //Lendings 24 through 29 (returned)
        for(i = 0; i < 6; i++){
            ++seq;
            if(lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()){
                startDate = LocalDate.of(2024, 5,(i+18));
                returnedDate = LocalDate.of(2024,5,(2*i+18));
                lending = Lending.newBootstrappingLending(books.get(i), readers.get(i%2+2), 2024, seq, startDate, returnedDate);
                lendingRepository.save(lending);
            }
        }

        //Lendings 30 through 35 (not returned, not overdue)
        for(i = 0; i < 6; i++){
            ++seq;
            if(lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()){
                startDate = LocalDate.of(2024, 6,(i/3+1));
                lending = Lending.newBootstrappingLending(books.get(i), readers.get(i%2+3), 2024, seq, startDate, null);
                lendingRepository.save(lending);
            }
        }

        //Lendings 36 through 45 (not returned, not overdue)
        for(i = 0; i < 10; i++){
            ++seq;
            if(lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()){
                startDate = LocalDate.of(2024, 6,(2+i/4));
                lending = Lending.newBootstrappingLending(books.get(i), readers.get(4-i%4), 2024, seq, startDate, null);
                lendingRepository.save(lending);
            }
        }

    }

    private void createPhotos() {
        /*
         * Optional<Photo> photoJoao = photoRepository.findByPhotoFile("foto-joao.jpg"); if(photoJoao.isEmpty()) { Photo
         * photo = new Photo(Paths.get("")) }
         */
    }
}
