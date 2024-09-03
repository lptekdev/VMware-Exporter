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
public class Cluster {
    
    public Cluster(){
        
    }
    
    private ManagedObjectReference moref;
    private String name;
    private String datacenter;
    private ArrayList<Datastore> datastores;
    private ManagedObjectReference parent;
    private String inventory_object;
    private ArrayList<Host> hosts;

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

    public ArrayList<Datastore> getDatastores() {
        return datastores;
    }

    public void setDatastores(ArrayList<Datastore> datastores) {
        this.datastores = datastores;
    }

    public ManagedObjectReference getParent() {
        return parent;
    }

    public void setParent(ManagedObjectReference parent) {
        this.parent = parent;
    }

    public String getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(String datacenter) {
        this.datacenter = datacenter;
    }

    public String getInventory_object() {
        return inventory_object;
    }

    public void setInventory_object(String inventory_object) {
        this.inventory_object = inventory_object;
    }

    public ArrayList<Host> getHosts() {
        return hosts;
    }

    public void setHosts(ArrayList<Host> hosts) {
        this.hosts = hosts;
    }
    
    
}
