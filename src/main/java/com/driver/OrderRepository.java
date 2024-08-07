package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        String orderId = order.getId();
        orderMap.put(orderId, order);
        //partnerToOrderMap.put(orderId,partnerToOrderMap.getOrDefault(orderId, new HashSet<String>).add(xx))
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        partnerMap.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
            HashSet<String> set = partnerToOrderMap.getOrDefault(partnerId, new HashSet<String>());
            set.add(orderId);
            partnerToOrderMap.put(partnerId, set);

            partnerMap.get(partnerId).setNumberOfOrders(partnerMap.get(partnerId).getNumberOfOrders()+1);

            orderToPartnerMap.put(orderId, partnerId);
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        return partnerMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        HashSet<String> temp = partnerToOrderMap.get(partnerId);
        List<String> ans = new ArrayList<>(temp);
        return ans;
    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        List<String> ans = new ArrayList<>(orderMap.keySet());
        return ans;
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        partnerMap.remove(partnerId);
        partnerToOrderMap.put(partnerId, new HashSet<>());

        for(String a : orderToPartnerMap.keySet()){
            String partner = orderToPartnerMap.get(a);
            if(a == partnerId){
                orderToPartnerMap.remove(a);
            }
        }
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        orderMap.remove(orderId);
        orderToPartnerMap.remove(orderId);

        for(String partner : partnerToOrderMap.keySet()) {
            HashSet<String> hset = partnerToOrderMap.get(partner);
            if(hset.contains(orderId)){
                hset.remove(orderId);
                partnerToOrderMap.put(partner, hset);
            }
        }
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here

        return orderMap.size() - orderToPartnerMap.size();
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        int currTime = (Integer.parseInt(timeString.substring(0,2)) * 60) + Integer.parseInt(timeString.substring(2,4));
        int count = 0;

        for(String orderNo : partnerToOrderMap.get(partnerId)){
            if(orderMap.get(orderNo).getDeliveryTime() > currTime)
                count++;
        }

        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        int time = Integer.MIN_VALUE;

        for(String orderNo : partnerToOrderMap.get(partnerId))
            time = Math.max(orderMap.get(orderNo).getDeliveryTime(),time);

        String ans = "";
        int minutes = time % 100;
        int hour = (time/100)/60;
        return ans = hour + "" + minutes;
    }
}