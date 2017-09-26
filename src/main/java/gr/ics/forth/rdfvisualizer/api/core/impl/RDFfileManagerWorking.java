/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.ics.forth.rdfvisualizer.api.core.impl;


import gr.ics.forth.redfvisualizer.api.core.utils.RDFTriple;
import gr.ics.forth.redfvisualizer.api.core.utils.Triple;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

/**
 *
 * @author minadakn
 */
public class RDFfileManagerWorking {

    private Model model;
    
    public void readFile(File rdfFile, String rdfFormat) throws FileNotFoundException{
        
        Model model = ModelFactory.createDefaultModel();

        InputStream targetStream = new FileInputStream(rdfFile);
        
        model.read(targetStream,null,rdfFormat);
        
        this.model = model;
    }
    
    
    public ResultSet query(String sparqlQuery) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
        
        //List<RDFTriple> retList= new ArrayList<>();
        
        Query query = QueryFactory.create(sparqlQuery) ;
        
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
    
        ResultSet results = qexec.execSelect();
    
//            for ( ; results.hasNext() ; )
//            {
//                QuerySolution soln = results.nextSolution() ;
//                //RDFNode x = soln.get("varName") ;       // Get a result variable by name.
//                //Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
//                //Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
//                
//                RDFTriple triple = new RDFTriple(soln.get("s").toString(),
//                soln.get("p").toString(),
//                soln.get("o").toString());
//
//                retList.add(triple); 
//                }
        return results;
        
        
    }
    
    public String selectAll()
    {
        String queryString = "Select * where {?s ?p ?o}";
        
        return queryString;
    }
    

     public String selectAllWithLabelsAndTypes(String resource, String labelProperty)
    {
        String queryString = "Select * where {<"+resource+"> ?p ?o .\n"
                + "<"+resource+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?stype .\n"
                + "<"+resource+"> <"+labelProperty+"> ?slabel .\n"
                + "?p <"+labelProperty+"> ?plabel .\n"
               // + "OPTIONAL {?p rdf:type ?ptype} .\n"
                + "?o <"+labelProperty+"> ?olabel .\n"
                + "OPTIONAL {?o <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?otype} .\n"
                + " }";
        
        return queryString;
    }
    
    public String selectLabel(String resource, String labelProperty)
    {
        String queryString = "Select ?label where {<"+resource+"> <"+labelProperty+"> ?label .\n"
                + " }";  
        return queryString;
    }
    
    public String selectType(String resource)
    {
        String queryString = "Select ?type where {<"+resource+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type .\n"
                + " }";  
        return queryString;
    }
    
     public String returnLabel(String resource, String labelProperty) throws RepositoryException, MalformedQueryException, QueryEvaluationException
    {

        String query = selectLabel(resource, labelProperty);
      
         ResultSet sparqlResults = query(query);
        
        String label = "";
       
        for ( ; sparqlResults.hasNext() ; )
        {
           QuerySolution soln = sparqlResults.nextSolution();
           // System.out.println("LABEL: "+result.toString());
           label = soln.get("label").toString();
        }
        
        return label;
}
        
        

     
     public String returnType(String resource) throws RepositoryException, MalformedQueryException, QueryEvaluationException
    {

        String query = selectType(resource);
      
         ResultSet sparqlResults = query(query);
        
        String type = "";
       
        for ( ; sparqlResults.hasNext() ; )
        {
           QuerySolution soln = sparqlResults.nextSolution();
           // System.out.println("LABEL: "+result.toString());
           type = soln.get("type").toString();
        }
        
        return type;

    }
          
    public Map<Triple,List<Triple>> returnOutgoingLinksWithTypes(String resource, String labelProperty) throws RepositoryException, MalformedQueryException, QueryEvaluationException
    {

        Map<Triple,List<Triple>> outgoingLinks = new HashMap<Triple,List<Triple>>();
        
        String query = selectAllWithLabelsAndTypes(resource,labelProperty);
      
        ResultSet sparqlResults = query(query);

    
        
        for ( ; sparqlResults.hasNext() ; ) {

           QuerySolution soln = sparqlResults.nextSolution();
           //System.out.println(result.toString());
       
            Triple mapKey = new Triple();
            Triple mapValue = new Triple();
            
            String key_uri = soln.get("p").toString();

            String key_label = soln.get("plabel").toString();
            //String key_type = result.getBinding("ptype").getValue().stringValue();
            String key_type = "NOTYPE";
            mapKey.setSubject(key_uri);
            mapKey.setLabel(key_label);
            mapKey.setType(key_type);
            
            String value_uri = soln.get("o").toString();
            String value_label = soln.get("olabel").toString();
            String value_type = soln.get("otype").toString();
            mapValue.setSubject(value_uri);
            mapValue.setLabel(value_label);
            mapValue.setType(value_type);
            
             if(outgoingLinks.containsKey(mapKey)) {

             List<Triple> objects = outgoingLinks.get(mapKey);

             objects.add(mapValue);

        outgoingLinks.put(mapKey, objects);

    } else {
            List<Triple> objects = new ArrayList();
            objects.add(mapValue);
            outgoingLinks.put(mapKey, objects);
    }
    }

        return outgoingLinks;
        
    }
}
