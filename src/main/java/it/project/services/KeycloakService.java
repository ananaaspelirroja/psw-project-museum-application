package it.project.services;

import it.project.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import it.project.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.client-secret}")
    private String keycloakClientSecret;

    private Keycloak keycloak;

    @Autowired
    private UserRepository userRepository; // Repository per gestire il database interno degli utenti

    @PostConstruct
    private void initialize() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakAuthServerUrl)
                .realm(keycloakRealm)
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .grantType("client_credentials")
                .build();
    }

    // Metodo per creare un nuovo utente in Keycloak e nel database locale
    public User createUser(User user) {
        // Crea l'utente in Keycloak
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        credential.setTemporary(false);
        userRepresentation.setCredentials(List.of(credential));

        Response response = keycloak.realm(keycloakRealm).users().create(userRepresentation);
        if (response.getStatus() == 201) { // HTTP 201 Created
            String location = response.getHeaderString("Location");
            String keycloakId = location.substring(location.lastIndexOf('/') + 1);
            response.close();

            // Salva l'utente anche nel database locale con l'ID Keycloak
            user.setId(Integer.parseInt(keycloakId));
            return userRepository.save(user);
        } else {
            response.close();
            throw new RuntimeException("Errore durante la creazione dell'utente in Keycloak: " + response.getStatusInfo());
        }
    }

    // Metodo per ottenere un utente da Keycloak e mappare direttamente su User
    public User getUserByUsername(String username) {
        List<UserRepresentation> users = keycloak.realm(keycloakRealm).users().search(username);
        if (users.isEmpty()) {
            return null;
        }

        UserRepresentation userRepresentation = users.get(0);
        User user = new User();
        user.setId(Integer.parseInt(userRepresentation.getId()));
        user.setUsername(userRepresentation.getUsername());
        user.setEmail(userRepresentation.getEmail());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());

        return user;
    }

    // Metodo per ottenere tutti gli utenti da Keycloak e restituirli come entità User
    public List<User> getUsers() {
        List<UserRepresentation> userRepresentations = keycloak.realm(keycloakRealm).users().list();
        List<User> users = new ArrayList<>();

        for (UserRepresentation userRepresentation : userRepresentations) {
            User user = new User();
            user.setId(Integer.parseInt(userRepresentation.getId()));
            user.setUsername(userRepresentation.getUsername());
            user.setEmail(userRepresentation.getEmail());
            user.setFirstName(userRepresentation.getFirstName());
            user.setLastName(userRepresentation.getLastName());
            users.add(user);
        }

        return users;
    }

    // Metodo per eliminare un utente da Keycloak e dal database locale
    public void deleteUser(String id) {
        try {
            keycloak.realm(keycloakRealm).users().delete(id);
            userRepository.deleteById(Integer.parseInt(id));
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'eliminazione dell'utente con ID: " + id, e);
        }
    }
}
