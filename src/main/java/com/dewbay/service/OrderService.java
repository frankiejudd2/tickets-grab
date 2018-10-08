package com.dewbay.service;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Yeauty
 * @version 1.0
 * @Description:TODO
 * @date 2018/4/7 11:49
 */
public interface OrderService {

    String buildOrder(String accessToken, String passengerList, String contactMobile, String contactName, JsonNode trainInfoNode);
}
