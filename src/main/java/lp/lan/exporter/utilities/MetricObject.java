/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.lan.exporter.utilities;
import com.vmware.vim25.ManagedObjectReference;
import org.springframework.stereotype.Component;
/**
 *
 * @author luispedro
 */
@Component
public class MetricObject {
 
       //private  ManagedObjectReference moref;
       /*private String function;
       private  String resource_type;       
       private  double metric_value;*/
       private String name;
       
       public MetricObject(){
           
       }
      /*
       public  MetricObject(ManagedObjectReference mr, String f, String rp, double mv){
           this.moref = mr;
           this.function = f;
           this.resource_type = rp;
           this.metric_value = mv;

       }*/

    /*public  ManagedObjectReference getMoref() {
        return this.moref;
    }

    public  void setMoref(ManagedObjectReference moref) {
        this.moref = moref;
    }*/
/*
    public  String getFunction() {
        return this.function;
    }

    public  void setFunction(String function) {
        this.function = function;
    }

    public  String getResource_type() {
        return this.resource_type;
    }

    public  void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public double getMetric_value() {
        return metric_value;
    }

    public void setMetric_value(double metric_value) {
        this.metric_value = metric_value;
    }
*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
       
              

}
