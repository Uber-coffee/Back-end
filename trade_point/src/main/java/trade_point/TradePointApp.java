package trade_point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


//@EnableConfigurationProperties(TradePointApp.class)
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class TradePointApp {
    public static void main(String[] args) {
        SpringApplication.run(TradePointApp.class, args);
    }
}
