package br.com.fabreum.AppProdutos.config;

import br.com.fabreum.AppProdutos.model.Role;
import br.com.fabreum.AppProdutos.model.Usuario;
import br.com.fabreum.AppProdutos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Componente para popular o banco de dados com dados iniciais na inicialização da aplicação.
 * Útil para testes e demonstração.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Cria o usuário ADMIN se ele não existir
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            // A senha deve ser sempre codificada antes de ser salva
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(Role.ROLE_ADMIN));
            usuarioRepository.save(admin);
        }

        // Cria o usuário SELLER se ele não existir
        if (usuarioRepository.findByUsername("seller").isEmpty()) {
            Usuario seller = new Usuario();
            seller.setUsername("seller");
            seller.setPassword(passwordEncoder.encode("seller123"));
            seller.setRoles(Set.of(Role.ROLE_SELLER));
            usuarioRepository.save(seller);
        }

        // Cria o usuário CUSTOMER se ele não existir
        if (usuarioRepository.findByUsername("customer").isEmpty()) {
            Usuario customer = new Usuario();
            customer.setUsername("customer");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setRoles(Set.of(Role.ROLE_CUSTOMER));
            usuarioRepository.save(customer);
        }
    }
}
