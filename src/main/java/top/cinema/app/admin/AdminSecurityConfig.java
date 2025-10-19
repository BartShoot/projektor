package top.cinema.app.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import top.cinema.app.user.JpaUserDetailsService;

import javax.sql.DataSource;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AdminSecurityConfig {

    private final JpaUserDetailsService jpaUserDetailsService;
    private final DataSource dataSource;

    public AdminSecurityConfig(JpaUserDetailsService jpaUserDetailsService, DataSource dataSource) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults())
                .securityMatcher("/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/api/**",
                                "/styles/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/admin/login",
                                "/user/register")
                        .permitAll()
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .formLogin(form -> form.loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout")
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe.tokenRepository(persistentTokenRepository()))
                .userDetailsService(jpaUserDetailsService)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/user/register"));

        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
