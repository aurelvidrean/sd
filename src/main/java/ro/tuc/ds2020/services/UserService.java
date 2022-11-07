package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findUserById(UUID id) {
        Optional<User> prosumerOptional = userRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Find-User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        return UserBuilder.toUserDTO(prosumerOptional.get());
    }

    public UUID insert(UserDTO userDTO) {
        User user = UserBuilder.toUserEntity(userDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

    public UUID update(UserDTO userDTO) {
        Optional<User> prosumerOptional = userRepository.findById(userDTO.getId());
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Update-User with id {} was not found in db", userDTO.getId());
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + userDTO.getId());
        }
        User userToUpdate = UserBuilder.toUserEntity(userDTO);
        userToUpdate.setId(userDTO.getId());
        userRepository.save(userToUpdate);
        return userToUpdate.getId();
    }

    public UUID delete(UUID id) {
        Optional<User> prosumerOptional = userRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Delete-User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        List<Device> userDevices = prosumerOptional.get().getDevices();
        Optional<User> u = userRepository.findById(UUID.fromString("b4527c7f-c7fb-489a-8786-f332e1d81de4"));
        if (u.isPresent()) {
            User admin = u.get();
            List<Device> adminDevices = admin.getDevices();
            adminDevices.addAll(userDevices);
            admin.setDevices(adminDevices);
            userRepository.save(admin);
            prosumerOptional.get().setDevices(null);
            userRepository.delete(prosumerOptional.get());
            return prosumerOptional.get().getId();
        } else {
            LOGGER.error("Delete-User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
    }

    public UserDTO getUserCredentials(String name, String password) {
        Optional<User> userOptional = userRepository.getUserCredentials(name, password);
        if(!userOptional.isPresent()){
            LOGGER.error("User with name and password {} was not found in db", name);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + name);
        }
        return UserBuilder.toUserDTO(userOptional.get());
    }
}