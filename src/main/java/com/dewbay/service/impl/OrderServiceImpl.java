package com.dewbay.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.dewbay.service.OrderService;
import com.dewbay.util.HttpClientUtils;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Yeauty
 * @version 1.0
 * @Description:TODO
 * @date 2018/4/7 11:53
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Value("${seat-name}")
    String seatName;

    @Override
    public String buildOrder(String accessToken, String passengerList, String contactMobile, String contactName, JsonNode trainInfoNode) {
        String url = "http://api.12306.com/v1/train/order?access_token=" + accessToken;

        Map headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN");
        headers.put("Connection", "keep-alive");
        headers.put("Content-Type", "application/json");
        headers.put("Host", "api.12306.com");
        headers.put("Origin", "http://www.12306.com");
        headers.put("Referer", "http://www.12306.com/");
        headers.put("User-Agent", HttpClientUtils.pcUserAgentArray[new Random().nextInt(HttpClientUtils.pcUserAgentArray.length)]);

        //{"deptStationCode":"SNQ","arrStationCode":"IOQ","trainCode":"G6013","deptDate":"2018-04-10","seatPrice":"179.0","runTime":"01:39","deptTime":"09:17","passengers":[{"passengerMobile":null,"passengerName":"张三","passportTypeId":"1","passportNo":"440203198903291234","trainTicketType":"1","policyProductNo":"","passengerId":"","birthday":"","sex":"M","isPassengerSave":true,"insurancePrice":0}],"contactsInfo":{"contactEmail":"","contactMobile":"13726248277","contactName":"李四","contactPassportNo":"","contactPassportType":"1"},"usingTrainAccount":false,"trainZWCode":"O","source":"P2"}
        JsonNode trainCode = trainInfoNode.get("trainCode");

//        JsonNode trainStatus = trainInfoNode.get("trainStatus");
//        JsonNode trainType = trainInfoNode.get("trainType");
//        JsonNode minPrice = trainInfoNode.get("minPrice");
//        JsonNode maxPrice = trainInfoNode.get("maxPrice");
//        JsonNode arrDate = trainInfoNode.get("arrDate");
//        JsonNode deptStationName = trainInfoNode.get("deptStationName");
//        JsonNode arrStationName = trainInfoNode.get("arrStationName");
//        JsonNode arrTime = trainInfoNode.get("arrTime");
//        JsonNode arriveDays = trainInfoNode.get("arriveDays");
//        JsonNode reason = trainInfoNode.get("reason");
//        JsonNode startSaleTime = trainInfoNode.get("startSaleTime");
//        JsonNode ywXiaPrice = trainInfoNode.get("ywXiaPrice");
//        JsonNode rwXiaPrice = trainInfoNode.get("rwXiaPrice");


        JsonNode deptDate = trainInfoNode.get("deptDate");
        JsonNode deptStationCode = trainInfoNode.get("deptStationCode");
        JsonNode arrStationCode = trainInfoNode.get("arrStationCode");
        JsonNode deptTime = trainInfoNode.get("deptTime");
        JsonNode runTime = trainInfoNode.get("runTime");
        JsonNode source = trainInfoNode.get("source");
        JsonNode seatList = trainInfoNode.get("seatList");
        JsonNode classSeatNode = null;
        JsonNode seatNameNode;
        for (JsonNode seatNode : seatList) {
            seatNameNode = seatNode.get("seatName");
            if (seatNameNode != null && seatName.equals(seatNameNode.asText().trim())) {
                classSeatNode = seatNode;
                break;
            }
        }

//        JsonNode seatName = classSeatNode.get("seatName");
//        JsonNode seatNum = classSeatNode.get("seatNum");
//        JsonNode showButton = classSeatNode.get("showButton");

        JsonNode seatPrice = classSeatNode.get("seatPrice");
        JsonNode seatCode = classSeatNode.get("seatCode");


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deptStationCode", deptStationCode.asText());
        jsonObject.put("arrStationCode", arrStationCode.asText());
        jsonObject.put("trainCode", trainCode.asText());
        jsonObject.put("deptDate", deptDate.asText());
        jsonObject.put("seatPrice", seatPrice.asText());
        jsonObject.put("runTime", runTime.asText());
        jsonObject.put("deptTime", deptTime.asText());
        jsonObject.put("usingTrainAccount", false);
        jsonObject.put("trainZWCode", seatCode.asText());
        jsonObject.put("source", source.asText());
        jsonObject.put("passengers", JSONValue.parse(passengerList));

        JSONObject jsonContactsInfo = new JSONObject();
        jsonContactsInfo.put("contactEmail", "");
        jsonContactsInfo.put("contactMobile", contactMobile);
        jsonContactsInfo.put("contactName", contactName);
        jsonContactsInfo.put("contactPassportNo", "");
        jsonContactsInfo.put("contactPassportType", "1");
        jsonObject.put("contactsInfo", jsonContactsInfo);

        String body = jsonObject.toJSONString();
        String json = HttpClientUtils.doPostJson(url, body, headers);
        return json;
    }
}
