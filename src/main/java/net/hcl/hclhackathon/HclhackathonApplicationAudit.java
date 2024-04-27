
package net.hcl.hclhackathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@ComponentScan("net.hcl.hclhackathon")
public class HclhackathonApplicationAudit {

	public static void main(String[] args) {
		SpringApplication.run(HclhackathonApplicationAudit.class, args);
	}

}

