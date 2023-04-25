package co.gov.sic.encuestas.config;

import co.gov.sic.encuestas.domain.User;
import co.gov.sic.encuestas.repository.UserRepository;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {
        super.onAuthenticationFailure(request, response, exception);

        String email = request.getParameter("username");
        Optional<User> userOptional = userRepository.findOneByLogin(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int intentos = user.getFailedAttempts() != null ? user.getFailedAttempts() : 0;
            user.setFailedAttempts(intentos + 1);
            if (user.getFailedAttempts() >= 3) {
                user.setLockedAt(true);
                user.setActivated(false);
            }
            userRepository.save(user);
        }
    }
}
