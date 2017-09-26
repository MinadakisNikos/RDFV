/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ics.forth.rdfvisualizer.api.core.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author minadakn
 */
public class XMLPropertyReader {
    
    
     private Properties prop;

    public XMLPropertyReader() throws IOException {

        this.prop = new Properties();

        File file = new File("./" + Resources.propertyFilename);
	FileInputStream fileInput = new FileInputStream(file);
	
        prop.loadFromXML(fileInput);
	
        fileInput.close();
        
    }

    public String getProperty(String property) {
        String retValue = this.prop.getProperty(property);
        return retValue;
    }
    
    
  
    
}
