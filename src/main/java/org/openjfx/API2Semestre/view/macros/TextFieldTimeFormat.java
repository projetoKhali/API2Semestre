package org.openjfx.api2semestre.view.macros;

import javafx.scene.control.TextField;

public class TextFieldTimeFormat {

    private static final String TIME_REGEX = "([01]?\\d|2[0-3]):([0-5]?\\d)";
    private static final String TIME_FORMAT = "%02d:%02d";

    // Method for formatting and "clamping" the value of a time TextField
    public static void addTimeVerifier(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(TIME_REGEX) && newValue.length() == 5 && newValue.charAt(2) == ':') {{
                String[] parts = newValue.split(":");
                if (parts.length == 2) {
                    int hours = Math.max(0, Math.min(23, Integer.parseInt(parts[0])));
                    int minutes = Math.max(0, Math.min(59, Integer.parseInt(parts[1])));
                    if (hours >= 0 && hours < 24 && minutes >= 0 && minutes < 60) return;
                }
            }}

            newValue = newValue.replaceAll("[^\\d:]", "");
            
            // Format the input into a valid time value
            String[] split = newValue.split(":");
            System.out.println(newValue + " length " + split.length);
            for (String s : split) System.out.print(s + " ");
            String[] parts = new String[] { split.length > 0 ? split[0] : "0", split.length > 1 ?  split[1] : "0"};
            String formattedValue = String.format(
                TIME_FORMAT,
                Math.max(0, Math.min(23, Integer.parseInt(parts[0]))),
                Math.max(0, Math.min(59, Integer.parseInt(parts[1])))
            );

            // Update the text field only if the new value is different
            if (!formattedValue.equals(textField.getText())) {
                textField.setText(formattedValue);
            }
        });
    }

}
