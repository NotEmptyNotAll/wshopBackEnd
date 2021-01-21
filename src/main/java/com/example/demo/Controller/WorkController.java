package com.example.demo.Controller;

import com.example.demo.Service.WorkService;
import com.example.demo.payload.Request.ClientOrdersRequest;
import com.example.demo.payload.Request.DataIdRequest;
import com.example.demo.payload.Request.SubStringSearchRequest;
import com.example.demo.payload.Response.OrdersTableResponse;
import com.example.demo.payload.Response.SimpleDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class WorkController {

    @Autowired
    private WorkService workService;

    @RequestMapping(value = "/getListOFWork", //
            method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public OrdersTableResponse getListOFWork(@RequestBody ClientOrdersRequest clientOrdersRequest) {

        try {
            return workService.getListOfWorks(clientOrdersRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new OrdersTableResponse(-1);
    }

    @RequestMapping(value = "/getListDetails", //
            method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public List<SimpleDataResponse> getListDetails(@RequestBody SubStringSearchRequest subStringSearchRequest) {

        try {
            return workService.getDetails(subStringSearchRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/startWork", //
            method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public OrdersTableResponse startWork(@RequestBody DataIdRequest dataIdRequest) {

        if (workService.workHaveUserById(dataIdRequest)) {
            return new OrdersTableResponse(-1);
        } else {
            try {
                return workService.startWork(dataIdRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @RequestMapping(value = "/endWork", //
            method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public OrdersTableResponse endWork(@RequestBody DataIdRequest dataIdRequest) {

        try {
            return workService.endWork(dataIdRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/getListJobs", //
            method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public List<SimpleDataResponse> getListJobs(@RequestBody SubStringSearchRequest subStringSearchRequest) {

        try {
            return workService.getJobs(subStringSearchRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @RequestMapping(value = "/getWorkItems", //
            method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public List<SimpleDataResponse> getWorkItems(@RequestBody SubStringSearchRequest subStringSearchRequest) {

        try {
            return workService.getWorkItems(subStringSearchRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
