package com.example.lee.circuitdesign.device;

/**
 * Created by Lee on 2016-11-28.
 */
public interface factory {
    int size();
    Object get(int i);
  //  boolean create(float touchX,float touchY);
    void add(Object o);
    void delete();
    void deleteAll();
    boolean select(float x, float y);
}
