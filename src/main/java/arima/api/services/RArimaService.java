package arima.api.services;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arima.api.configuration.RServeConfig;

public class RArimaService {
	
	private final RServeConfig config;
	private final RConnection connection;
	private static final Logger LOGGER = LoggerFactory.getLogger(RArimaService.class);
	
	public RArimaService(final Integer port, final RServeConfig config) throws RserveException {
        this.config = config;

        LOGGER.info("Connecting to classifier: {}:{}", this.config.getHostname(), port);
        this.connection = new RConnection(this.config.getHostname(), port);
    }
	
	public void initiateRserveWorkspace() throws RserveException {
        this.connection.eval(this.config.getInitiateRServeWorkspace());
    }
	
	
}