package com.restapi.app.twittor.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.restapi.app.twittor.restFileUpload.controller.FileUploadController;
import com.restapi.app.twittor.security.service.UserDetailsServiceImpl;
import com.restapi.app.twittor.securityJwt.JwtEntryPoint;
import com.restapi.app.twittor.securityJwt.JwtTokenFilter;

@Configuration
@EnableWebSecurity
//con perPostEnabled se usa para indicar a q metodos puede acceder solo el admin
// Los metodos que no lleven anotación pueden acceder el admin como un generic user
// @preauthorized solo puede acceder el admin
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
    UserDetailsServiceImpl userDetailsService;

    //Devuelve el mensaje de no autorizado
    @Autowired
    JwtEntryPoint jwtEntryPoint;

    private final static Logger logger = LoggerFactory.getLogger(MainSecurity.class);

    
    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(8);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        logger.info("aca es conf¡gure decoder");
    	auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
    		"/auth/**",
    		"/registro",
    		"/obtenerBanner",
    		"/obtenerAvatar",
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**"
            // other public endpoints of your API may be appended to this array
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Desactivamos cookies ya que enviamos un token
            // cada vez que hacemos una petición
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and().csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
    }
}