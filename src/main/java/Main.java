import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    public static String listToJson(List<Employee> list){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String fileName){
        try (FileWriter  fileWriter = new FileWriter(fileName)){
            fileWriter.write(json);
            fileWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static List<Employee> parseXML (String fileName){
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Node root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i =0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()){
                    Element element = (Element) node;
                    NamedNodeMap map = element.getAttributes();
                    for (int a = 0; a < map.getLength(); a++){
                        String AttrName = map.item(a).getNodeName();
                        String AttrValue = map.item(a).getNodeValue();
                        Employee employee =
                    }

                }
            }

        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        return list;
    }
    public static void main(String[] args) {
        String fileName = "data.csv";
        List<Employee> list = parseCSV(fileName);
        String json = listToJson(list);
        writeString(json, "data.json");
        List<Employee> list1 = parseXML("data.xml");
        String json1 = listToJson(list1);
        writeString(json1, "data1.json");
    }
}
