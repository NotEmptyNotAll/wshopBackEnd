package com.example.demo;

import com.example.demo.Service.ConnectorService;
import itd.dt.dtPackageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import wshop.Application;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Properties;

@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) {

      //  Properties properties = new Properties();

        //properties.put("jboss.naming.client.ejb.context", true);
//		connectorService.connect();
//        try {
//            Context context = new InitialContext(properties);
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }

        try {
            // Настройка подключения к серверу
            String providerHostName = "cloud.bfti-europe.lt"; // Можно указывать IP адрес сервера или имя хоста в сети
            int providerPort = 4447;

            dtPackageManager.setConnector(providerHostName, providerPort, wshop.Application.getRemoteConnector());
            Application.init("cloud", true);

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        SpringApplication application = new SpringApplication(DemoApplication.class);
        application.setAdditionalProfiles("ssl");
        application.run(args);
    }

}
