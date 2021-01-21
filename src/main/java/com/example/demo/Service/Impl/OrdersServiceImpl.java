package com.example.demo.Service.Impl;

import com.example.demo.DTO.CellData;
import com.example.demo.DTO.OrdersTableColumn;
import com.example.demo.DTO.OrdersTableRow;
import com.example.demo.DTO.User;
import com.example.demo.Service.UserService;
import com.example.demo.Utility.StringDataUtility;
import com.example.demo.payload.Request.ClientOrdersRequest;
import com.example.demo.payload.Request.SubStringSearchRequest;
import com.example.demo.payload.Response.OrdersTableResponse;
import itd.dt.dtException;
import itd.dt.requests.*;

import itd.mq.sql.mqSQLBoolean;
import itd.resources.TextBundle;
import itd.resources.TextBundleFactory;
import itd.th.gui.thDatePeriod;
import itd.th.locate.bool.thBooleanEqual;
import itd.th.locate.thLocateOptionsSet;
import itd.th.thAgent;
import itd.th.thLocateProcessor;
import itd.th.thVOArrayColumnInfo;
import itd.th.thVOTableModel;

import itd.th.vo.ThClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import wshop.ejb3.entity.WorkOrderEntity;
import wshop.thesauri.customers.thCustomersAgent;
import wshop.thesauri.worders.*;


