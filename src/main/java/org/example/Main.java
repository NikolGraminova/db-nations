package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/db-nations";
        String user = "root";
        String password = "Myroot23?";

        String sql1 = "SELECT countries.name as country, countries.country_id as id, regions.name region, continents.name as continent\n" +
                "FROM countries\n" +
                "JOIN regions ON countries.region_id = regions.region_id\n" +
                "JOIN continents ON regions.continent_id = continents.continent_id\n" +
                "ORDER BY country;\n";

        try{
            Connection conn = DriverManager.getConnection(url, user, password);
            try(PreparedStatement ps = conn.prepareStatement(sql1)){
                try(ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        String country = rs.getString("country");
                        int id = rs.getInt("id");
                        String region = rs.getString("region");
                        String continent = rs.getString("continent");
                        System.out.println(country + " - " + id + " - " + region + " - " + continent);
                    }
                }
            }


        } catch (SQLException s){
            System.out.println("SQL Connection error.");
            s.printStackTrace();
        }


    }
}