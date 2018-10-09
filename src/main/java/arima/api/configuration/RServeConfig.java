package arima.api.configuration;

import javax.validation.constraints.NotNull;

public class RServeConfig {

    @NotNull private String hostname = "rserve";

    @NotNull private String codePath = "/opt/arima";

    @NotNull private Integer port = 6311;

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

    public String getInitiateRServeWorkspace() {
        return "source(\"" + this.getCodePath() + "/R/initiateRServeWorkspace.R\")";
    }
}
