package pt.psoft.g1.psoftg1.usermanagement.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


import java.util.HashSet;
import java.util.Set;

@Data
@Schema(description = "A User form AMQP communication")
public class UserViewAMQP {
    @Setter
    @Getter
    private Long id;

    @NonNull
    @NotBlank
    @Email
    @Setter
    @Getter
    private String username;

    @NotNull
    private Long version;

    @NonNull
    @NotBlank
    @Setter
    @Getter
    private String password;

    @NonNull
    @NotBlank
    private String name;

    @Getter
    @Setter
    private String role;

    //private Set<String> authorities = new HashSet<>();


    @JsonCreator
    public UserViewAMQP(
            @JsonProperty("id") Long id,
            @JsonProperty("username") String username,
            @JsonProperty("version") long version,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("role") String role) {
        this.id = id;
        this.username = username;
        this.version = version;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
