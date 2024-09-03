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
public class Datacenter {
    
    public Datacenter(){
        
    }
    
    private ManagedObjectReference moref;
    private String name;
    private ArrayList<Cluster> clusters;
    private ManagedObjectReference parent;
    private String inventory_object;

    public ArrayList<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(ArrayList<Cluster> clusters) {
        this.clusters = clusters;
    }

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
    
    

}


