package beer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableConfigurationProperties
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
