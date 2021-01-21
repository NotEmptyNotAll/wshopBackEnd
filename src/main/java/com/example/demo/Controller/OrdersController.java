package com.example.demo.Controller;

import com.example.demo.DTO.User;
import com.example.demo.Service.OrdersService;
import com.example.demo.payload.Request.ClientOrdersRequest;
import com.example.demo.payload.Request.SubStringSearchRequest;
import com.example.demo.payload.Response.OrdersTableResponse;
import itd.dt.dtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/")
    public String serviceIsAlive() {
        return "I'm alive";
    }


    @RequestMapping(value = "/getCroppedOrders", //
            method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public OrdersTableResponse getCroppedOrders(@RequestBody ClientOrdersRequest clientOrdersRequest) {

        try {
            return ordersService.getCroppedOrderList(clientOrdersRequest);
        } catch (dtException e) {
            System.out.println(e);
        }
        return new OrdersTableResponse(-1);

    }





    @GetMapping("/test")
    public OrdersTableResponse testOrders() {
        ClientOrdersRequest clientOrdersRequest = new ClientOrdersRequest();
        User user = new User(100, "user");
        user.setPassword("quadcore");

        clientOrdersRequest.setUser(user);
        try {
            return ordersService.getCroppedOrderList(clientOrdersRequest);
        } catch (dtException e) {
            e.printStackTrace();
        }
        return new OrdersTableResponse();
    }


    @RequestMapping(value = "/getListCustomer", //
            method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public List<User> getListCustomer(@RequestBody SubStringSearchRequest subStringSearchRequest) {
        try {
            System.out.println("////");
            return ordersService.getCustomers(subStringSearchRequest);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

}