import com.example.demo.Service.OrdersService;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {

    //    @Autowired
//    ConnectorService connectorService;

    @Autowired
    StringDataUtility stringDataUtility;

    @Autowired
    UserService userService;

    @Value("${column.status.name}")
    private String statusColumnName;

    @Value("${column.date.name}")
    private String dateColumnName;

    @Value("${column.customer.name}")
    private String customerColumnName;

    private static OrdersTableResponse ordersTableResponse;


    @Override
    public OrdersTableResponse getCroppedOrderList(ClientOrdersRequest clientOrdersRequest) throws dtException {

//        if (!connectorService.connect()) {
//            return null;
//        }

        if (!userService.login(clientOrdersRequest.getUser())) {
            System.out.println("Login exception");
            return new OrdersTableResponse(-1);
        }
        System.out.println("Login success");

        Locale locale = new Locale("ru");
        Locale.setDefault(locale);
        JComponent.setDefaultLocale(locale);
        this.ordersTableResponse = new OrdersTableResponse();

        // Создаем экземпляр класса, обслуживающего "Справочник заказов". Он содержит необходимую информацию о
        // таблице worders и ее полях, а также о EJB классах, необходимых для манипуляции с данными.
        thAgent agent = new thWOrdersAgent();

        // Создаем окно на SWING с полями фильтрации данных. Понятно что здесь нам SWING ни к чему, позже мы займемся
        // переделкой этого окна под WEB. Но пока приходится создавать этот объект для совместимости.
        thWOrdersWestFilter westFilter = new thWOrdersWestFilter();
        westFilter.setThAgent(agent);

        // Создаем обработчик запроса. В оригинальном приложении запрос инициируется из SWING окна, созданного выше
        // (thWOrdersWestFilter westFilter), это же окно и является обработчиком запроса. Здесь же у нас фиктивное
        // окно отдельно, а обработчик отдельно, поэтому код выглядит немного странно. Со временем мы это приведем
        // в полную гармонию.
        thLocateProcessor locateProcessor = new thLocateProcessor() {


            @Override
            public void processFindedData(Container pContainer,
                                          thAgent pAgent,
                                          thLocateOptionsSet pFilter,
                                          dtResultSet pResultSet, dtRequestTally pRequestTally) {
                // Вызываем метод, который обработает полученные с сервера данные
                printWordersTable(agent, pResultSet, pRequestTally, ordersTableResponse, clientOrdersRequest, clientOrdersRequest.getLang());
            }


            @Override
            public void locate(thLocateOptionsSet pFilter, Container pContainer) {
                // Этот метод нам здесь не нужен
            }
        };

        thLocateOptionsSet combinedFilter = this.setFilter(clientOrdersRequest, agent);
        agent.locate(combinedFilter, null, locateProcessor);
        System.out.println(this.customerColumnName);
        System.out.println(this.dateColumnName);
        System.out.println(this.statusColumnName);
//        if (!clientOrdersRequest.filterDataIsEmpty()) {
//            ordersTableResponse.setOrdersTableBody(
//                    this.filterListOrders(ordersTableResponse.getOrdersTableBody(),
//                            ordersTableResponse.getColumnTables(),
//                            clientOrdersRequest));
//        }
        return ordersTableResponse;
    }

    @Override
    public List<User> getCustomers(SubStringSearchRequest subStringSearchRequest) {
        List<User> userList = new ArrayList<>();
//        if (!connectorService.connect()) {
//            return null;
//        }
        try {
            thAgent agent = new thCustomersAgent();
            //Нам нужно всего лишь несколько колонок, поэтому запрашиваем в БД только эти колонки
            ArrayList<dtVOProp> properties = new ArrayList<>();
            properties.add(ThClass.FLD_ID);
            properties.add(ThClass.FLD_ALIAS);
            properties.add(ThClass.FLD_NAME);

            dtVOArray customers = agent.locateThesauri(properties);

            // Печать данных
            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println("|Customer id  | Customer alias               |Customer name                                |");
            System.out.println("--------------------------------------------------------------------------------------------");
            // Отображает только первые 50 клиентов
            int sizeResp = 0;
            for (Iterator<dtVO> it = customers.iterator(); it.hasNext(); ) {
                dtVO customer = it.next();
                if (sizeResp <= subStringSearchRequest.getSizeResponse()) {
                    Object customerAlias = customer.getProp(ThClass.FLD_NAME);
                    if (customerAlias.toString().toLowerCase(Locale.ROOT).contains(subStringSearchRequest.getName().toLowerCase(Locale.ROOT))) {
                        Object customerId = null;
                        customerId = customer.getProp(ThClass.FLD_ID);
                        userList.add(new User(Integer.parseInt(customerId.toString()), customerAlias.toString()));
                        System.out.println(
                                String.format("|%13s|%30s|",
                                        customerId.toString(),
                                        customerAlias.toString()
                                )
                        );
                        sizeResp++;
                    }
                } else {
                    break;
                }

            }
//            for (int i = 0; i < customers.getArray().size(); i++) {
//
//                dtVO orderVO = customers.get(i);
//
//                // Id заказчика
//                Object customerId = null;
//
//                customerId = orderVO.getProp(ThClass.FLD_ID);
//
//                // Краткое имя заказчика
//                Object customerAlias = orderVO.getProp(ThClass.FLD_NAME);
//                // Полное имя заказчика
//                Object customrName = orderVO.getProp(ThClass.FLD_NAME);
//
//                System.out.println(
//                        String.format("|%13s|%30s|%45s|",
//                                customerId.toString(),
//                                customerAlias.toString(),
//                                customrName.toString()
//                        )
//                );
//                userList.add(new User(Integer.parseInt(customerId.toString()), customerAlias.toString()));
//            }

        } catch (dtException e) {
            e.printStackTrace();
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        return userList;
    }


    private static void printWordersTable(thAgent agent, dtResultSet resultSet,
                                          dtRequestTally requestTally, OrdersTableResponse ordersTableResponse, ClientOrdersRequest clientOrdersRequest, String lang) {
        Locale locale = new Locale("ru");
        Locale.setDefault(locale);
        JComponent.setDefaultLocale(locale);
        List<OrdersTableRow> tableBody = new ArrayList<>();
        List<OrdersTableColumn> ordersTableColumns = new ArrayList<>();

        // Регистрируем ресурс с текстами
        TextBundleFactory.setDefaultResourceName("wshop-texts.properties");
        TextBundle textBundle = TextBundleFactory.getDefaultTextBundle();

        // Получаем данные в виде javax.swing.table.TableModel
        thVOTableModel tableModel = agent.getVOTableModel(resultSet, requestTally, true);
        // Получаем список сырых данных (сущностей заказов), на основе которых была построена таблица. Каждая сущность
        // соответствует одной строке в таблице заказов в базе данных, но при этом она содержит не все данные этой строки,
        // а только те, которые нужны для отображения в таблице. Для того, чтобы получить отсутствующие данные, необходмо
        // сделать дополнительный запрос к базе данных.
        dtVOArray worders = tableModel.getVOArray();
        // Получаем информацию о колонках
        TableColumnModel tableColumnModel = tableModel.getTableColumnModel();

        // Получаем количество колонок
        int columnCount = tableModel.getColumnCount();

        // Печатаем заголовок




        for (int columnNo = 0; columnNo < columnCount; columnNo++) {
            System.out.print("|");
            // Информация о колонке (см. javax.swing.table.TableColumn)
            thVOArrayColumnInfo column = (thVOArrayColumnInfo) tableColumnModel.getColumn(columnNo);
            // Заголовок колонки
            String name = textBundle.getString(column.getColumnHeaderId());
            // Ширина колонки
            int width = column.getPreferredWidth();
            ordersTableColumns.add(new OrdersTableColumn(name, width));
            System.out.print(name);
        }
        System.out.println("|");

        // Печатаем срдержимое таблицы
        List<CellData> cellDataList = null;
        int rowCount = tableModel.getRowCount();

        for (int rowNo = 0; rowNo < rowCount; rowNo++) {
            boolean isValidBySunString = true;
            if (clientOrdersRequest.getSearchString() != null) {
                isValidBySunString = false;
            }

            cellDataList = new ArrayList<>();
            for (int columnNo = 0; columnNo < columnCount; columnNo++) {

                System.out.print("|");

                // Информация о колонке (см. javax.swing.table.TableColumn)
                thVOArrayColumnInfo column = (thVOArrayColumnInfo) tableColumnModel.getColumn(columnNo);
                // Ширина колонки
                int width = column.getPreferredWidth();
                // Значение ячейки
                Object cellValue = tableModel.getValueAt(rowNo, columnNo);
                // Значение ячейки может быть равно null, это нормально
                if (cellValue == null) {
                    cellValue = "";
                }

                try {
                    if (isValidBySunString) {
                        cellDataList.add(new CellData(column.getHeaderValue().toString(), cellValue.toString()));
                    } else if (cellValue.toString().toLowerCase()
                            .contains(clientOrdersRequest.getSearchString().toLowerCase())) {
                        isValidBySunString = true;
                        cellDataList.add(new CellData(column.getHeaderValue().toString(), cellValue.toString()));
                    } else {
                        cellDataList.add(new CellData(column.getHeaderValue().toString(), cellValue.toString()));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    cellDataList.add(new CellData(column.getHeaderValue().toString(), cellValue.toString()));
                }


                System.out.print(cellValue.toString());
            }

            if (isValidBySunString) {
                String tempCom = "";
                try {
//                    // Печатаем комментарий
//                    Object comment = worders.get(rowNo).getProp(WorkOrderEntity.FLD_COMMENT);;
//                    System.out.println("| Comment: " + (comment == null ? "" : comment.toString()));
//                    tempCom = (comment == null ? "" : comment.toString());
                    // Получаем идентификатор заказа
                    Object id = worders.get(rowNo).getProp(ThClass.FLD_ID);
                    // Считываем заказ из базы данных
                    agent.initByKey(id);
                    // Получаем комментарий из считанной сущности
                    Object comment = agent.getVOProperty(WorkOrderEntity.FLD_COMMENT);
                    // Печатаем комментарий
                    System.out.println("| Comment: " + (comment == null ? "" : comment));
                    tempCom = (comment == null ? "" : comment.toString());
                } catch (dtException e) {
                }
                cellDataList.add(new CellData("Comment", tempCom));

                tableBody.add(new OrdersTableRow(cellDataList, tempCom));
            }
            System.out.println("|");
        }

        ordersTableResponse.setOrdersTableBody(tableBody);
        ordersTableResponse.setColumnTables(ordersTableColumns);
        ordersTableColumns.toString();

    }


    private static void printListWorkTable(thAgent agent, dtResultSet resultSet,
                                           dtRequestTally requestTally, OrdersTableResponse ordersTableResponse, ClientOrdersRequest clientOrdersRequest, String lang) {
        Locale locale = new Locale("ru");
        Locale.setDefault(locale);
        JComponent.setDefaultLocale(locale);
        List<OrdersTableRow> tableBody = new ArrayList<>();
        List<OrdersTableColumn> ordersTableColumns = new ArrayList<>();

        // Регистрируем ресурс с текстами
        TextBundleFactory.setDefaultResourceName("wshop-texts.properties");
        TextBundle textBundle = TextBundleFactory.getDefaultTextBundle();

        // Получаем данные в виде javax.swing.table.TableModel
        thVOTableModel tableModel = agent.getVOTableModel(resultSet, requestTally, true);
        // Получаем список сырых данных (сущностей заказов), на основе которых была построена таблица. Каждая сущность
        // соответствует одной строке в таблице заказов в базе данных, но при этом она содержит не все данные этой строки,
        // а только те, которые нужны для отображения в таблице. Для того, чтобы получить отсутствующие данные, необходмо
        // сделать дополнительный запрос к базе данных.
        dtVOArray worders = tableModel.getVOArray();
        // Получаем информацию о колонках
        TableColumnModel tableColumnModel = tableModel.getTableColumnModel();

        // Получаем количество колонок
        int columnCount = tableModel.getColumnCount();
        System.out.println("//////////");
        // Печатаем заголовок
        System.out.println();
        for (int columnNo = 0; columnNo < columnCount; columnNo++) {
            System.out.print("|");
            // Информация о колонке (см. javax.swing.table.TableColumn)
            thVOArrayColumnInfo column = (thVOArrayColumnInfo) tableColumnModel.getColumn(columnNo);
            // Заголовок колонки
            String name = textBundle.getString(column.getColumnHeaderId());
            // Ширина колонки
            int width = column.getPreferredWidth();
            System.out.println(name);
            if (name.equals("Код") || name.equals("Автомобіль") || name.equals("Назва деталі") ||
                    name.equals("Номер двигуна") || name.equals("Співробітник") || name.equals("Викон.") ||
                    name.equals("Закр.") || name.equals("Викон. до")) {
                ordersTableColumns.add(new OrdersTableColumn(name, width));
            }

            System.out.print(name);
        }
        System.out.println("|");

        // Печатаем срдержимое таблицы
        List<CellData> cellDataList = null;
        int rowCount = tableModel.getRowCount();

        for (int rowNo = 0; rowNo < rowCount; rowNo++) {
            boolean isValidBySunString = true;
            if (clientOrdersRequest.getSearchString() != null) {
                System.out.println(clientOrdersRequest.getSearchString());
                isValidBySunString = false;
            }

            cellDataList = new ArrayList<>();
            for (int columnNo = 0; columnNo < columnCount; columnNo++) {

                System.out.print("|");

                // Информация о колонке (см. javax.swing.table.TableColumn)
                thVOArrayColumnInfo column = (thVOArrayColumnInfo) tableColumnModel.getColumn(columnNo);
                // Ширина колонки
                int width = column.getPreferredWidth();
                // Значение ячейки
                Object cellValue = tableModel.getValueAt(rowNo, columnNo);
                // Значение ячейки может быть равно null, это нормально
                if (cellValue == null) {
                    cellValue = "";
                }

                try {
                    if (isValidBySunString) {
                        cellDataList.add(new CellData(column.getHeaderValue().toString(), cellValue.toString()));
                    } else if (cellValue.toString().toLowerCase()
                            .contains(clientOrdersRequest.getSearchString().toLowerCase())) {
                        isValidBySunString = true;
                        cellDataList.add(new CellData(column.getHeaderValue().toString(), cellValue.toString()));
                    } else {
                        cellDataList.add(new CellData(column.getHeaderValue().toString(), cellValue.toString()));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    cellDataList.add(new CellData(column.getHeaderValue().toString(), cellValue.toString()));
                }


                System.out.print(cellValue.toString());
            }

            if (isValidBySunString) {
                String tempCom = "";
                try {

                    // Печатаем комментарий
                    Object comment = worders.get(rowNo).getProp(WorkOrderEntity.FLD_COMMENT);;
                    System.out.println("| Comment: " + (comment == null ? "" : comment));
                    tempCom = (comment == null ? "" : comment.toString());
                } catch (dtException e) {
                }
                cellDataList.add(new CellData("Comment", tempCom));

                tableBody.add(new OrdersTableRow(cellDataList, tempCom));
            }
            System.out.println("|");
        }

        ordersTableResponse.setOrdersTableBody(tableBody);
        ordersTableResponse.setColumnTables(ordersTableColumns);
        ordersTableColumns.toString();

    }

    private List<OrdersTableRow> filterListOrders(List<OrdersTableRow> ordersTableRows,
                                                  List<OrdersTableColumn> ordersTableColumn,
                                                  ClientOrdersRequest clientOrdersRequest) {


        //фильтруем по сотруднику
        ordersTableRows = this.filterByParam(ordersTableRows,
                ordersTableColumn,
                this.customerColumnName,
                clientOrdersRequest.getSearchString());

        //фильтруем по статусу
        ordersTableRows = this.filterByParam(ordersTableRows,
                ordersTableColumn,
                this.statusColumnName,
                clientOrdersRequest.getStatus() ? "thWOrders.orderClosed.yes" : "thWOrders.orderClosed.no");


        return ordersTableRows;
    }

    private thLocateOptionsSet setFilter(ClientOrdersRequest clientOrdersRequest, thAgent agent) {
        thLocateOptionsSet combinedFilter = new thLocateOptionsSet();

        //==========================================================
        // ФИЛЬТРАЦИЯ ДАННЫХ
        //
        //----------------------------------------------------------
        // ФИЛЬТР ПО ДАТЕ
        //
        // Фильтрация возможна по разным датам, при этом нужно указывать соответствующие поля класса WorkOrderEntity:
        //        // - для фильтрации по дате открытия заказа поле WorkOrderEntity.FLD_OPEN_DATE
        //        // - для фильтрации по дате закрытия заказа поле WorkOrderEntity.FLD_ACT_DATE
        //        //----------------------------------------------------------
        if (clientOrdersRequest.getDateFrom() != null && clientOrdersRequest.getDateFrom() != null) {
            Date fromDate = stringDataUtility.dateOf(clientOrdersRequest.getDateFrom());
            Date toDate = stringDataUtility.dateOf(clientOrdersRequest.getDateTo());
            System.out.println(clientOrdersRequest.getCloseDate());

            if (!clientOrdersRequest.getCloseDate()) {
                thLocateOptionsSet dateFilter = thDatePeriod
                        .getFilter(agent, fromDate, toDate, WorkOrderEntity.FLD_OPEN_DATE.getAlias());
                combinedFilter.add(dateFilter);

            } else {
                thLocateOptionsSet dateFilter = thDatePeriod
                        .getFilter(agent, fromDate, toDate, WorkOrderEntity.FLD_ACT_DATE.getAlias());
                combinedFilter.add(dateFilter);
                clientOrdersRequest.setState("CLOSED");
            }
        }


        // ФИЛЬТР ПО СОСТОЯНИЮ ОПЛАТЫ (ОПЛАЧЕН/НЕОПЛАЧЕН)
        //
        if (clientOrdersRequest.getPayed() != null) {
            boolean temp = clientOrdersRequest.getPayed();
            thLocateOptionsSet payedFilter = thWOrdersPayedField.getFilter(temp);
            combinedFilter.add(payedFilter);
        }

        //----------------------------------------------------------
        // ФИЛЬТР ПО СОСТОЯНИЮ ЗАКАЗА (ОТКРЫТ/ВЫПОЛНЕН/ЗАКРЫТ)
        //
        if (clientOrdersRequest.getState() != null) {
            thWOrdersStateField.State state = thWOrdersStateField.State.valueOf(clientOrdersRequest.getState()); // Другие варианты: FINISHED, UNCLOSED, CLOSED
            thLocateOptionsSet stateFilter = thWOrdersStateField.getFilter(state);
            combinedFilter.add(stateFilter);

        }


        //----------------------------------------------------------
        // ФИЛЬТР ПО ЗАКАЗЧИКУ
        //
        if (clientOrdersRequest.getCustomerId() != null) {
            thLocateOptionsSet customerFilter = thWOrdersCustomerField.getFilter(clientOrdersRequest.getCustomerId());
            combinedFilter.add(customerFilter);
        }

        //----------------------------------------------------------
        // ФИЛЬТР ПО СОТРУДНИКУ, ОТКРЫВШЕМУ ЗАКАЗ
        //

        if (clientOrdersRequest.getEmployeeId() != null) {
            mqSQLBoolean emplyeePredicate = new thBooleanEqual(agent,
                    WorkOrderEntity.FLD_OPEN_EMPLOYEE_ID.getAlias(),
                    clientOrdersRequest.getEmployeeId().toString());
            combinedFilter.add(emplyeePredicate);
        }

        return combinedFilter;
    }

    private List<OrdersTableRow> filterByParam(List<OrdersTableRow> ordersTableRows,
                                               List<OrdersTableColumn> ordersTableColumn,
                                               String paramByFilter,
                                               String value) {
        Integer numberColumn = ordersTableColumn
                .indexOf(ordersTableColumn.stream().filter(elem -> {
                    return elem.getNameColumn().equals(paramByFilter);
                }).findFirst().get());
        return ordersTableRows.stream().filter(elem -> {
            return elem.getRowData().get(numberColumn).getCellData().contains(value);
        }).collect(Collectors.toList());
    }


}
