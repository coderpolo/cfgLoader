package com.test.cfgLoader.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kaisheng1 on 2017/12/13.
 */

@Component
@Scope(value = "singleton")

public class SharedMap
{
    public ConcurrentHashMap<String, List> getData() {
        return data;
    }

    public void setData(ConcurrentHashMap<String, List> data) {
        this.data = data;
    }

    private ConcurrentHashMap<String,List> data = new ConcurrentHashMap<String, List>();
}

