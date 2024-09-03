/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.lan.exporter.utilities;

import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.HostHardwareSummary;
import com.vmware.vim25.HostListSummary;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VirtualMachineConfigSummary;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSummary;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lp.lan.exporter.ExporterApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author luispedro
 */
@Component
@PropertySource("classpath:vsphere.properties")
public class Collector implements Runnable {

    private ExposeMetrics exposeMetrics;
    private static ArrayList<Datacenter> datacenters;
    private static ArrayList<Metric> metrics;
    int datacenters_hash = -1;
    int datacenters_current_hash = -1;
    int number_of_datacenters;
    int number_of_clusters;
    int number_of_datastores;
    int current_number_of_objects = 0;
    int previous_number_of_objects = 0;
    int number_of_hosts;

    @Value("${vsphere.vcenter}")
    private String vcenter;

    @Value("${vsphere.username}")
    private String username;

    @Value("${vsphere.password}")
    private String password;

    @Value("${vsphere.collector_interval}")
    private int collector_interval;

    @Autowired
    /*public Collector(MeterRegistry registry) {
        this.meterRegistry = registry;

    }*/

    public static ArrayList<Metric> getMetrics() {
        return metrics;
    }

    public static void setMetrics(ArrayList<Metric> m) {
        metrics = m;
    }

    public static void setDatacenters(ArrayList<Datacenter> dcs) {
        datacenters = dcs;
    }

    public static ArrayList<Datacenter> getDatacenters() {
        return datacenters;
    }

