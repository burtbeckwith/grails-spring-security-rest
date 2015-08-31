import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

beans = {

    userDetailsService(InMemoryUserDetailsManager, [])

    passwordEncoder(PlaintextPasswordEncoder)
}
