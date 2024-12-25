package com.m41den.coffeeplex;

import com.m41den.coffeeplex.controller.CliController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            CliController cliController = new CliController(
                    CliController.Backend.SQL_DATABASE,
                    "jdbc:mysql://coffeeplex:kotikzevnul@db:3306/coffeeplex"
            );
            cliController.loop();
        } catch (Exception e) {
            System.err.println("Explodid: " + e.getMessage());
        }
    }

    public static void main_legacy(String[] args) {
        CliController cliController;
        try {
            cliController = new CliController(CliController.Backend.IN_MEMORY, "");
        } catch (Exception e) {return;}
        try {
            cliController.load("data.ser");

        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            // Exception before actual variable assignment, so we are *probably* safe
        }
        cliController.loop();
        try {
            cliController.dump("data.ser");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
}