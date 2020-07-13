package auth.service.user_details;

import auth.entity.Customer;
import auth.entity.Role;
import auth.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerDetailsServiceTest {

    private CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final Map<String, Customer> customers = new HashMap<>();

    @BeforeEach
    public void init(){
        customerRepository = mock(CustomerRepository.class);
        when(customerRepository.findByPhoneNumber(anyString())).thenAnswer(
                i-> customers.get(i.getArgument(0))
        );
        Customer customer = new Customer();
        customer.setId((long)1);
        customer.setFirstName("test");
        customer.setLastName("testovich");
        customer.setPhoneNumber("88005553535");
        customers.put(customer.getPhoneNumber(), customer);
    }

    @Test
    void loadUserByUsername() {
        CustomerDetailsService customerDetailsService = new CustomerDetailsService(customerRepository, passwordEncoder);
        UserDetails userDetails = customerDetailsService.loadUserByUsername("88005553535");
        assertNotNull(userDetails);
        assertEquals(userDetails.getUsername(), "1");
        assertTrue(passwordEncoder.matches("", userDetails.getPassword()));
        assertArrayEquals(userDetails.getAuthorities().toArray(), List.of(Role.ROLE_CUSTOMER).toArray());
    }
}