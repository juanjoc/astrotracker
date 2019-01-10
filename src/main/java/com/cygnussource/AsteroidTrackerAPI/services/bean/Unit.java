package com.cygnussource.AsteroidTrackerAPI.services.bean;


public interface Unit<T> {

    T find(String val);

    String getUnit();

    T[] values();


}
