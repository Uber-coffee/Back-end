package auth.service.user_details;

import auth.entity.Customer;
import auth.entity.Role;
import auth.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomerDetailsService(CustomerRepository customerRepository,
                                  PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        final Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
        if (customer == null) throw new UsernameNotFoundException("");
        return org.springframework.security.core.userdetails.User.builder()
                .username(Long.toString(customer.getId()))
                .password(passwordEncoder.encode(""))
                .authorities(List.of(Role.ROLE_CUSTOMER))
                .disabled(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .accountExpired(false)
                .build();
    }
}
