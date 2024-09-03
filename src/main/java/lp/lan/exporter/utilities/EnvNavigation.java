/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.lan.exporter.utilities;

import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.SelectionSpec;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.VimPortType;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author luispedro
 */
public class EnvNavigation {

    public EnvNavigation() {

    }

    public static List<ObjectContent> getEnvironmentObject(ManagedObjectReference managed_obj, String source_type, String source_path, String resource_type, String[] resource_property_name, ServiceContent serviceContent, VimPortType vimPort) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<ObjectContent> objectContents;
        try {
            String selectionspec_name = source_type + "_" + resource_type;
            SelectionSpec folderToFolderSelection = new SelectionSpec();
            folderToFolderSelection.setName(selectionspec_name);

            // spec to traverse from RootFolder to DC
            TraversalSpec nav_traversal = new TraversalSpec();
            nav_traversal.setName(selectionspec_name);
            nav_traversal.setPath(source_path); //o nome propriedade que queremos saber desta source
            nav_traversal.setType(source_type); // sempre o tipo da source que estamos a usar
            nav_traversal.getSelectSet().addAll(Arrays.asList(new SelectionSpec[]{folderToFolderSelection}));
            nav_traversal.setSkip(false);

            //Define the Datacenter "name" property to collect
            PropertySpec propertySpec = new PropertySpec();
            if (resource_property_name != null) {
                propertySpec.getPathSet().addAll(Arrays.asList(resource_property_name)); // proprieadades que queremos obter do resource_type
            }
            propertySpec.setType(resource_type); // o tipo de propriedade que estamos a querer saber desta source

            //define the object spec for the Datacenter collection properties
            ObjectSpec objectSpec = new ObjectSpec();
            objectSpec.setObj(managed_obj);   // o MORef do objecto de origem que estamos a usar          
            objectSpec.getSelectSet().addAll(Arrays.asList(new SelectionSpec[]{nav_traversal}));
            objectSpec.setSkip(false);

            // Create PropertyFilterSpec using the PropertySpec and ObjectPec
            PropertyFilterSpec propertyFilterSpec = new PropertyFilterSpec();
            propertyFilterSpec.getPropSet().addAll(Arrays.asList(new PropertySpec[]{propertySpec}));
            propertyFilterSpec.getObjectSet().addAll(Arrays.asList(new ObjectSpec[]{objectSpec}));

            ManagedObjectReference morPropertyCollector = serviceContent.getPropertyCollector();
            objectContents = vimPort.retrieveProperties(morPropertyCollector, Arrays.asList(new PropertyFilterSpec[]{propertyFilterSpec}));
            return objectContents;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("" + e.toString());
        }
        return null;
        
    }

}
