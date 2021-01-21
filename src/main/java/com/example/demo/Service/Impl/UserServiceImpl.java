package com.example.demo.Service.Impl;

import com.example.demo.DTO.User;
import com.example.demo.Service.ConnectorService;
import com.example.demo.Service.UserService;
import itd.dt.dtException;
import itd.dt.dtPropertyAccessException;
import itd.dt.requests.dtVO;
import itd.dt.requests.dtVOArray;
import itd.th.vo.ThClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wshop.Application;
import wshop.ejb3.entity.EmployeeEntity;
import wshop.forms.EmployeeEditor;
import wshop.thesauri.employees.thEmployeesAgent;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static wshop.forms.EmployeeEditor.DEFAULT_CHARSET;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ConnectorService connectorService;

    private thEmployeesAgent agent;

    @Override
    public List<User> getUser() {

//        if (!connectorService.connect()) {
//            return null;
//        }
        List<User> users = new ArrayList<>();


        // Получаем список сотрудников
        dtVOArray employees = null;
        try {
            employees = agent.locateThesauriAllParameters();
        } catch (dtException e) {
            e.printStackTrace();
        }

        // Печатаем список сотрудников
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("| Employee name                                                                            |");
        System.out.println("--------------------------------------------------------------------------------------------");
        if (employees != null && employees.getArray() != null) {
            try {
                for (dtVO employeeVO : employees.getArray()) {
                    // Полное имя сотрудника
                    Object employeeName = null;
                    employeeName = employeeVO.getProp(ThClass.FLD_ALIAS);
                    System.out.println("| " + employeeName + " | " + ((Number) employeeVO.getPrimaryKey()).intValue());
                    System.out.println(employeeName);
                    System.out.println("| " + employeeName + ", roleId=" + employeeVO.getProp(EmployeeEntity.FLD_ROLE_ID));

                    if (employeeName != null) {
                        users.add(new User(((Number) employeeVO.getPrimaryKey()).intValue(),
                                employeeName.toString(),
                                Integer.parseInt(employeeVO.getProp(EmployeeEntity.FLD_ROLE_ID).toString())));
                    }
                }
            } catch (dtPropertyAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        return users;
    }

    public UserServiceImpl() {

        // Получаем экземпляр класса, обслуживающего "Справочник сотрудников" (агента справочника)
        this.agent = new thEmployeesAgent();

        // Настраиваем агента на работу только с сотрудниками данного подразделения
        agent.setLocalOnly(true);
        // Настраиваем агента на работу только с сотрудниками, у которых заданы пароли
        agent.setWithPasswordOnly(true);
        // Получаем список сотрудников
    }

    @Override
    public boolean login(User user) {

        dtVOArray employees = null;
        try {
            employees = agent.locateThesauriAllParameters();
        } catch (dtException e) {
            e.printStackTrace();
        }

        if (employees == null) {
            return false;
        }

        // Печатаем список сотрудников
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("| Employee name                                                                            |");
        System.out.println("--------------------------------------------------------------------------------------------");
        dtVO<?, ?> adminVO = null;
        try {

            for (dtVO employeeVO : employees.getArray()) {

                // Полное имя сотрудника
                Object employeeName = employeeVO.getProp(ThClass.FLD_NAME);
                System.out.println("| " + employeeName + " | " + ((Number) employeeVO.getPrimaryKey()).intValue());
                // Ищем среди всех сотрудников администратора
                if (((Number) employeeVO.getPrimaryKey()).intValue() == user.getId()) {
                    System.out.println(((Number) employeeVO.getPrimaryKey()).intValue() + "    " + user.getId());
                }

                if (((Number) employeeVO.getPrimaryKey()).intValue() == user.getId()) {
                    adminVO = employeeVO;
                    System.out.println("---------------User login: " + user.getName());
                }
            }
        } catch (dtPropertyAccessException | NullPointerException e) {
            e.printStackTrace();
        }
        System.out.println("--------------------------------------------------------------------------------------------");

        // Если не нашли администратора, больше тут нечего делать
        if (adminVO == null) {
            return false;
        }
        // запрашиваем у пользователя пароль администратора
        String password = user.getPassword();

        try {
            // Проверяем пароль
            String storedSalt = adminVO.getProp(EmployeeEntity.FLD_SALT);
            String storedPassHash = adminVO.getProp(EmployeeEntity.FLD_PASSWORD);
            if (storedSalt != null && storedPassHash != null) {

                byte[] salt = DatatypeConverter.parseBase64Binary(storedSalt);
                byte[] passHash = DatatypeConverter.parseBase64Binary(storedPassHash);
                byte[] providedPassword = password.getBytes(DEFAULT_CHARSET);
                byte[] providedHash = EmployeeEditor.getHash(providedPassword, salt);

                if (Arrays.equals(passHash, providedHash)) {
                    // Если пароль подошел, регистрируем прользователя в системе и выходим
                    Application.setUser(adminVO);
                    return true;
                }
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | dtPropertyAccessException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Can't process password", e);
        }


        return false;
    }
}
