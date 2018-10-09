package arima.api;

import arima.api.services.RArimaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
    public CommandLineRunner commandLineRunner(
    		ApplicationContext ctx, 
    		RArimaService rArimaService) {
        return args -> {
        	LOGGER.info("Setting up RServe environment...");
            rArimaService.initiateRserveWorkspace();
        };
	};
}
