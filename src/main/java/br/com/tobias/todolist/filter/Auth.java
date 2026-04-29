package br.com.tobias.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    IUserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    	throws ServletException, IOException {
    
        var servletPath = request.getServletPath();
         
        if (servletPath.startsWith("/tasks/")) {
            //Take user info
            var auth = request.getHeader("Authorization");
            
            // Remove Basic word and get only the password
            var password = auth.substring("Basic".length()).trim();
            
            // Decode the password
            byte[] authDecode = Base64.getDecoder().decode(password);
            
            var userAuth = new String(authDecode);
            
            // Split the words to get user and pass
            String[] creadentials = userAuth.split(":");
            
            String username = creadentials[0];
            String pass = creadentials[1];
            
            var user = userRepository.findByUsername(username);
            
            // Check if the user extis
            if (user == null){
                response.sendError(401, "User not authenticated!");
            } else {
                var authenticated = BCrypt.verifyer().verify(pass.toCharArray(), user.getPassword());
                
                if (authenticated.verified) {
                    request.setAttribute("userID", user.getId());
                   	filterChain.doFilter(request, response);
                } else {
                    response.sendError(401, "Wrong password!");
                }
            }
        } else {
           	filterChain.doFilter(request, response);
        }
	}
}
