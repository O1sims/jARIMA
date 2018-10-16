package arima.api.services;

import javax.validation.constraints.NotNull;

import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class RArimaService {
	
	@Value("${rserve.hostname}") 
	@NotNull private String hostname;
	
	@Value("${rserve.port}") 
	@NotNull private int port;
	
	@Value("${rserve.production}")
	@NotNull private boolean production;
	
	private RConnection connection;
	
	private final Logger LOGGER = LoggerFactory.getLogger(RArimaService.class);
	
	public void performArima(int[] tsData) throws Exception {
    	
		LOGGER.info("Connecting to Rserve: {}:{}", this.hostname, this.port);
		
        if (this.production) {
                try {
                	this.connection = new RConnection(
                			this.hostname,
                			this.port);
                	this.connection.assign("tsData", tsData);
                	this.connection.voidEval("dffv <- data.frame(forecast::forecast(forecast::auto.arima(tsData), 1))");
                	final Double hi95 = this.connection.eval("dffv$Hi.95").asDouble();
                	final Double lo95 = this.connection.eval("dffv$Lo.95").asDouble();
                	LOGGER.info(hi95.toString());
                	LOGGER.info(lo95.toString());
                } catch (Exception e) {
                	LOGGER.error("Failed initializing Rserve connection.", e);
                	throw e;
                } finally {
                	LOGGER.info("Successfully connected to Rserve and created workspace");
                };
        } else {
        	LOGGER.info("This is not a production build -- not connecting to Rserve");
        };
    }

}