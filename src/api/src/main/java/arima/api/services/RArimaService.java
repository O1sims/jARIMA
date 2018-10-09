package arima.api.services;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arima.api.configuration.RServeConfig;

@Service
public class RArimaService {
	
	private final RServeConfig config;
	private final RConnection connection;
	private static final Logger LOGGER = LoggerFactory.getLogger(RArimaService.class);
	
	public RArimaService(final RServeConfig config) throws RserveException {
        this.config = config;

        LOGGER.info("Connecting to classifier: {}:{}", 
        		this.config.getHostname(), 
        		this.config.getPort());
        
        this.connection = new RConnection(
        		this.config.getHostname(),
        		this.config.getPort());
    }
	
	public void initiateRserveWorkspace() throws RserveException {
		LOGGER.info("Initiating RServe workspace...");
        this.connection.eval(this.config.getInitiateRServeWorkspace());
    }
	
}