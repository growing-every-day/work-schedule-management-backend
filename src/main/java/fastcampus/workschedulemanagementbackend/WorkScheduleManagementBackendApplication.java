package fastcampus.workschedulemanagementbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackages = "fastcampus.workschedulemanagementbackend.common")
@SpringBootApplication
public class WorkScheduleManagementBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkScheduleManagementBackendApplication.class, args);
    }

}
