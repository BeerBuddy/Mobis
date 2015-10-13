package de.fh.dortmund.beerbuddy.backend;

import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Dominik Broj on 17.09.2015.
 *
 * @author Dominik Broj
 * @since 17.09.2015
 */
@SuppressWarnings("unused")
@Component
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do here
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String[][] headers = {
                {"Access-Control-Allow-Origin", "*"},
                {"Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, PATCH, HEAD"},
                {"Access-Control-Max-Age", "3600"},
                {"Access-Control-Allow-Headers", "x-requested-with"}
        };
        for (String[] header : headers) {
            httpServletResponse.setHeader(header[0], header[1]);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to do here
    }
}
