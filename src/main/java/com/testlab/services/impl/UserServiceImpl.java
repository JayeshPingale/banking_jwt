package com.testlab.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testlab.DTO.AccountResponseDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.UserRequestDTO;
import com.testlab.DTO.UserResponseDTO;
import com.testlab.DTO.UserUpdateDTO;
import com.testlab.Exception.NotFoundException;
import com.testlab.Repository.RoleRepository;
import com.testlab.Repository.UserRepository;
import com.testlab.entities.Customer;
import com.testlab.entities.Role;
import com.testlab.entities.User;
import com.testlab.mapper.AddressMapper;
import com.testlab.mapper.UserMapper;
import com.testlab.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public UserResponseDTO create(UserRequestDTO reqDTO) {
        User user = UserMapper.toEntity(reqDTO);

        // ✅ Role mapping
        Role role = roleRepo.findByRoleName(reqDTO.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role); // add role to user

        User savedUser = userRepo.save(user);

        return UserMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByID(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepo.findAll();

        return users.stream().map(user -> {
            UserResponseDTO uDto = new UserResponseDTO();
            uDto.setId(user.getUserId());
            uDto.setUserName(user.getUserName());

            // ✅ Map roles (List<Role> → List<String>)
            uDto.setRoles(
                user.getRoles() != null
                        ? user.getRoles().stream()
                                .map(Role::getRoleName)
                                .collect(Collectors.toList())
                        : new ArrayList<>()
            );

            // ✅ Map customer
            Customer c = user.getCustomer();
            if (c != null) {
                CustomerResponseDTO cDto = new CustomerResponseDTO();
                cDto.setCustomerId(c.getCustomerId());
                cDto.setEmailid(c.getEmailid());
                cDto.setContactNo(c.getContactNumber());
                cDto.setDob(c.getDob());
                cDto.setAddress(c.getAddress() != null ? AddressMapper.toResponse(c.getAddress()) : null);

                // ✅ Map accounts
                List<AccountResponseDTO> accountDtos = c.getAccounts() != null
                        ? c.getAccounts().stream().map(a -> {
                            AccountResponseDTO aDto = new AccountResponseDTO();
                            aDto.setAccountId(a.getAccountId());
                            aDto.setAccountNumber(a.getAccountNumber());
                            aDto.setAccountType(a.getAccountType());
                            aDto.setBalance(a.getBalance());
                            aDto.setCustomerId(a.getCustomer() != null ? a.getCustomer().getCustomerId() : null);
                            return aDto;
                        }).collect(Collectors.toList())
                        : new ArrayList<>();
                cDto.setAccounts(accountDtos);

                uDto.setCustomer(cDto);
            }

            return uDto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateDTO req) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        // ✅ sirf password update allowed hai
        if (req.getUserName() != null || req.getRoleId() != null) {
            throw new IllegalArgumentException("Only password can be updated");
        }

        if (req.getPassword() != null) {
            user.setPassword(req.getPassword());
        }

        return UserMapper.toResponse(user);
    }

    @Override
    public void deleteUserbyID(Long id) {
        if (!userRepo.existsById(id)) {
            throw new NotFoundException("User id doesn't exist: " + id);
        }
        userRepo.deleteById(id);
    }

    @Override
    public UserResponseDTO updatePassword(Long userId, String newPassword) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id doesn't exist: " + userId));

        user.setPassword(newPassword);
        userRepo.save(user);

        return UserMapper.toResponse(user);
    }
}
