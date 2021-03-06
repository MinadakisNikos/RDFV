/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ics.forth.rdfvisualizer.api.core.properties;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import gr.ics.forth.rdfvisualizer.api.example.TestPrioritise;
import gr.ics.forth.rdfvisualizer.api.core.utils.IntPair;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author minadakn
 */
public class XSTREAMpropertyReader {

    public Map<String, List<IntPair>> returnPriorities(String filePath) throws FileNotFoundException {

        FileReader reader = new FileReader(filePath);  // load file

        XStream xstream = new XStream();
        xstream.processAnnotations(Xproperties.class);
        xstream.processAnnotations(Preference.class);
        xstream.processAnnotations(WeightedProperty.class);
        Xproperties data = (Xproperties) xstream.fromXML(reader);

        Preference firstPreference = (Preference) data.preferences.get(0);

        WeightedProperty wp = (WeightedProperty) firstPreference.weightedProperties.get(0);

        Map<String, List<IntPair>> priorities = new HashMap<String, List<IntPair>>();

        List<Preference> preferences = data.preferences;
        for (Preference pref : preferences) {

            List<WeightedProperty> wproperties = pref.weightedProperties;
            List<IntPair> wPairs = new ArrayList();
            for (WeightedProperty wproperty : wproperties) {
                IntPair weightPair = new IntPair();
                weightPair.setPairKey(wproperty.propertyUri);
                weightPair.setPairValue(Integer.parseInt(wproperty.propertyWeight));
                wPairs.add(weightPair);
            }
            priorities.put(pref.type_uri, wPairs);
        }
        return priorities;
        // System.out.println(priorities);
    }

//    @XStreamAlias("xproperties") 
//    public class Xproperties {
//        
//    @XStreamAlias("virtuoso_host") // 
//    private String virtuoso_host;
//    
//    @XStreamImplicit(itemFieldName = "preference")
//    public List preferences = new ArrayList();
//    }
//@XStreamAlias("preference")
//public class Preference {
//
//    @XStreamAlias("type_uri")
//    private String type_uri;
//
//    @XStreamImplicit(itemFieldName = "weighted_property")
//    private List weightedProperties = new ArrayList();
//
//}
//@XStreamAlias("weighted_property")
//public class WeightedProperty {
//
//    @XStreamAlias("property_uri")
//    private String propertyUri;
//    
//    @XStreamAlias("property_weight")
//    private String propertyWeight;
//
//}
}
