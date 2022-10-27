package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.User;

public class UserBuilder {

    private UserBuilder() {
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getPassword(), user.isAdmin(), user.getDevices());
    }

    public static User toUserEntity(UserDTO userDTO) {
        return new User(userDTO.getName(), userDTO.getPassword(), userDTO.isAdmin(), userDTO.getDevices());
    }

}
