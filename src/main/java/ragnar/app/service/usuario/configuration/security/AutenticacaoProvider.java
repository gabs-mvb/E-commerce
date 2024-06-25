package ragnar.app.service.usuario.configuration.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ragnar.app.service.usuario.configuration.AutenticacaoService;

public class AutenticacaoProvider implements AuthenticationProvider {

    private final AutenticacaoService autenticacaoService;
    private final PasswordEncoder passwordEncoder;

    public AutenticacaoProvider(AutenticacaoService autenticacaoService, PasswordEncoder passwordEncoder) {
        this.autenticacaoService = autenticacaoService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();

        UserDetails userDetails = this.autenticacaoService.loadUserByUsername(email);
        if (this.passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        throw new BadCredentialsException("Usuário ou Senha Inválidos");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
