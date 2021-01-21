package com.example.demo.Service;

import com.example.demo.payload.Request.ClientOrdersRequest;
import com.example.demo.payload.Request.DataIdRequest;
import com.example.demo.payload.Request.SubStringSearchRequest;
import com.example.demo.payload.Response.OrdersTableResponse;
import com.example.demo.payload.Response.SimpleDataResponse;
import itd.dt.dtException;

import java.util.List;

public interface WorkService {

    OrdersTableResponse getListOfWorks(ClientOrdersRequest clientOrdersRequest);

    OrdersTableResponse startWork(DataIdRequest dataIdRequest) throws dtException;

    OrdersTableResponse endWork(DataIdRequest dataIdRequest) throws dtException;


    List<SimpleDataResponse> getDetails(SubStringSearchRequest subStringSearchRequest);

    List<SimpleDataResponse> getJobs(SubStringSearchRequest subStringSearchRequest);

    boolean workHaveUserById(DataIdRequest dataIdRequest);

    List<SimpleDataResponse> getWorkItems(SubStringSearchRequest subStringSearchRequest);
}
