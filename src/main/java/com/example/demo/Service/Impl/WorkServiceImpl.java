package com.example.demo.Service.Impl;

import com.example.demo.DTO.CellData;
import com.example.demo.DTO.OrdersTableColumn;
import com.example.demo.DTO.OrdersTableRow;
import com.example.demo.Service.UserService;
import com.example.demo.Service.WorkService;
import com.example.demo.Utility.StringDataUtility;
import com.example.demo.payload.Request.ClientOrdersRequest;
import com.example.demo.payload.Request.DataIdRequest;
import com.example.demo.payload.Request.SubStringSearchRequest;
import com.example.demo.payload.Response.OrdersTableResponse;
import com.example.demo.payload.Response.SimpleDataResponse;
import com.ibm.icu.util.Calendar;
import itd.dt.dtException;
import itd.dt.requests.*;
import itd.dt.security.dtAccessControlException;
import itd.th.locate.thLocateFilter;
import itd.th.locate.thLocateOptionsSet;
import itd.th.thAgent;
import itd.th.thLocateProcessor;
import itd.th.vo.ThClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wshop.ejb3.entity.WOrderJobEntity;
import wshop.ejb3.entity.WorkOrderEntity;
import wshop.thesauri.automarks.thAutomarksAgent;
import wshop.thesauri.details.thDetailsAgent;
import wshop.thesauri.jobs.thJobsAgent;
import wshop.thesauri.jobs.thWOrderJobsAgent;
import wshop.thesauri.worders.thWOrdersAgent;
import wshop.thesauri.workitems.thWorkItemsAgent;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class WorkServiceImpl implements WorkService {
//    @Autowired
//    ConnectorService connectorService;

    @Autowired
    private StringDataUtility stringDataUtility;

    @Autowired
    private UserService userService;

    private boolean userAlreadyLogin;
    private boolean workStarted;
    private boolean searchWorkIdMode;
    private boolean workFind;
    private Integer searchWorkId;


    @Value("${column.status.name}")
    private String statusColumnName;

    @Value("${column.date.name}")
    private String dateColumnName;

    @Value("${column.customer.name}")
    private String customerColumnName;

    private static OrdersTableResponse ordersTableResponse;

    public WorkServiceImpl() {
        this.userAlreadyLogin = false;
        this.workStarted = false;
        this.workFind = false;
    }

    @Override
    public List<SimpleDataResponse> getJobs(SubStringSearchRequest subStringSearchRequest) {
        thAgent agent = new thJobsAgent();
        // Печать содержимого справочника деталей
        List<SimpleDataResponse> simpleDataResponse = null;
        try {
            simpleDataResponse = getProcessDetails(agent, subStringSearchRequest.getName(), subStringSearchRequest.getSizeResponse());
        } catch (dtException e) {
            e.printStackTrace();
        }
        return simpleDataResponse;
    }

    @Override
    public List<SimpleDataResponse> getWorkItems(SubStringSearchRequest subStringSearchRequest) {
        thAgent agent = new thWorkItemsAgent();
        // Печать содержимого справочника деталей
        List<SimpleDataResponse> simpleDataResponse = null;
        try {
            simpleDataResponse = getProcessDetails(agent, subStringSearchRequest.getName(), subStringSearchRequest.getSizeResponse());

        } catch (dtException e) {
            e.printStackTrace();
        }
        return simpleDataResponse;
    }

    @Override
    public List<SimpleDataResponse> getDetails(SubStringSearchRequest subStringSearchRequest) {

        thAgent agent = new thDetailsAgent();
        // Печать содержимого справочника деталей
        List<SimpleDataResponse> simpleDataResponse = null;
        try {
            simpleDataResponse = getProcessDetails(agent, subStringSearchRequest.getName(), subStringSearchRequest.getSizeResponse());
        } catch (dtException e) {
            e.printStackTrace();
        }
        return simpleDataResponse;
    }


    private List<SimpleDataResponse> getProcessDetails(thAgent agent, String substr, Integer maxSize) throws dtException {

        // Получение полного неотфильтрованного списка заказов
        dtVOArray records = agent.locateThesauriAllParameters();

        List<SimpleDataResponse> simpleDataResponse = new ArrayList<>();
        // Печать данных
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("| Id       | Alias                      | Name                                             |");
        System.out.println("--------------------------------------------------------------------------------------------");
        int kol = 0;
        for (dtVO detailVO : records.getArray()) {

            // Id записи
            Object id = detailVO.getProp(ThClass.FLD_ID);
            // Краткое наименование записи
            Object alias = detailVO.getProp(ThClass.FLD_ALIAS);
            // Полное наименование записи
            Object name = detailVO.getProp(ThClass.FLD_NAME);
            // Печать полученной информации

            if (kol >= maxSize) {
                break;
            } else if (alias != null && alias.toString().toLowerCase(Locale.ROOT).contains(substr.toLowerCase(Locale.ROOT))) {
                simpleDataResponse.add(new SimpleDataResponse(alias.toString(), Integer.parseInt(id.toString())));
                System.out.println(String.format("|%10s|%28s|%50s|", id, alias, name));
                kol++;
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        return simpleDataResponse;
    }


    @Override
    public OrdersTableResponse getListOfWorks(ClientOrdersRequest clientOrdersRequest) {
//        if (!connectorService.connect()) {
//            return null;
//        }
        if (!this.userAlreadyLogin) {
            if (!userService.login(clientOrdersRequest.getUser())) {
                return new OrdersTableResponse(-1);
            }
        }
        Locale locale = new Locale(clientOrdersRequest.getLang());
        Locale.setDefault(locale);
        JComponent.setDefaultLocale(locale);
        ordersTableResponse = new OrdersTableResponse();

        thWOrderJobsAgent agent = new thWOrderJobsAgent();

        // Создаем обработчик запроса. В оригинальном приложении запрос инициируется из SWING окна, созданного выше
        // (thWOrdersWestFilter westFilter), это же окно и является обработчиком запроса. Здесь же у нас фиктивное
        // окно отдельно, а обработчик отдельно, поэтому код выглядит немного странно. Со временем мы это приведем
        // в полную гармонию.
        thLocateProcessor locateProcessor = new thLocateProcessor() {

            @Override
            public void processFindedData(Container pContainer, thAgent pAgent, thLocateOptionsSet pFilter, dtResultSet pResultSet, dtRequestTally pRequestTally) {

                // Т.к. на данный момент программа не имеет готовой таблицы для отображения конкретно этого запроса,
                // извлекаем данные из контейнера и отображаем их самостоятельно
                //
                // Извлекаем из полученных с сервера данных основную таблицу. Т.к. запрос выполнялся
                // через thWOrderJobsAgent, то основной таблицей будет список заказанных работ.
                dtGeneralResult result = pResultSet.get(pRequestTally);

                // Проверяем что результат не является исключением
                if (result instanceof dtExceptionResult) {
                    Logger.getAnonymousLogger().log(Level.WARNING, "Can't process password", ((dtExceptionResult) result).getException());
                } else if (result instanceof dtVOArray) {


                    List<OrdersTableRow> tableBody = new ArrayList<>();
                    List<OrdersTableColumn> ordersTableColumns = new ArrayList<>();
                    List<CellData> cellDataList = null;
                    ordersTableColumns.add(new OrdersTableColumn("номер заказа", 100));
                    ordersTableColumns.add(new OrdersTableColumn("ID работы", 100));
                    ordersTableColumns.add(new OrdersTableColumn("работа", 100));
                    ordersTableColumns.add(new OrdersTableColumn("кол-во", 100));
                    ordersTableColumns.add(new OrdersTableColumn("статус", 100));
                    ordersTableColumns.add(new OrdersTableColumn("исполнители", 100));
                    ordersTableColumns.add(new OrdersTableColumn("выполн.до", 100));
                    ordersTableColumns.add(new OrdersTableColumn("деталь", 100));
                    ordersTableColumns.add(new OrdersTableColumn("автомобиль", 100));
                    int kol = 0;
                    // Т.к. мы запрашивали список, то результат должен быть массивом
                    for (dtVO worderJobVo : ((dtVOArray) result).getArray()) {
//                        if (kol > 150) {
//                            break;
//                        } else {
//                            kol++;
//                        }
                        try {

                            // Номер заказа
                            Object worderId = worderJobVo.getProp(WOrderJobEntity.FLD_WORDER_ID);
                            System.out.println("///////////////////////////////////////FLD_FINISHED " + worderJobVo.getProp(WOrderJobEntity.FLD_FINISHED));
                            // ID заказанной работы
                            Object worderJobId = worderJobVo.getProp(ThClass.FLD_ID);
                            // Краткое наименование работы
                            Object jobAlias = worderJobVo.getProp(ThClass.FLD_ALIAS);
                            // Количество работ
                            Object quantity = worderJobVo.getProp(WOrderJobEntity.FLD_QUANTITY);
                            // Статус работы (0: не выполнена, 1: выполнена, 2: выполняется, 3: пауза)
                            Object status = worderJobVo.getProp(WOrderJobEntity.FLD_RATIO);
                            // Список исполнителей
                            Object executors = worderJobVo.getProp(ThClass.FLD_NAME);
                            // Запланированная дата выполнения заказа
                            Object doBeforeDate = null;
                            // Краткое наименование детали
                            Object detailAlias = null;
                            // Полное наименование детали
                            Object detailName = null;
                            // Марка и модель автомобиля
                            Object vehicleName = null;

                            // Извлекаем из полученных с сервера данных запись с информацией о нужном нам заказе
                            thWOrdersAgent wOrdersAgent = new thWOrdersAgent();
                            if (worderId != null) {
                                dtVO worderVo = pResultSet.get(worderId, wOrdersAgent.getVOClass());

                                if (worderVo != null) {

                                    // Запланированная дата выполнения заказа
                                    doBeforeDate = worderVo.getProp(WorkOrderEntity.FLD_DO_BEFORE_DATE);

                                    // ID детали
                                    Object detailId = worderVo.getProp(WorkOrderEntity.FLD_DETAIL_ID);
                                    if (detailId != null) {

                                        // Извлекаем из полученных с сервера данных запись с информацией о нужной нам детали
                                        thDetailsAgent detailsAgent = new thDetailsAgent();
                                        dtVO detailVo = pResultSet.get(detailId, detailsAgent.getVOClass());

                                        if (detailVo != null) {

                                            // Краткое наименование детали
                                            detailAlias = detailVo.getProp(ThClass.FLD_ALIAS);
                                            // Полное наименование детали
                                            detailName = detailVo.getProp(ThClass.FLD_NAME);
                                        }
                                    }
                                    // ID марки автомобиля
                                    Object automarkId = worderVo.getProp(WorkOrderEntity.FLD_AUTO_MARK_ID);

                                    if (automarkId != null) {

                                        // Извлекаем из полученных с сервера данных запись с информацией о нужной нам модели автомобиля
                                        thAutomarksAgent automarkAgent = new thAutomarksAgent();
                                        dtVO automarkVo = pResultSet.get(automarkId, automarkAgent.getVOClass());

                                        if (automarkVo != null) {

                                            // Марка автомобиля
                                            Object automarkName = automarkVo.getProp(ThClass.FLD_NAME);
                                            // Модель автомобиля
                                            Object automodelName = worderVo.getProp(WorkOrderEntity.FLD_AUTO_MODEL);

                                            // Марка и модель автомобиля
                                            vehicleName = automarkName;
                                            if (automodelName != null) {
                                                vehicleName = vehicleName.toString() + " " + automodelName;
                                            }
                                        }
                                    }
                                    System.out.println(worderJobId + ", " + searchWorkId + ", " + searchWorkIdMode);

                                    // Печатаем полученные выше данные
                                    if (!searchWorkIdMode && (!workStarted || (executors != null && executors.toString().equals(clientOrdersRequest.getUser().getName())))) {
                                        cellDataList = new ArrayList<>();
                                        System.out.print(doBeforeDate + ", ");
                                        System.out.print(worderId + ", ");
                                        System.out.print(vehicleName + ", ");
                                        System.out.print(detailAlias + ", ");
                                        System.out.print(jobAlias + ", ");
                                        System.out.print(quantity + ", ");
                                        System.out.print(status + ", ");
                                        System.out.println(executors);
                                        cellDataList.add(new CellData("номер заказа", worderId != null ? worderId.toString() : ""));
                                        cellDataList.add(new CellData("ID работы", worderJobId != null ? worderJobId.toString() : ""));
                                        cellDataList.add(new CellData("работа", jobAlias != null ? jobAlias.toString() : ""));
                                        cellDataList.add(new CellData("кол-во", quantity != null ? quantity.toString() : ""));
                                        cellDataList.add(new CellData("статус", stringDataUtility.convertStatusToInt(status.toString())));
                                        cellDataList.add(new CellData("исполнители", executors != null ? executors.toString() : ""));
                                        cellDataList.add(new CellData("выполн.до", doBeforeDate != null ? doBeforeDate.toString() : ""));
                                        cellDataList.add(new CellData("деталь", detailAlias != null ? detailAlias.toString() : ""));
                                        cellDataList.add(new CellData("автомобиль", vehicleName != null ? vehicleName.toString() : ""));
                                        tableBody.add(new OrdersTableRow(cellDataList, ""));

                                    } else if (searchWorkIdMode && Integer.parseInt(worderJobId.toString()) == searchWorkId) {
                                        workFind = true;
                                        break;
                                    }


                                }
                            }

                        } catch (dtException e) {
                            Logger.getAnonymousLogger().log(Level.WARNING, "Can't process password", e);
                        }
                    }

                    userAlreadyLogin = false;
                    workStarted = false;
                    ordersTableResponse.setOrdersTableBody(tableBody);
                    ordersTableResponse.setColumnTables(ordersTableColumns);

                }

            }

            @Override
            public void locate(thLocateOptionsSet pFilter, Container pContainer) {
                // Этот метод нам здесь не нужен
            }
        };
        // Фильтр, указывающий что должны отбираться только те работы, которые выполнялись (или выполняются) текущим пользователем
        try {

            thLocateOptionsSet locateOptions = new thLocateOptionsSet();
            // Помещаем в контейнер опцию с указанием присоединить к результату запроса связанные данные из справочника заказов
            locateOptions.add(agent.getIncludeWorkOrderOption());
            // Помещаем в контейнер опцию с указанием присоединить к результату запроса связанные данные из справочника деталей
            locateOptions.add(agent.getIncludeDetailOption());
            // Помещаем в контейнер опцию с указанием присоединить к результату запроса связанные данные из справочника марок автомобилей
            locateOptions.add(agent.getIncludeVehicleOption());
            if ((this.workStarted || clientOrdersRequest.isOnlyUser()) && clientOrdersRequest.getWorkStatus() == 2) {
                thLocateOptionsSet currentUserFilter = agent.getCurrentUserFilter();
                locateOptions.add(currentUserFilter);
            }
            if (clientOrdersRequest.getWorkId() != null) {
                // Фильтр по ID работы
                thLocateFilter jobFilter = agent.getJobFilter(clientOrdersRequest.getWorkId());
                // Помещаем в контейнер фильтр по ID работы
                locateOptions.add(jobFilter);
            }
            thLocateOptionsSet worderStatusFilter = agent.getWorderStatusFilter(false);
            locateOptions.add(worderStatusFilter);

            if (clientOrdersRequest.getWorkStatus() != null) {

                // Фильтр по ID статуса работы
                thLocateOptionsSet statusFilter = agent.getStatusFilter(clientOrdersRequest.getWorkStatus());

                // Помещаем в контейнер фильтр по ID статуса работы
                locateOptions.add(statusFilter);
            }
            if (clientOrdersRequest.getDetailId() != null) {
                // Фильтр по ID детали
                thLocateOptionsSet detailFilter = agent.getDetailFilter(clientOrdersRequest.getDetailId());
                // Помещаем в контейнер фильтр по ID детали
                locateOptions.add(detailFilter);
            }

            if (clientOrdersRequest.getWorkDateTo() != null) {
                thLocateOptionsSet doBeforePeriodFilter;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateFrom = sdf.parse(clientOrdersRequest.getWorkDateFrom());
                    Date dateTo = sdf.parse(clientOrdersRequest.getWorkDateTo());
                    doBeforePeriodFilter = agent.getDoBeforePeriodFilter(dateFrom, dateTo);
                } catch (ParseException e) {
                    e.printStackTrace(System.err);
                    doBeforePeriodFilter = new thLocateOptionsSet();
                }
                locateOptions.add(doBeforePeriodFilter);
            }

//        thLocateOptionsSet doBeforePeriodFilter;
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
//            Date dateFrom = sdf.parse("01.12.2020");
//            Date dateTo = sdf.parse("30.12.2020");
//            doBeforePeriodFilter = agent.getDoBeforePeriodFilter(dateFrom, dateTo);
//        } catch (ParseException e) {
//            e.printStackTrace(System.err);
//            doBeforePeriodFilter = new thLocateOptionsSet();
//        }

            agent.locate(locateOptions, null, locateProcessor);
        } catch (dtAccessControlException e) {
            e.printStackTrace();
        }
        return ordersTableResponse;
    }

    private List<OrdersTableRow> deleteNotExistExecutorsRow(List<OrdersTableRow> temp, List<Integer> indexArr) {
        List<OrdersTableRow> tableBody = new ArrayList<>();
        for (Integer index :
                indexArr) {
            tableBody.add(temp.get(index));
        }
        return tableBody;
    }

    @Override
    public boolean workHaveUserById(DataIdRequest dataIdRequest) {
        this.workFind = false;
        this.searchWorkIdMode = true;
        this.searchWorkId = dataIdRequest.getId();
        this.getListOfWorks(new ClientOrdersRequest(dataIdRequest.getUser()));
        this.searchWorkIdMode = false;
        this.searchWorkId = null;
        return this.workFind;
    }

    @Override
    public OrdersTableResponse startWork(DataIdRequest dataIdRequest) throws dtException {
        if (userService.login(dataIdRequest.getUser())) {
            this.userAlreadyLogin = true;
            thWOrderJobsAgent.startNextStageOfTheJob(dataIdRequest.getId(),
                    stringDataUtility.dateOf(dataIdRequest.getDate()));
            return new OrdersTableResponse();
        } else {
            return null;
        }
    }

    @Override
    public OrdersTableResponse endWork(DataIdRequest dataIdRequest) throws dtException {
        if (userService.login(dataIdRequest.getUser())) {
            this.userAlreadyLogin = true;
            this.workStarted = true;
            thWOrderJobsAgent.finishTheJob(dataIdRequest.getId(),
                    stringDataUtility.dateOf(dataIdRequest.getDate()));
            return getListOfWorks(new ClientOrdersRequest(dataIdRequest.getUser()));
        } else {
            return null;
        }
    }


}
