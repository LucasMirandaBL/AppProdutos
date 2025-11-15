package br.com.fabreum.AppProdutos.security;

import br.com.fabreum.AppProdutos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço para carregar os detalhes do usuário a partir do banco de dados.
 * Implementa a interface UserDetailsService do Spring Security.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Localiza um usuário pelo seu nome de usuário (username).
     * Este método é chamado pelo Spring Security durante o processo de autenticação.
     *
     * @param username O nome de usuário a ser localizado.
     * @return Um objeto UserDetails (no nosso caso, a própria entidade Usuario).
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o username: " + username));
    }
}
