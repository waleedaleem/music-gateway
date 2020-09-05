package com.walid.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author wmoustaf
 */
@EnableZuulProxy
@SpringBootApplication
public class MusicGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicGatewayApplication.class, args);
    }

}
