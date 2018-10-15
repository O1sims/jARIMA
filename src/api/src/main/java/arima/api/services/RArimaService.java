package arima.api.services;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import arima.api.configuration.RServeConfig;


@Service
public class RArimaService {
	
	private RConnection connection;
	private static final Logger LOGGER = LoggerFactory.getLogger(RArimaService.class);
	
	public void initRServeWorkspace(final RServeConfig config) throws RserveException {
        
        if (config.getProduction()) {
        	LOGGER.info("Connecting to Rserve: {}:{}", 
                	config.getHostname(), 
                	config.getPort()
                );
                
                try {
                	this.connection = new RConnection(
                			config.getHostname(),
                			config.getPort()
                	);
                	this.connection.eval(
                			config.getInitiateArimaWorkspaces() + 
                			"; initiateArimaWorkspace()");
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
	
	public double[] performRArima(int[] tsData) throws REXPMismatchException, REngineException {
		this.connection = new RConnection(
    			"rserve",
    			6311);
		
		LOGGER.info("Performing analysis...");
		
		this.connection.assign("tsData", tsData);
		this.connection.eval("javaResults <- performArima(tsData)");
		
		final REXP RScores = this.connection.eval("javaResults");
		
		return RScores.asDoubles();
	}
	
}