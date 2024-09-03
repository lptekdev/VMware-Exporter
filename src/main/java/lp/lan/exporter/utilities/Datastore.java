/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.lan.exporter.utilities;

import com.vmware.vim25.ManagedObjectReference;
import io.micrometer.core.instrument.Gauge;
import java.util.ArrayList;

/**
 *
 * @author luisr
 */
public class Datastore {
    
    
    public Datastore(){
    
    }
    
    private ManagedObjectReference moref;
    private String name;
    private String cluster;
    private double capacity;
    private double freeSpace;
    private ManagedObjectReference parent;
    private String inventory_object;
    ArrayList<Metric> metrics;


    public ManagedObjectReference getMoref() {
        return moref;
    }

    public void setMoref(ManagedObjectReference moref) {
        this.moref = moref;
    }

    public ManagedObjectReference getParent() {
        return parent;
    }

    public void setParent(ManagedObjectReference parent) {
        this.parent = parent;
    }

    

    public String getName() {
        return name;
    }

    public String getCluster() {
        return cluster;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getFreeSpace() {
        return freeSpace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setFreeSpace(double freeSpace) {
        this.freeSpace = freeSpace;
    }

    public String getInventory_object() {
        return inventory_object;
    }

    public void setInventory_object(String inventory_object) {
        this.inventory_object = inventory_object;
    }

    public ArrayList<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(ArrayList<Metric> metrics) {
        this.metrics = metrics;
    }



   
    
    
}
