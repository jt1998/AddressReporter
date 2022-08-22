package com.founder.addressreporter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author 姜涛
 * @create 2022-08-17 10:45
 */
@Configuration
@EnableWebSocket
public class ServerConfig {
    @Bean
    public ServerEndpointExporter serverEndpoint(){
        return new ServerEndpointExporter();

    }
}
