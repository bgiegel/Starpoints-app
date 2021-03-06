package fr.softeam.starpointsapp.service.mapper;

import fr.softeam.starpointsapp.domain.Authority;
import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.domain.User;
import fr.softeam.starpointsapp.service.dto.UserDTO;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity User and its DTO UserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    List<UserDTO> usersToUserDTOs(List<User> users);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "persistentTokens", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activationKey", ignore = true)
    @Mapping(target = "resetKey", ignore = true)
    @Mapping(target = "resetDate", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "communities", ignore = true)
    @Mapping(target = "contributions", ignore = true)
    User userDTOToUser(UserDTO userDTO);

    List<User> userDTOsToUsers(List<UserDTO> userDTOs);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    default Set<String> stringsFromAuthorities (Set<Authority> authorities) {
        return authorities.stream().map(Authority::getName)
            .collect(Collectors.toSet());
    }

    default Set<Authority> authoritiesFromStrings(Set<String> strings) {
        return strings.stream().map(string -> {
            Authority auth = new Authority();
            auth.setName(string);
            return auth;
        }).collect(Collectors.toSet());
    }

    default Set<String> stringsFromCommunities(Set<Community> communities) {
        return communities.stream().map(Community::getName)
            .collect(Collectors.toSet());
    }

    default Set<Community> communitiesFromStrings(Set<String> strings) {
        return strings.stream().map(string -> {
            Community community = new Community();
            community.setName(string);
            return community;
        }).collect(Collectors.toSet());
    }
}
