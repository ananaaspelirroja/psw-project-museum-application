package it.project.services;

import it.project.entity.User;
import it.project.repositories.UserRepository;
import it.project.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeycloakService keycloakService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public User registerUser(User user) throws Exception {
        // Creare utente in Keycloak
        int userId = keycloakService.createUser(user).getId();

        // Creare l'utente anche nel database locale se l'inserimento in Keycloak ha successo
        if (userId != 0) {
            user.setId(userId);
            return userRepository.save(user); // Salva l'utente nel database
        } else {
            throw new Exception("Impossibile creare l'utente in Keycloak.");
        }
    }


    @Transactional(readOnly = true)
    public User findUserById(int userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }
}
