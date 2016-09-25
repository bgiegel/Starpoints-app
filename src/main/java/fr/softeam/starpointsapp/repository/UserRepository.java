package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.User;

import java.time.ZonedDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneById(Long userId);

    @Query(value = "select distinct user from User user left join fetch user.authorities left join fetch user.communities where user.login= :login")
    Optional<User> findOneByLoginWithAuthoritiesAndCommunities(@Param("login")String login);

    @Query(value = "select distinct user from User user left join fetch user.authorities left join fetch user.communities",
        countQuery = "select count(user) from User user")
    Page<User> findAllWithAuthorities(Pageable pageable);

    @Override
    void delete(User t);

    @Query("select members from Community community left join community.leader left join community.members as members where community.leader.login = :login")
    Page<User> findMembersOfCommunitiesLeadedBy(@Param("login") String login, Pageable pageable);
}
