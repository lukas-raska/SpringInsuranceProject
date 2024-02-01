package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.data.entities.EmployeeEntity;
import project.data.repositories.EmployeeRepository;
import project.models.dtos.UserRegisterDTO;
import project.models.dtos.mappers.UserMapper;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.PasswordDoNotEqualException;

import java.time.LocalDate;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public void create(UserRegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new PasswordDoNotEqualException();
        }
        EmployeeEntity newEmployee = userMapper.mapToEmployee(dto);
        newEmployee.setPassword(passwordEncoder.encode(dto.getPassword()));
        newEmployee.setDateOfRegistration(LocalDate.now());
        try{
            employeeRepository.save(newEmployee);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateEmailException();
        }
    }
}
