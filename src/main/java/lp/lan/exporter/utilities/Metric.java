/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.lan.exporter.utilities;

import io.micrometer.core.instrument.Counter;

/**
 *
 * @author luispedro
 */
public class Metric {
    
    
    private String name;
    private String description;
    private String[] labels;
    private long value;
    private String method_name;
    private String object_type;


    public Metric(String name, String description, String[] labels, long value, String property_name, String object_type) {
      
        this.name = name;
        this.description = description;
        this.labels = labels;
        this.value = value;
        this.method_name = property_name;
        this.object_type = object_type;
    }

 
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }
 
    
}
