package tech.buildrun.safenotes.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import tech.buildrun.safenotes.entity.Role;
import tech.buildrun.safenotes.entity.Scope;
import tech.buildrun.safenotes.entity.User;
import tech.buildrun.safenotes.repository.RoleRepository;
import tech.buildrun.safenotes.repository.ScopeRepository;
import tech.buildrun.safenotes.repository.UserRepository;

import java.util.Set;

@Component
public class RBACSeed implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ScopeRepository scopeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RBACSeed(RoleRepository roleRepository, UserRepository userRepository, ScopeRepository scopeRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.scopeRepository = scopeRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {

        //1 - criar as scopes
        Scope readScope = ensureScope("NOTE:READ");
        Scope writeScope = ensureScope("NOTE:WRITE");
        Scope profileRead = ensureScope("PROFILE:READ");

        //2 - criar os papeis e associar as scopes
        Role userViewer = ensureRole("VIEWER", Set.of(profileRead));
        Role userRole = ensureRole("USER", Set.of(readScope, writeScope, profileRead));

        // 3 - criar os usuarios e associar os papeis
        ensureUser("emily", "senha", userViewer);
        ensureUser("erick", "senha", userRole);
        ensureUser("bruno", "senha", userRole);

    }

    private Scope ensureScope(String name) {
        return scopeRepository.findByName(name)
                .orElseGet(() -> scopeRepository.save(new Scope(null, name)));
    }

    private Role ensureRole(String name, Set<Scope> scopes) {

        return roleRepository.findByName(name)
                .map(existingRole -> {
                    existingRole.setScopes(scopes);
                    return roleRepository.save(existingRole);
                })
                .orElseGet(() -> roleRepository.save(new Role(null, name, scopes)));

    }

    private User ensureUser(String username, String password, Role roles) {

        userRepository.findByUsername(username)
                .map(user ->{
                    user.setPassword(bCryptPasswordEncoder.encode(password));
                    user.setRoles(Set.of(roles));
                    return userRepository.save(user);
                })
                .orElseGet(() -> userRepository.save(new User(username, bCryptPasswordEncoder.encode(password), Set.of(roles), 1)));
        return null;

    }


}
