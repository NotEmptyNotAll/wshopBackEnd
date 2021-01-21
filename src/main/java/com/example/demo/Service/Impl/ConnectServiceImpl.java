package com.example.demo.Service.Impl;

import com.example.demo.Service.ConnectorService;
import itd.dt.dtPackageManager;
import org.springframework.stereotype.Service;

@Service
public class ConnectServiceImpl implements ConnectorService
{
    @Override
    public boolean connect() {
        try {
            // Настройка подключения к серверу
            String providerHostName = "cloud.bfti-europe.lt"; // Можно указывать IP адрес сервера или имя носта в сети
            int providerPort = 4447;
            dtPackageManager.setConnector(providerHostName, providerPort, wshop.Application.getRemoteConnector());

        } catch (Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
        return true;
    }
}
