package pub.ron.jwt;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pub.ron.jwt.domain.Permission;
import pub.ron.jwt.domain.Role;
import pub.ron.jwt.domain.User;
import pub.ron.jwt.repository.PermissionRepository;
import pub.ron.jwt.repository.RoleRepository;
import pub.ron.jwt.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtApplicationTests {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @Before
    public void initData() {
        Set<Permission> permissions = new HashSet<>();
        for (String permission : new String[] {"add", "delete", "update", "query"}) {
            permissions.add(new Permission(permission));

        }
        permissionRepository.saveAll(permissions);
        Role role = new Role("admin");
        role.setPermissions(permissions);
        roleRepository.save(role);

        User user = new User();
        user.setRealName("ron");
        user.setUsername("ron");
        user.setPassword("123");
        HashSet<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Test
    public void contextLoads() {

//        MockMvcRequestBuilders.
    }

}

