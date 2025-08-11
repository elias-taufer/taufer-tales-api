package com.taufer.tales.authgateway;
import jakarta.servlet.*; import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.RequiredArgsConstructor;

@Component @RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtService jwt;
  private final UserDetailsService uds;

  @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws java.io.IOException, ServletException {
    String h = req.getHeader("Authorization");
    if (h!=null && h.startsWith("Bearer ")){
      String token = h.substring(7);
      String username = null;
      try { username = jwt.extractUsername(token); } catch(Exception ignored){}
      if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
        UserDetails user = uds.loadUserByUsername(username);
        if(jwt.isValid(token, user)){
          var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
          auth.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(req));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
    }
    chain.doFilter(req,res);
  }
}
