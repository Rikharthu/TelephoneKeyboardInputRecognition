package com.example.uberv;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static final String LOG_TAG = Main.class.getSimpleName();
    private static List<PhoneButton> sButtons;

    public static void main(String[] args) {
        Logger.init(Logger.LogLevel.DEBUG);
        Logger.d("main()");
        Logger.i("main()");
        Logger.e("main()");
        Logger.out("TEST");
        try {
            throw new Exception("my exception");
        } catch (Exception e) {
            Logger.e(e, "main - %s", "Exception occured");
            Logger.e(e);
        }

//        Scanner sc = new Scanner(System.in);
//        Logger.out("Please type an Integer");
//        int i = sc.nextInt();
//        System.out.println("You typed: " + i);

        Scanner scanner = new Scanner("Hello, my name is Richard Kuodis!");
        scanner.useDelimiter(" ");
        while (scanner.hasNext()) {
            System.out.println(scanner.next());
        }
        scanner.close();

        Pattern pattern = Pattern.compile("([A-Z][a-z]*)([0-9]*)");
        Matcher matcher = pattern.matcher("2277822466");
        String input = "2277822466";
        List<String> numbers = Arrays.stream(input.split(""))
                .map(Integer::parseInt)
                .map(integer -> "Number is " + integer)
                .collect(Collectors.toList());

        // Create phonebuttons
        sButtons = new ArrayList<>();
        sButtons.add(new PhoneButton(1, " "));
        sButtons.add(new PhoneButton(2, "A", "B", "C"));
        sButtons.add(new PhoneButton(3, "D", "E", "F"));
        sButtons.add(new PhoneButton(4, "G", "H", "I"));
        sButtons.add(new PhoneButton(5, "J", "K", "L"));
        sButtons.add(new PhoneButton(6, "M", "N", "O"));
        sButtons.add(new PhoneButton(7, "P", "Q", "R", "S"));
        sButtons.add(new PhoneButton(8, "T", "U", "V"));
        sButtons.add(new PhoneButton(9, "W", "X", "Y", "Z"));

        Scanner fileScanner = null;
        List<String> matching = new ArrayList<>();
        try {
            fileScanner = new Scanner(new BufferedReader(new FileReader("words.txt")));
            fileScanner.useDelimiter("\\r\\n");

            File numbersFile = new File("numbers.txt");
            BufferedWriter writer = Files.newBufferedWriter(numbersFile.toPath(), Charset.forName("UTF-8"));
            PrintWriter printer = new PrintWriter(writer);
            while (fileScanner.hasNext()) {
                String value = fileScanner.next();
                if ("apple".equalsIgnoreCase(value)) {
                    Logger.out("Apple has been found!");
                }
                // TODO write matching values in a separate file and delete when precised
                // TODO or transform original words.txt into numbers.txt
                // TODO try to multi-thread?
                String phoneValue = getPhoneValue(value);
//                if (phoneValue.matches("^626")) {
                if (phoneValue.contains("626")) {
//                    Logger.out("Arbour found");
                    matching.add(value);
                }
                Logger.out(value + " | " + phoneValue);
                printer.println(phoneValue);
            }
        } catch (IOException e) {
            Logger.e(e);
        } finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
        }
        scanner= new Scanner(System.in);
        Logger.out(matching.toString());
        Logger.d("Done");
        scanner.next();
    }

    private static String getPhoneValue(String value) {
        String[] characters = value.split("");
        StringBuilder stringBuilder = new StringBuilder();
        for (String character : characters) {
            try {
                stringBuilder.append(getPhoneButton(character).getNumber());
            } catch (Exception e) {
                Logger.e(e, "Could not find PhoneButton for character: " + character);
            }
        }
        return stringBuilder.toString();
    }

    private static PhoneButton getPhoneButton(String c) {
        for (PhoneButton pb : sButtons) {
            if (pb.hasCharacter(c.toUpperCase())) {
                return pb;
            }
        }
        return null;
    }
}
