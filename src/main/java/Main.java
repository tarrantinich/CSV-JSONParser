import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class Main {

    public static List<Employee> parseCSV(String fileName) {
        List<Employee> list = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(File file) {
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("data.xml");

            Node staffNode = document.getFirstChild();
            NodeList nodeList = staffNode.getChildNodes();
            long id = 0;
            String firstName = "";
            String lastName = "";
            String country = "";
            int age = 0;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                NodeList employeeChilds = node.getChildNodes();
                for (int j = 0; j < employeeChilds.getLength(); j++) {
                    Node atr = employeeChilds.item(j);
                    if (employeeChilds.item(j).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    switch (atr.getNodeName()) {
                        case "id": {
                            id = Long.valueOf(atr.getTextContent());
                            break;
                        }
                        case "firstName": {
                            firstName = atr.getTextContent();
                            break;
                        }
                        case "lastName": {
                            lastName = atr.getTextContent();
                            break;
                        }
                        case "country": {
                            country = atr.getTextContent();
                            break;
                        }
                        case "age": {
                            age = Integer.valueOf(atr.getTextContent());
                            break;
                        }
                    }
                }
                Employee employee = new Employee(id, firstName, lastName, country, age);
                list.add(employee);
            }
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        String fileName = "data.csv";
        List<Employee> list = parseCSV(fileName);
        String json = listToJson(list);
        writeString(json, "data.json");

        File file = new File("data.xml");
        List<Employee> list1 = parseXML(file);
        String json1 = listToJson(list1);
        writeString(json1, "data1.json");
    }
}
