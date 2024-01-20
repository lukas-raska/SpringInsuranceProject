package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import project.models.dtos.ClientDTO;

public interface ClientService extends UserDetailsService {



    void createNewClient(ClientDTO dto);


    void editClient (ClientDTO dto);


    ClientDTO getClientById (long id);





}
