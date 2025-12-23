package az.att.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.web.filter.OncePerRequestFilter;

public class CaptureRedirectParamFilter extends OncePerRequestFilter {
    public static final String ATTR = "REDIRECT_AFTER_LOGIN";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, java.io.IOException {

        // Matches: /{contextPath}/oauth2/authorization/{registrationId}
        String prefix = req.getContextPath() + "/oauth2/authorization/";
        if (req.getRequestURI().startsWith(prefix)) {
            String target = req.getParameter("redirect");
            if (target != null && !target.isBlank()) {
                req.getSession(true).setAttribute(ATTR, target);
            }
        }
        chain.doFilter(req, res);
    }
}
