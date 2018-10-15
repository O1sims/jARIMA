package arima.api.configuration;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "rserve")
public class RServeConfig {
	
	@NotNull private String hostname;

    @NotNull private String codePath;

    @NotNull private Integer port;
    
    @NotNull private boolean production;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public String getCodePath() {
        return this.codePath;
    }

    public void setCodePath(final String codePath) {
        this.codePath = codePath;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }
    
    public boolean getProduction() {
        return this.production;
    }

    public void setProduction(final boolean production) {
        this.production = production;
    }

    public String getInitiateArimaWorkspaces() {
        return "source(\"" + this.getCodePath() + "/initiateArimaWorkspace.R\")";
    }
}
