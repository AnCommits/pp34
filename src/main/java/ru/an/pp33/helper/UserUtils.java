package ru.an.pp33.helper;

import org.springframework.stereotype.Component;
import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;
import ru.an.pp33.service.UserService;

@Component
public class UserUtils {
    private final UserService userService;

    public UserUtils(UserService userService) {
        this.userService = userService;
    }

    /**
     * Indicates if user2 or his creator created user1
     */
    public boolean isAncestor(User user1, User user2) {
        long parentAdminId1 = user1.getParentAdminId();
        if (parentAdminId1 == 0) {
            return false;
        }
        if (parentAdminId1 == user2.getId()) {
            return true;
        }
        User user = userService.getUser(parentAdminId1);
        if (user == null) {
            return false;
        }
        return isAncestor(user, user2);
    }

    public void setUsersParentAdminId(User user, User me) {
        User userBefore = userService.getUser(user.getId());
        user.setParentAdminId(
                hasRoleAdmin(userBefore) != hasRoleAdmin(user)
                        ? me.getId()
                        : userBefore.getParentAdminId());
    }

    public static boolean hasRoleAdmin(User user) {
        return user.getRoles().stream().map((Role::getName)).anyMatch(r -> r.equals("ADMIN"));
    }
}
