/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.lan.exporter.utilities;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 * @author luispedro
 */


public class VSphereMetrics {

    
    public VSphereMetrics(MeterRegistry registry) {
        System.out.println("RETURNING METRICS!!");
        // Define and register your gauge metric
        Metric datastore_free_space_metric = new Metric("datastore_free_space","Cached Datastore freespace in bytes",new String[]{},0,"freeSpace", "datastore");        
        ArrayList<Metric> metrics_array_aux = new ArrayList<>();        
        metrics_array_aux.add(datastore_free_space_metric);
        
        if (Collector.getDatacenters() != null) {

            for (Datacenter dc : Collector.getDatacenters()) {
                //System.out.println(""+dc.getName());
                for (Cluster cl : dc.getClusters()) {
                    //System.out.println(""+cl.getName());
                    for (Datastore ds : cl.getDatastores()) {
                        for (int i = 0; i < ds.getMetrics().size(); i++) {
                            try {
                                // this returns the fiedl which matches the property ame of the metric, to return the correct metric value
                                Field field = ds.getClass().getDeclaredField(ds.getMetrics().get(i).getMethod_name());
                                field.setAccessible(true); // This allows us to access private fields
                                double value = (double) field.get(ds);

                                Gauge.builder(ds.getMetrics().get(i).getName(), ds, instance -> value)
                                        .description(ds.getMetrics().get(i).getDescription())
                                        .tag("datacenter", dc.getName())
                                        .tag("cluster", cl.getName())
                                        .tag("datastore", ds.getName())
                                        .register(registry);
                            } catch (NoSuchFieldException ex) {
                                Logger.getLogger(VSphereMetrics.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SecurityException ex) {
                                Logger.getLogger(VSphereMetrics.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(VSphereMetrics.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(VSphereMetrics.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        }
    }

    public double GetGaugleDouble() {
        return (double) 32;
    }
}
