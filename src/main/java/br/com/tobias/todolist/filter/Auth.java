package br.com.tobias.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.tobias.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Auth extends OncePerRequestFilter {

    private final IUserRepository userRepository;

    public Auth(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var path = request.getServletPath();
        var isProtected = path.startsWith("/tasks/") || path.startsWith("/users/me");
        if (!isProtected || request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        var credentials = extractCredentials(request);
        if (credentials == null) {
            response.sendError(401, "Authorization header missing or malformed");
            return;
        }

        var user = userRepository.findByUsername(credentials[0]);
        if (user == null) {
            response.sendError(401, "User not authenticated!");
            return;
        }

        var result = BCrypt.verifyer().verify(credentials[1].toCharArray(), user.getPassword());
        if (!result.verified) {
            response.sendError(401, "Wrong password!");
            return;
        }

        request.setAttribute("userID", user.getId());
        filterChain.doFilter(request, response);
    }

    private String[] extractCredentials(HttpServletRequest request) {
        var auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Basic ")) return null;

        var encoded = auth.substring("Basic ".length()).trim();
        var decoded = new String(Base64.getDecoder().decode(encoded));
        return decoded.split(":", 2);
    }
}
