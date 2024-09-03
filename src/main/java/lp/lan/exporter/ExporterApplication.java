package lp.lan.exporter;

import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lp.lan.exporter.utilities.Collector;
import lp.lan.exporter.utilities.Datacenter;
import lp.lan.exporter.utilities.Metric;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class ExporterApplication {

    
    public static void main(String[] args) {
        //SpringApplication.run(ExporterApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(ExporterApplication.class, args);
        
        /*METRICS DEFINITION*/
        Metric datastore_free_space_metric = new Metric("datastore_free_space","Cached Datastore freespace in bytes",new String[]{},0,"getFreeSpace", "datastore");
        Metric datastore_total_capacity = new Metric("datastore_total_capacity","Cached Datastore total capacity in bytes",new String[]{},0,"getCapacity", "datastore");
        Metric host_num_cpu_cores = new Metric("host_num_cpu_cores","Number of physical cores", new String[]{},0,"getNumCpuCores","host");
        Metric host_memory_size = new Metric("host_memory_size","Number of total memory", new String[]{},0,"getMemorySize","host");
        
        ArrayList<Metric> metrics_array_aux = new ArrayList<>();        
        metrics_array_aux.add(datastore_free_space_metric);
        metrics_array_aux.add(datastore_total_capacity);
        metrics_array_aux.add(host_num_cpu_cores);
        metrics_array_aux.add(host_memory_size);
                
        //Thread to start ehe metric collector
        Collector.setDatacenters(new ArrayList<Datacenter>());
        Collector.setMetrics(metrics_array_aux);
        //Collector t = new Collector();
        //Collector t;
        Collector collector = context.getBean(Collector.class);
        collector.run();
        
        
        
        System.out.println("Main method executed by main thread"); 
    
        
    }

}
