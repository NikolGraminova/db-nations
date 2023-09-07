package org.nations;

import java.math.BigDecimal;
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

        String sql2 = """
                SELECT languages.language
                FROM countries
                JOIN regions ON countries.region_id = regions.region_id
                JOIN continents ON regions.continent_id = continents.continent_id
                JOIN country_languages ON countries.country_id = country_languages.country_id
                JOIN languages ON country_languages.language_id = languages.language_id
                WHERE countries.country_id = ?
                ORDER BY countries.name;
                """;

        String sql3 = """
                SELECT year, population , gdp\s
                FROM countries
                JOIN country_stats on countries.country_id = country_stats.country_id
                WHERE countries.country_id = ?
                ORDER by year desc
                limit 1;
                """;

        try{
            Scanner scan = new Scanner(System.in);
            Connection conn = DriverManager.getConnection(url, user, password);
            boolean run = true;
            while (run){
                System.out.print("Search: ");
                String searchString = scan.nextLine();
                try(PreparedStatement ps = conn.prepareStatement(sql1)){
                    ps.setString(1, searchString);
                    System.out.println("COUNTRY          " +  "ID          " + "REGION          " + "CONTINENT          ");
                    try(ResultSet rs = ps.executeQuery()){
                        while (rs.next()){
                            String country = rs.getString("country");
                            int id = rs.getInt("id");
                            String region = rs.getString("region");
                            String continent = rs.getString("continent");
                            System.out.println(country + "          " + id + "          " + region + "          " + continent);
                        }
                    }
                }
                System.out.print("Choose a country id: ");
                int searchId = Integer.parseInt(scan.nextLine());
                try(PreparedStatement ps = conn.prepareStatement(sql2)){
                    ps.setInt(1, searchId);
                    try(ResultSet rs = ps.executeQuery()){
                        System.out.println("\n" + "Details:");
                        System.out.print("Languages: ");
                        while(rs.next()){
                            String language = rs.getString("language");
                            System.out.print(language + " ");
                        }
                    }
                }
                try(PreparedStatement ps = conn.prepareStatement(sql3)){
                    ps.setInt(1, searchId);
                    try(ResultSet rs = ps.executeQuery()){
                        while(rs.next()){
                            int year = rs.getInt("year");
                            int population = rs.getInt("population");
                            BigDecimal gdp = rs.getBigDecimal("gdp");

                            System.out.println("\n" + "Most recent stats");
                            System.out.println("Year: " + year);
                            System.out.println("Population: " + population);
                            System.out.println("GDP: " + gdp + "\n");
                        }

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