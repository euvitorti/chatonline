package br.chat.ChatOnline.repository.user;

import br.chat.ChatOnline.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    UserDetails findByUsername(String login);
}