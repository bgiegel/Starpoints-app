package fr.softeam.starpointsapp.service.dto;

import fr.softeam.starpointsapp.config.Constants;
import fr.softeam.starpointsapp.domain.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * A DTO representing a user, with his authorities.
 */
public class BasicUserDTO {

    protected Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    protected String login;

    @Size(max = 50)
    protected String firstName;

    @Size(max = 50)
    protected String lastName;

    public BasicUserDTO() {
    }

    public BasicUserDTO(User user) {
        this(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName());
    }

    public BasicUserDTO(Long id, String login, String firstName, String lastName) {

        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "id='" + id + '\'' +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            "}";
    }
}
