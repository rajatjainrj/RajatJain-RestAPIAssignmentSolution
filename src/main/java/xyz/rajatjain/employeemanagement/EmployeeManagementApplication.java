package xyz.rajatjain.employeemanagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import xyz.rajatjain.employeemanagement.services.PostStartupScript;

@Slf4j
@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class EmployeeManagementApplication {

    private PostStartupScript postStartupScript;

    @Autowired
    public void setPostStartupScript(PostStartupScript postStartupScript) {
        this.postStartupScript = postStartupScript;
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        log.info("Employee Management System just started up!");
        postStartupScript.initializeRolesAndDefaultEmployees();
    }

}
