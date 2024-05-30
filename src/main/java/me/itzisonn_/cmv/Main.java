package me.itzisonn_.cmv;

import lombok.Getter;
import me.itzisonn_.cmv.run.Global;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    @Getter
    private static Global global;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("  Usage: <file_to_run>");
            return;
        }

        if (args.length >= 2) {
            System.out.println("  Usage: <file_to_run>");
            return;
        }

        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String line = reader.readLine();

            while (line != null) {
                lines.add(Utils.removeDuplicatedSpaces(line.trim()));
                line = reader.readLine();
            }

            reader.close();
        }
        catch (IOException e) {
            System.err.println("  Can't find file with name \"" + args[0] + "\"");
            return;
        }

        System.out.println("  CMV-v1.1.4: running file with name \"" + args[0] + "\"");
        global = new Global(lines);
        global.run();
    }
}