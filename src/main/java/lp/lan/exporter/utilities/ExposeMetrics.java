/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.lan.exporter.utilities;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.vmware.vim25.ManagedObjectReference;
import java.util.Set;

/**
 *
 * @author luispedro
 */
@Component
public class ExposeMetrics {

    private final Counter myCounter;
    private static MeterRegistry meterRegistry;
    ToDoubleFunction<Datastore> datastore_double_func;

    //private static Map <Map<ManagedObjectReference,String>, Double> full_map = new HashMap<>();
    private static Map<String, Double> hasmap = new HashMap<>();
    private static Map<ManagedObjectReference, String> inner_map = new HashMap<>();

    private static Map<String, Double> metric_object_map = new HashMap<>();

    @Autowired
    public ExposeMetrics(MeterRegistry registry) {
        myCounter = registry.counter("my_counter");
        meterRegistry = registry;

    }

    public void someMethod() {
        // Increment the counter
        myCounter.increment();
    }

    public static void ExposeMetrics() {
        System.out.println("ExposeMetrics-Exposing metrics gauges to METER_REGISTRY");
        if (Collector.getDatacenters() != null) {

            for (Datacenter dc : Collector.getDatacenters()) {
                //System.out.println(""+dc.getName());
                for (Cluster cl : dc.getClusters()) {
                    //System.out.println(""+cl.getName());
                    for (Datastore ds : cl.getDatastores()) {
                        for (int i = 0; i < ds.getMetrics().size(); i++) {
                            
                            String map_key = dc.getName() + cl.getName() + ds.getName() + ds.getMetrics().get(i).getMethod_name();
                           
                            Iterable<Tag> tags = Tags.of("datacenter", dc.getName(), "cluster", cl.getName(), "datastore", ds.getName());
                            Gauge.builder(ds.getMetrics().get(i).getName(), metric_object_map, map -> map.get(map_key))
                                    .tags(tags)
                                    .register(meterRegistry);
                           
                        }
                    }
                     for (Host host : cl.getHosts()) {
                        for (int i = 0; i < host.getMetrics().size(); i++) {
                            
                            String map_key = dc.getName() + cl.getName() + host.getName() + host.getMetrics().get(i).getMethod_name();
                           
                            Iterable<Tag> tags = Tags.of("datacenter", dc.getName(), "cluster", cl.getName(), "host", host.getName());
                            Gauge.builder(host.getMetrics().get(i).getName(), metric_object_map, map -> map.get(map_key))
                                    .tags(tags)
                                    .register(meterRegistry);
                           
                        }
                    }
                }
            }
        }
    }

    public static void UpdateMetrics() {
        System.out.println("ExposeMetrics-Updating Metrics Hashtable");
        for (Datacenter dc : Collector.getDatacenters()) {
            //System.out.println(""+dc.getName());
            for (Cluster cl : dc.getClusters()) {
                //System.out.println(""+cl.getName());
                for (Datastore ds : cl.getDatastores()) {
                    for (int i = 0; i < ds.getMetrics().size(); i++) {
                        try {
                            Method method = ds.getClass().getMethod(ds.getMetrics().get(i).getMethod_name());                           
                            String map_key = dc.getName() + cl.getName() + ds.getName() + ds.getMetrics().get(i).getMethod_name();
                            metric_object_map.put(map_key, (double) method.invoke(ds));
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(ExposeMetrics.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvocationTargetException ex) {
                            Logger.getLogger(ExposeMetrics.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                for (Host host : cl.getHosts()) {
                    for (int i = 0; i < host.getMetrics().size(); i++) {
                        try {
                            Method method = host.getClass().getMethod(host.getMetrics().get(i).getMethod_name());                           
                            String map_key = dc.getName() + cl.getName() + host.getName() + host.getMetrics().get(i).getMethod_name();
                            metric_object_map.put(map_key, (double) method.invoke(host));
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(ExposeMetrics.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvocationTargetException ex) {
                            Logger.getLogger(ExposeMetrics.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }
}
