package org.mycompany.test;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
@Getter @Setter
public class DemoApplication {

    private String url;
    private String username;
    private String password;

    @Override
    public String toString() {
        return "DemoApplication { "+
                "url= " + url +'\n'+
                "username= " + username +'\n'+
                "password= " + password +'\n'+" }";

    }
}
