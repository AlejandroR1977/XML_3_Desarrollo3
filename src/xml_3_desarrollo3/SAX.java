package xml_3_desarrollo3;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SAX {

    public class Sales extends DefaultHandler {

        private SAXParser pars = null;
        private SAXParserFactory SaxParsFact;
        private double TSales;
        private boolean ISales;
        private static final String ClassName = Sales.class.getName();
        private final static Logger Log = Logger.getLogger(ClassName);
        private String Element;
        private String ID;
        private String Name;
        private String LName;
        private String Sales;
        private String Dept;
        private String State;
        private String Key;
        private HashMap<String, Double> HashState;
        private HashMap<String, Double> HashDept;
        
        public static void main(String[] args) {
        if (args.length == 0) {
            Log.severe("No file to process. Usage is:" + "\njava DeptSalesReport <keyword>");
            return;
        }
        File xmlFile = new File(args[0]);
        SAX handler = new SAX();
        handler.Check(xmlFile);
    }

        public Sales() {
            super();
            SaxParsFact.setNamespaceAware(true);
            SaxParsFact.setValidating(true);
            SaxParsFact = SAXParserFactory.newInstance();

            HashState = new HashMap<>();
            HashDept = new HashMap<>();
        }

        private void Check(File file) {
            try {
                pars = SaxParsFact.newSAXParser();
            } catch (SAXException | ParserConfigurationException e) {
                Log.severe(e.getMessage());
                System.exit(1);
            }
            System.out.println("Analizando la sintaxis del documento : " + file + "...");
            try {
                pars.parse(file, this);
                Key = State;
            } catch (SAXException | IOException e) {
                Log.severe(e.getMessage());
            }
        }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (localName.equals("sale_record")) {
            ISales = true;
        }
        Element = localName;
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("sale_record")) {
            double vs = 0.0;
            try {
                vs = Double.parseDouble(this.Sales);
            } catch (NumberFormatException e) {
                Log.severe(e.getMessage());
            }
            if (HashState.containsKey(this.State)) {
                double sum = HashState.get(this.State);
                HashState.put(this.State, sum + vs);
            } else {
                HashState.put(this.State, vs);
            }
            if (HashDept.containsKey(this.Dept)) {
                double sum = HashDept.get(this.Dept);
                HashDept.put(this.Dept, sum + vs);
            } else {
                HashDept.put(this.Dept, vs);
            }
            TSales = TSales + vs;
            ISales = false;
        }
    }
    @Override
    public void characters(char[] bytes, int start, int length) throws SAXException {

        switch (Element) {
            case "first_name":
                this.Name = new String(bytes, start, length);
                break;
            case "last_name":
                this.LName = new String(bytes, start, length);
                break;
            case "sales":
                this.Sales = new String(bytes, start, length);
                break;
            case "department":
                this.Dept = new String(bytes, start, length);
                break;
            case "state":
                this.State = new String(bytes, start, length);
                break;
            case "id":
                this.ID = new String(bytes, start, length);
                break;
        }
    }
    @Override
    public void startDocument() throws SAXException {
        TSales = 0.0;
    }
    @Override
    public void endDocument() throws SAXException {
        Set<Map.Entry<String, Double>> Depts = HashDept.entrySet();
        Set<Map.Entry<String, Double>> States = HashState.entrySet();
        System.out.println("-----Cantidad de ventas estado-----");
        for (Map.Entry<String, Double> entry : States) {
            System.out.printf("%-15.15s $%,9.2f\n", entry.getKey(), entry.getValue());
        }
        System.out.println("-----Cantidad de ventas por departamento-----");
        for(Map.Entry<String, Double> entry : Depts){
            System.out.printf("%-15.15s $,9.2f\n", entry.getKey(), entry.getValue());
        }
        System.out.printf("Total de ventas: $%,9.2f\n", TSales);
    }
    private void Print() {
        System.out.printf("%4.4s %-10.10s %-10.10s %9.9s %-10.10s %-15.15s\n",
                ID, Name, LName, Sales, State, Dept);
    }
    }
}