    @Autowired
    public Collector() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void run() {
        int counter = 0;
        while (true) {
            number_of_datacenters = 0;
            number_of_clusters = 0;
            number_of_datastores = 0;
            number_of_hosts = 0;

            System.out.println("Collector-RUNNING COLLECTOR THREAD");
            ArrayList<Datacenter> datacenters_array_list = new ArrayList<>();

            Session session = new Session();
            session.setUsername(this.username);
            session.setvimSdkUrl(this.vcenter);
            session.setPassword(this.password);

            System.out.println("Collector-Login in vcenter: " + this.vcenter);
            boolean login = false;
            try {
                login = session.login();
            } catch (Exception e) {
                System.out.println("" + e.toString());
                System.out.println("Unable to login in the vCenter server");
            }

            if (login) {
                try {
                    ServiceContent serviceContent = session.getServiceContent();
                    VimPortType vimPort = session.getVimPort();

                    //System.out.println("" + serviceContent.getRootFolder());
                    ManagedObjectReference root_folder = serviceContent.getRootFolder();
                    //System.out.println("" + root_folder.getValue());
                    //System.out.println("" + root_folder.getType());

                    ManagedObjectReference serviceInstance = new ManagedObjectReference();
                    serviceInstance.setType("ServiceInstance");
                    serviceInstance.setValue("ServiceInstance");

                    //get the datacenters list
                    System.out.println("Collector-Getting all the datacenters");
                    List<ObjectContent> datacenter_list = EnvNavigation.getEnvironmentObject(serviceContent.getRootFolder(), "Folder", "childEntity", "Datacenter", new String[]{"name"}, serviceContent, vimPort);

                    for (ObjectContent datacenter : datacenter_list) {

                        number_of_datacenters++;
                        ArrayList<Cluster> clusters_array_list = new ArrayList<>();

                        //System.out.println("" + datacenter.getObj().getValue());
                        Datacenter dc_aux = new Datacenter();
                        dc_aux.setMoref(datacenter.getObj());
                        dc_aux.setParent(root_folder);
                        dc_aux.setInventory_object("datacenter");

                        Object[] array_properties = datacenter.getPropSet().toArray();
                        for (Object obj_aux : array_properties) {
                            DynamicProperty dynprop = (DynamicProperty) obj_aux;
                            dc_aux.setName(dynprop.getVal().toString());
                            //System.out.println("DATACENTER NAME: "+dynprop.getName());
                        }
                        System.out.println("Collector-Datacenter: " + dc_aux.getName());
                        //get the cluster HOST FOLDER
                        System.out.println("Getting the host folder object which contains the clusters objects");
                        List<ObjectContent> host_folders = EnvNavigation.getEnvironmentObject(datacenter.getObj(), "Datacenter", "hostFolder", "Folder", null, serviceContent, vimPort);

                        System.out.println("Collector-Getting all the clusters");
                        for (ObjectContent host_folder : host_folders) {

                            //get the host clusters
                            List<ObjectContent> cluster_list = EnvNavigation.getEnvironmentObject(host_folder.getObj(), "Folder", "childEntity", "ClusterComputeResource", new String[]{"name"}, serviceContent, vimPort);
                            for (ObjectContent cluster : cluster_list) {

                                number_of_clusters++;

                                Cluster cluster_aux = new Cluster();
                                ManagedObjectReference cluster_obj = cluster.getObj();
                                cluster_aux.setMoref(cluster.getObj());
                                cluster_aux.setParent(host_folder.getObj());
                                cluster_aux.setDatacenter(dc_aux.getName());
                                cluster_aux.setInventory_object("cluster");

                                array_properties = cluster.getPropSet().toArray();
                                for (Object obj_aux : array_properties) {
                                    DynamicProperty dynprop = (DynamicProperty) obj_aux;
                                    cluster_aux.setName(dynprop.getVal().toString());
                                    // System.out.println("CLUSTER NAME: "+dynprop.getVal().toString());
                                }

                                System.out.println("Collector-Getting all datastores from cluster: " + cluster.getObj().getValue());
                                List<ObjectContent> datastore_list = EnvNavigation.getEnvironmentObject(cluster_obj, "ClusterComputeResource", "datastore", "Datastore", new String[]{"summary"}, serviceContent, vimPort);
                                ArrayList<Datastore> datastores_array_list = new ArrayList<>();
                                for (ObjectContent ds : datastore_list) {
                                    //System.out.println("" + ds.getObj().getValue());
                                    number_of_datastores++;
                                    Datastore datastore_aux = new Datastore();
                                    datastore_aux.setMoref(ds.getObj());
                                    datastore_aux.setParent(cluster_aux.getMoref());
                                    datastore_aux.setCluster(cluster_aux.getName());
                                    datastore_aux.setInventory_object("datastore");

                                    // getting the datastores properties
                                    array_properties = ds.getPropSet().toArray();
                                    for (Object obj_aux : array_properties) {
                                        DynamicProperty dynprop = (DynamicProperty) obj_aux;
                                        DatastoreSummary sum = (DatastoreSummary) dynprop.getVal();
                                        datastore_aux.setCapacity(sum.getCapacity());
                                        datastore_aux.setFreeSpace(sum.getFreeSpace());
                                        datastore_aux.setName(sum.getName());
                                    }
                                    System.out.println("Collector-Datastore: " + datastore_aux.getName());
                                    datastore_aux.setMetrics((ArrayList<Metric>) this.metrics.stream().filter(obj -> obj.getObject_type().equals("datastore")).collect(Collectors.toList()));
                                    datastores_array_list.add(datastore_aux);

                                }
                                cluster_aux.setDatastores(datastores_array_list);
                                clusters_array_list.add(cluster_aux);

                                //COLLECT THE HOSTS OF THE CLUSTER
                                System.out.println("Collector-Getting all hosts from cluster: " + cluster.getObj().getValue());
                                List<ObjectContent> host_list = EnvNavigation.getEnvironmentObject(cluster_obj, "ClusterComputeResource", "host", "HostSystem", new String[]{"summary", "name"}, serviceContent, vimPort);
                                ArrayList<Host> hosts_array_list = new ArrayList<>();
                                for (ObjectContent host : host_list) {
                                    number_of_hosts++;
                                    Host host_aux = new Host();
                                    host_aux.setMoref(host.getObj());
                                    host_aux.setParent(cluster_aux.getMoref());
                                    host_aux.setCluster(cluster_aux.getName());
                                    host_aux.setInventory_object("host");

                                    array_properties = host.getPropSet().toArray();
                                    //getting the name of the host
                                    Object obj_aux = array_properties[0];
                                    DynamicProperty dynprop = (DynamicProperty) obj_aux;
                                    System.out.println("" + dynprop.getVal());
                                    host_aux.setName(dynprop.getVal().toString());

                                    //getting the hardware resources of the host
                                    obj_aux = array_properties[1];
                                    dynprop = (DynamicProperty) obj_aux;
                                    System.out.println("" + dynprop.getName());
                                    HostListSummary sum = (HostListSummary) dynprop.getVal();
                                    HostHardwareSummary host_hardware = (HostHardwareSummary) sum.getHardware();
                                    host_aux.setNumCpuCores(host_hardware.getNumCpuCores());
                                    host_aux.setNumCpuThreads(host_hardware.getNumCpuThreads());
                                    host_aux.setMemorySize(host_hardware.getMemorySize());

                                    host_aux.setMetrics((ArrayList<Metric>) this.metrics.stream().filter(obj -> obj.getObject_type().equals("host")).collect(Collectors.toList()));
                                    hosts_array_list.add(host_aux);
                                }

                                cluster_aux.setHosts(hosts_array_list);

                            }
                            dc_aux.setClusters(clusters_array_list);
                        }

                        //getting the VMs folder, to retrieve all VMs
                        List<ObjectContent> vms_folder = EnvNavigation.getEnvironmentObject(datacenter.getObj(), "Datacenter", "vmFolder", "Folder", null, serviceContent, vimPort);
                        System.out.println("" + vms_folder);
                        for (ObjectContent vm_folder : vms_folder) {
                            List<ObjectContent> vms_list = EnvNavigation.getEnvironmentObject(vm_folder.getObj(), "Folder", "childEntity", "VirtualMachine", new String[]{"summary"}, serviceContent, vimPort);
                            for (ObjectContent vm : vms_list) {
                                System.out.println("" + vm.getObj().getValue());
                                array_properties = vm.getPropSet().toArray();
                                for (Object obj_aux : array_properties) {
                                    DynamicProperty dynprop = (DynamicProperty) obj_aux;
                                    VirtualMachineSummary vm_sum = (VirtualMachineSummary) dynprop.getVal();

                                    VirtualMachineConfigSummary vm_config = vm_sum.getConfig();
                                    VirtualMachineRuntimeInfo vm_runtime = vm_sum.getRuntime();
                                    System.out.println("CPU: " + vm_config.getNumCpu());
                                    System.out.println("MEM: " + vm_config.getMemorySizeMB());
                                    System.out.println("HOST: " + vm_runtime.getHost().getValue());
                                }
                            }
                        }

                        datacenters_array_list.add(dc_aux);
                    }

                    /*System.out.println("\n");
                for (Datacenter dc : datacenters_array_list) {
                    System.out.println("" + dc.getName());
                    for (Cluster cl : dc.getClusters()) {
                        System.out.println("" + cl.getName());
                        for (Datastore ds : cl.getDatastores()) {
                            System.out.println("" + ds.getName());
                            System.out.println("" + ds.getCapacity());
                            System.out.println("" + ds.getFreeSpace());
                            System.out.println("\n");
                        }
                        System.out.println("\n");
                    }
                    System.out.println("\n");
                }*/
                    datacenters = datacenters_array_list;
                    current_number_of_objects = number_of_datacenters + number_of_clusters + number_of_datastores + number_of_hosts;

                    //datacenters_current_hash = datacenters.toString().hashCode();
                    System.out.println("Collector-VCENTER CURRENT NUMBER OF OBJECTS: " + current_number_of_objects);
                    //session logout
                    vimPort.logout(serviceContent.getSessionManager());

                    ExposeMetrics.UpdateMetrics();

                    if (current_number_of_objects != previous_number_of_objects) {
                        System.out.println("Collector-New objects were added to the Datacenters, so updating all metrics definitions with ExposeMtrics function");
                        ExposeMetrics.ExposeMetrics();
                        //counter++;
                        previous_number_of_objects = current_number_of_objects;
                    }

                } catch (RuntimeFaultFaultMsg e) {
                    e.printStackTrace();
                } catch (InvalidPropertyFaultMsg ex) {
                    ex.printStackTrace();
                    Logger.getLogger(Collector.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error collecting data from vCenter (might be due a broken connection to the vCenter in the middle of the process");
                }
            } else {
                System.out.println("Collector-Sleeping for 2 minutes due to unable to connect the vCenter server");
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Collector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            System.out.println("Collector-Sleeping for " + collector_interval + "s");
            try {
                Thread.sleep(collector_interval * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Collector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
