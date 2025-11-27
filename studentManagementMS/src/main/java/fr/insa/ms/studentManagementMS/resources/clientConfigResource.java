package fr.insa.ms.studentManagementMS.resources;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class clientConfigResource {
	
	@Value("${server.port}")
	private String serverPort;
	@Value("${db.connection}")
	private String typeConnectionDeBD;
	@Value("${db.host}")
	private String dbHost;
	@Value("${db.port}")
	private String dbPort;


	public String getServerPort() {
	    return serverPort;
	}
	public void setServerPort(String serverPort) {
	    this.serverPort = serverPort;
	}
	public String getTypeConnectionDeBD() {
	    return typeConnectionDeBD;
	}
	public void setTypeConnectionDeBD(String typeConnectionDeBD) {
	    this.typeConnectionDeBD = typeConnectionDeBD;
	}
	public String getDbHost() {
	    return dbHost;
	}
	public void setDbHost(String dbHost) {
	    this.dbHost = dbHost;
	}
	public String getDbPort() {
	    return dbPort;
	}
	public void setDbPort(String dbPort) {
	    this.dbPort = dbPort;
	}
}
