package org.codeintelligence.processing;

import org.codeintelligence.database.InformationDatabase;
import org.codeintelligence.models.Road;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataOutput {

    private InformationDatabase db;

    public DataOutput(InformationDatabase db) {
        this.db = db;
    }

    public void toConsole() {
        try {
            ResultSet resultSet = db.readAllData();
            while (resultSet.next()) {
                System.out.printf("%s %s %s %s %n", resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void toXML() {
        final String filePath = "roads.xml";

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("roads");
            document.appendChild(root);

            ResultSet resultSet = db.readAllData();
            Element roadEntry, name, country, length, speedLimit;
            while (resultSet.next()) {
                // road elements
                roadEntry = document.createElement("road");
                root.appendChild(roadEntry);

                name = document.createElement("name");
                name.setTextContent(resultSet.getString(2));
                roadEntry.appendChild(name);

                country = document.createElement("country");
                country.setTextContent(resultSet.getString(3));
                roadEntry.appendChild(country);

                length = document.createElement("length");
                length.setTextContent(resultSet.getString(4));
                roadEntry.appendChild(length);

                speedLimit = document.createElement("speedLimit");
                speedLimit.setTextContent(resultSet.getString(5));
                roadEntry.appendChild(speedLimit);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(filePath);

            transformer.transform(source, result);
        } catch (ParserConfigurationException | SQLException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public void toXML(String inputFilePath, String outputFilePath){
        List<Road> roadsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0].trim();
                String country = data[1].trim();
                Double length = Double.parseDouble(data[2].trim());
                Integer speedLimit = Integer.parseInt(data[3].trim());
                String roadType = data[4].trim();
                Double elevation = Double.parseDouble(data[5].trim());

                Road road = new Road(name, country, length,speedLimit, roadType, elevation);
                roadsList.add(road);
            }

            try {

                DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

                Document document = documentBuilder.newDocument();

                // root element
                Element root = document.createElement("roads");
                document.appendChild(root);


                for (Road road : roadsList) {
                    // road elements
                    Element roadEntry = document.createElement("road");
                    root.appendChild(roadEntry);

                    Element name = document.createElement("name");
                    name.setTextContent(road.getName());
                    roadEntry.appendChild(name);

                    Element country = document.createElement("country");
                    country.setTextContent(road.getCountry());
                    roadEntry.appendChild(country);

                    Element length = document.createElement("length");
                    length.setTextContent(Double.toString(road.getLength()));
                    roadEntry.appendChild(length);

                    Element speedLimit = document.createElement("speedLimit");
                    speedLimit.setTextContent(Integer.toString(road.getSpeedLimit()));
                    roadEntry.appendChild(speedLimit);

                    if (road.getRoadType() != null) {
                        Element roadType = document.createElement("roadType");
                        roadType.setTextContent(road.getRoadType());
                        roadEntry.appendChild(roadType);
                    }

                    if (road.getElevation() != null) {
                        Element elevation = document.createElement("elevation");
                        elevation.setTextContent(Double.toString(road.getElevation()));
                        roadEntry.appendChild(elevation);
                    }
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new File(outputFilePath));

                transformer.transform(source, result);
            } catch (ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
