package auth.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value(value = "${application.firebase.configUri}")
    private String configUri;

    @PostConstruct
    public void init() {
        try {
            InputStream is = new FileInputStream(new File(configUri));

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(is))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.error("Cannot initialize Firebase App with config uri: {}.", configUri);
        }
    }
}
