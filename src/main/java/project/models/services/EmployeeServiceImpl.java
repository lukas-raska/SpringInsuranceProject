package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.data.entities.EmployeeEntity;
import project.data.repositories.EmployeeRepository;
import project.models.dtos.UserDisplayDTO;
import project.models.dtos.UserEditDTO;
import project.models.dtos.UserRegisterDTO;
import project.models.dtos.mappers.UserMapper;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.EmployeeNotFoundException;
import project.models.exceptions.PasswordDoNotEqualException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementace rozhraní {@link EmployeeService}
 * Definuje implementaci service metod pro práci se zaměstanci
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
       private EmployeeRepository employeeRepository;




    /**
     * Vytvoří nového zaměstance a uloží do databáze
     *
     * @param dto - data předaná z registračního formuláře {@link UserRegisterDTO}
     */
    @Override
    public void create(UserRegisterDTO dto) {
        //výjimka v případě nestejně zadaných hesel (zachytává se v controlleru)
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordDoNotEqualException();
        }
        //vytvoření nového zaměstance
        EmployeeEntity newEmployee = userMapper.mapToEmployee(dto);
        newEmployee.setPassword(passwordEncoder.encode(dto.getPassword())); //heslo vynecháno z UserMapper, nutno vytvořit přes hashovací metodu
        newEmployee.setDateOfRegistration(LocalDate.now());

        //pokus o uložení a zachycení výjimky při pokusu o uložení duplicitního emailu
        try {
            employeeRepository.save(newEmployee);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException();
        }
    }

    /**
     * Získá záznamy o všech zaměstancích v databázi
     *
     * @return - seznam zaměstnanců
     */
    @Override
    public List<UserDisplayDTO> getAll() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employeeEntity -> userMapper.mapToDTO(employeeEntity))
                .collect(Collectors.toList());
    }

    /**
     * Edituje data u stávající entity
     *
     * @param dto {@link UserEditDTO}
     */
    @Override
    public void edit(UserEditDTO dto) {
        //načtení editované entity z databáze
        EmployeeEntity fetchedEmployee = employeeRepository
                .findById(dto.getId())
                .orElseThrow(EmployeeNotFoundException::new);
        //upraví údaje v načtené entitě dle dat předaných dto
        userMapper.updateEmployeeEntity(dto, fetchedEmployee);
        //upravenou entitu uložím
        employeeRepository.save(fetchedEmployee);

    }

    /**
     * Získá zaměstnance z databáze dle zadaného id
     * @param id Identifikátor zaměstnance
     * @return - vrací informace o zaměstnanci ve formě {@link UserDisplayDTO}
     */
    @Override
    public UserDisplayDTO getById(long id) {
        EmployeeEntity fetchedEmployee = employeeRepository
                .findById(id)
                .orElseThrow(EmployeeNotFoundException::new);
        return userMapper.mapToDTO(fetchedEmployee);
    }

    /**
     * Slouží k odstranění záznamu o zaměstnanci z databáze
     * @param id - identifikátor zaměstance
     */
    @Override
    public void remove(long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public int getAge(LocalDate dateOfBirth) {
        return 0;
    }
}
