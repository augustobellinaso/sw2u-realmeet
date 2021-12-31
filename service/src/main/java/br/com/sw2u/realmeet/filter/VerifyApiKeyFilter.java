package br.com.sw2u.realmeet.filter;

import br.com.sw2u.realmeet.domain.entity.Client;
import br.com.sw2u.realmeet.domain.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;


import static org.apache.commons.lang3.StringUtils.isBlank;

public class VerifyApiKeyFilter extends GenericFilterBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyApiKeyFilter.class);
    private static final String HEADER_API_KEY = "api-key";
    
    private final ClientRepository clientRepository;
    
    public VerifyApiKeyFilter(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;
        
        var apiKey = httpRequest.getHeader(HEADER_API_KEY);
        
        if (isBlank(apiKey)) {
            sendUnauthorizedError(httpResponse, apiKey);
        } else if (isValidApiKey(apiKey)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            sendUnauthorizedError(httpResponse, apiKey);
        }
    }
    
    private boolean isValidApiKey(String apiKey) {
        return clientRepository.findById(apiKey)
                .filter(Client::getActive)
                .stream()
                .peek(c -> LOGGER.info("Valid API key: '{}' ({})", c.getApiKey(), c.getDescription()))
                .findFirst()
                .isPresent();
    }
    
    private void sendUnauthorizedError(HttpServletResponse httpResponse, String apiKey) throws IOException {
        var errorMessage = isBlank(apiKey) ? "API key is missing" : "API key is invalid";
        LOGGER.error(errorMessage);
        
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setContentLength(errorMessage.length());
        httpResponse.setContentType("text/plain");
        
        try (Writer out = httpResponse.getWriter()) {
            out.write(errorMessage);
        }
    }
}
