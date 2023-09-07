package org.nations;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/db-nations";
        String user = "root";
        String password = "Myroot23?";

        String sql1 = """
                SELECT countries.name AS country, countries.country_id AS id, regions.name AS region, continents.name AS continent
                FROM countries
                JOIN regions ON countries.region_id = regions.region_id
                JOIN continents ON regions.continent_id = continents.continent_id
                WHERE countries.name LIKE '%' ? '%'\s
                ORDER BY country;
                """;

        try{
            Scanner scan = new Scanner(System.in);
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Enter a string to filter the results: ");
            String search = scan.nextLine();
            try(PreparedStatement ps = conn.prepareStatement(sql1)){
                ps.setString(1, search);
                System.out.println("COUNTRY - ID - REGION - CONTINENT");
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
            scan.close();
        } catch (SQLException s){
            System.out.println("SQL Connection error.");
            s.printStackTrace();
        }


    }
}