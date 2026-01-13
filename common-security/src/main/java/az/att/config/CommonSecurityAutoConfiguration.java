package az.att.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@AutoConfiguration
@ComponentScan(basePackages = {
        "az.att.auth",
        "az.att.config"
        
})
public class CommonSecurityAutoConfiguration {
    
    
}

