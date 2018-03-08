/*
 * To change this license header, choose License Headers in Project XProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ics.forth.rdfvisualizer.api.example;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;
import gr.ics.forth.rdfvisualizer.api.core.properties.Preference;

import gr.ics.forth.rdfvisualizer.api.core.properties.Resources;
import gr.ics.forth.rdfvisualizer.api.core.properties.WeightedProperty;

import gr.ics.forth.rdfvisualizer.api.core.properties.XMLPropertyReader;
import gr.ics.forth.rdfvisualizer.api.core.properties.XSTREAMpropertyReader;
import gr.ics.forth.rdfvisualizer.api.core.utils.IntPair;
import gr.ics.forth.rdfvisualizer.api.core.utils.Pair;
import gr.ics.forth.rdfvisualizer.api.core.utils.Prioritise;
import gr.ics.forth.rdfvisualizer.api.core.utils.Triple;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

/**
 *
 * @author minadakn
 */
public class TestPrioritise {
    public static void main(String[] args) throws RepositoryException, MalformedQueryException, QueryEvaluationException, FileNotFoundException, IOException{
      
        Map<String,List<IntPair>> priorities = new HashMap<String,List<IntPair>>();
        Map<String,List<IntPair>> prioritiesSorted = new HashMap<String,List<IntPair>>();
        
        XSTREAMpropertyReader xreader = new XSTREAMpropertyReader();
        
       // List<Preference> preferences = data.preferences;
        priorities = xreader.returnPriorities("properties.xml");
        
        System.out.println(priorities);
        
        Prioritise pr = new Prioritise();
        prioritiesSorted = pr.prioritiseProperties(priorities);

        System.out.println(prioritiesSorted);
    }
        
}
