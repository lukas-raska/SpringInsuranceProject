package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.data.entities.ClientEntity;
import project.data.repositories.ClientRepository;
import project.models.dtos.ClientDTO;
import project.models.dtos.mappers.ClientMapper;
import project.models.exceptions.ClientNotFoundException;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.PasswordDoNotEqualException;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createNewClient(ClientDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordDoNotEqualException();
        }
        ClientEntity newClient = new ClientEntity();
        newClient = clientMapper.dtoToEntity(dto);
        newClient.setPassword(passwordEncoder.encode(dto.getPassword()));

        try {
            clientRepository.save(newClient);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException();
        }
    }

    @Override
    public void editClient(ClientDTO dto) {
        ClientEntity fetchedClient = clientRepository
                .findById(dto.getId())
                .orElseThrow(ClientNotFoundException::new);
        clientMapper.updateClientEntity(dto, fetchedClient);
        clientRepository.save(fetchedClient);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clientRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }


}
