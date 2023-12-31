package org.codeintelligence.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Road {

    private String name;
    private String country;

    private Double length;
    private Integer speedLimit;

    private String roadType;

    private Double elevation;

    public Road(String name, String country){  // try a constructor
        this.name = name;
        this.country = country;
        this.length = 0.0;
        this.speedLimit = 0;
    }

    public Road(String name, String country, Double length) {
        this.name = name;
        this.country = country;
        this.length = length;
    }

    public Road(String country){
        this("Main Street", country);
    }


    public Road(String name, String country, Double length, String roadType, Double elevation) {
        this.name = name;
        this.country = country;
        this.length = length;
        this.roadType = roadType;
        this.elevation = elevation;
    }

    public Road(String name, String country, Double length, Integer speedLimit, String roadType, Double elevation) {
        this.name = name;
        this.country = country;
        this.length = length;
        this.speedLimit = speedLimit;
        this.roadType = roadType;
        this.elevation = elevation;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        name = (String) ois.readObject();
        country = (String) ois.readObject();
    }

    public Road deserialize(ByteArrayInputStream stream) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(stream);
        try {
            // Casting the result of readObject() occurs after the deserialization process ends
            // which make it possible to read any object and can lead to gadget chain attacks
            return (Road) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Road(){
        this("", "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Integer speedLimit) {
        this.speedLimit = speedLimit;
    }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    @Override
    public String toString() {
        return "Road{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", length=" + length +
                ", speedLimit=" + speedLimit +
                ", roadType='" + roadType + '\'' +
                ", elevation=" + elevation +
                '}';
    }
}
