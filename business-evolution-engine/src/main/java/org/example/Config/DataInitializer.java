package org.example.config;

import org.example.entities.Role;
import org.example.entities.User;
import org.example.repositories.RoleRepository;
import org.example.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               RoleRepository roleRepository) {

        return args -> {

            // 1️⃣ Créer le rôle MANAGER s’il n’existe pas
            Role managerRole = createRoleIfNotExists(roleRepository, "Manager",
                    "Gère l'ensemble des projets et équipes");

            Role chefEquipeRole = createRoleIfNotExists(roleRepository, "Chef d equipe",
                    "Suit l'avancement de son équipe");

            Role membreRole = createRoleIfNotExists(roleRepository, "Membre",
                    "Exécute les tâches assignées");

            // 2️⃣ Créer l'utilisateur MANAGER par défaut s’il n’existe pas
            if (!userRepository.existsByEmail("manager@admin.com")) {

                User manager = new User();
                manager.setUsername("admin");
                manager.setEmail("manager@admin.com");
                manager.setPassword(
                        new BCryptPasswordEncoder().encode("admin123")
                );
                manager.setPhoneNumber("00000000");
                manager.setAddress("SYSTEM");
                manager.setRole(managerRole);
                manager.setTeam(null); // MANAGER n’appartient à aucune équipe

                userRepository.save(manager);

                System.out.println("✅ Utilisateur MANAGER par défaut créé");
                System.out.println("📧 Email : manager@admin.com");
                System.out.println("🔑 Password : admin123");
            } else {
                System.out.println("ℹ️ Utilisateur MANAGER déjà existant");
            }
        };
    }
    private Role createRoleIfNotExists(RoleRepository roleRepository, String title, String Description) {
        return roleRepository.findByTitle(title)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setTitle(title);
                    role.setDescription(Description);
                    return roleRepository.save(role);
                });
    }
}
