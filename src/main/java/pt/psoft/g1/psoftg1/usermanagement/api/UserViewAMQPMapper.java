package pt.psoft.g1.psoftg1.usermanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserViewAMQPMapper extends MapperInterface {
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "version", source = "version")

    public UserViewAMQP toUserViewAMQP(User user){
        return new UserViewAMQP(user.getUsername(), user.getVersion(), user.getPassword(), user.getName().toString(), user.getAuthorities().iterator().next().getAuthority());
    };

    public abstract List<UserViewAMQP> toUserViewAMQP(List<User> userList);

}
