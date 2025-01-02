package pt.psoft.g1.psoftg1.readermanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQP;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReaderViewAMQPMapper {

//    @Named(value = "toReaderView")
    @Mapping(target = "fullName", source = "reader.name.name")
    @Mapping(target = "username", source = "reader.username")
    @Mapping(target = "userId", source = "reader.id")
    @Mapping(target = "birthDate", source = "birthDate.birthDate")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gdpr", source = "gdprConsent")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "version", source = "version")
//    @Mapping(target = "photoURI", expression = "java(generatePhotoUrl(readerDetails))")
//    @Mapping(target = "interestList", expression = "java(mapInterestList(readerDetails.getInterestList()))")
    public abstract ReaderViewAMQP toReaderViewAMQP(ReaderDetails readerDetails);

    public abstract List<ReaderViewAMQP> toReaderViewAMQP(List<ReaderDetails> readerDetailsList);


    @Mapping(target = "fullName", source = "reader.name.name")
    @Mapping(target = "username", source = "reader.username")
    @Mapping(target = "userId", source = "reader.id")
    @Mapping(target = "birthDate", source = "readerDetails.birthDate.birthDate")
    @Mapping(target = "phoneNumber", source = "readerDetails.phoneNumber")
    @Mapping(target = "gdpr", source = "readerDetails.gdprConsent")
    @Mapping(target = "readerNumber", source = "readerDetails.readerNumber")
    @Mapping(target = "version", source = "readerDetails.version")
    public abstract ReaderViewAMQP toReaderViewAMQP(ReaderDetails readerDetails , Reader reader);
}
