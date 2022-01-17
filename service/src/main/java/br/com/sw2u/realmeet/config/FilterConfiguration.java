package br.com.sw2u.realmeet.config;

import br.com.sw2u.realmeet.domain.repository.ClientRepository;
import br.com.sw2u.realmeet.filter.VerifyApiKeyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {
    
    private final ClientRepository clientRepository;
    
    public FilterConfiguration(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    @Bean
    public FilterRegistrationBean<VerifyApiKeyFilter> verifyApiKeyFilterFilterRegistrationBean() {
        var filterFilterRegistrationBean = new FilterRegistrationBean<VerifyApiKeyFilter>();
        filterFilterRegistrationBean.setFilter(new VerifyApiKeyFilter(clientRepository));
        filterFilterRegistrationBean.addUrlPatterns("/rooms/*", "/allocations/*");
        return filterFilterRegistrationBean;
    }
    
}
