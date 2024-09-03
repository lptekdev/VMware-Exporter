/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.lan.exporter.utilities;

import com.vmware.vim25.ManagedObjectReference;
import java.util.ArrayList;

/**
 *
 * @author luispedro
 */
public class Host {
    
    
    public Host () {
        
    }
    private ManagedObjectReference moref;
    private String name;
    private ManagedObjectReference parent;
    private String inventory_object;
    private double memorySize;
    private double numCpuCores;
    private double numCpuThreads;
    private String cluster;
    ArrayList<Metric> metrics;

    public ManagedObjectReference getMoref() {
        return moref;
    }

    public void setMoref(ManagedObjectReference moref) {
        this.moref = moref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ManagedObjectReference getParent() {
        return parent;
    }

    public void setParent(ManagedObjectReference parent) {
        this.parent = parent;
    }

    public String getInventory_object() {
        return inventory_object;
    }

    public void setInventory_object(String inventory_object) {
        this.inventory_object = inventory_object;
    }

    public double getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    public double getNumCpuCores() {
        return numCpuCores;
    }

    public void setNumCpuCores(int numCpuCores) {
        this.numCpuCores = numCpuCores;
    }

    public double getNumCpuThreads() {
        return numCpuThreads;
    }

    public void setNumCpuThreads(int numCpuThreads) {
        this.numCpuThreads = numCpuThreads;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public ArrayList<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(ArrayList<Metric> metrics) {
        this.metrics = metrics;
    }
    
    
    
    
}
