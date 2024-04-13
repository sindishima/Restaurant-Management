package com.example.HungryNet.Security;

//import com.example.HungryNet.Security.filters.JwtTokenVerifier;
//import com.example.HungryNet.Security.filters.JwtUsernamePasswordAuthenticationFilter;
import com.example.HungryNet.Security.filters.JwtTokenVerifier;
import com.example.HungryNet.Security.filters.JwtUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.authorizeRequests()
//                .antMatchers( "/", "/login/**").permitAll()
//                .anyRequest().authenticated()
//            .and()
////            .formLogin()
////                .loginPage("/login")
////                .permitAll()
////                .defaultSuccessUrl("/admin", true)
////                .passwordParameter("password")
////                .usernameParameter("username")
////             .and()
//                .httpBasic() ;



        JwtUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new JwtUsernamePasswordAuthenticationFilter(authenticationManagerBean());
        usernamePasswordAuthenticationFilter.setFilterProcessesUrl("/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManagerBean()))
                .addFilterAfter(new JwtTokenVerifier(), JwtUsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests().antMatchers("/login/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",   //this URLs are not secured
//                "/configuration/ui",
                "/swagger-resources/**",
//                "/configuration/security",
                "/swagger-ui.html"
//                "/webjars/**"
        );
    }

}
