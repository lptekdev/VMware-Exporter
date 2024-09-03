/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.lan.exporter.utilities;

import com.vmware.vim25.ManagedObjectReference;

/**
 *
 * @author luisr
 */
public class MORef {
    
    public MORef(){
        
    }
    
    private String type;
    private ManagedObjectReference obj;

    public void setTyppe(String type) {
        this.type = type;
    }

    public void setObj(ManagedObjectReference obj) {
        this.obj = obj;
    }
    
}
