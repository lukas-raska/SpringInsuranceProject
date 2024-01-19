package project.models.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.models.dtos.ClientEditDTO;
import project.models.dtos.ClientRegistrationDTO;

public interface ClientService extends UserDetailsService {

    ClientEditDTO createNewClient(ClientRegistrationDTO dto);


    void editClient (ClientEditDTO clientEditDTO);


}
