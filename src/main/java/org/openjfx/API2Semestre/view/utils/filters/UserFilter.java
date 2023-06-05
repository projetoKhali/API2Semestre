package org.openjfx.api2semestre.view.utils.filters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.authentication.User;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class UserFilter {

    public static List<User> filterFromView (
        List<User> users,
        Optional<TableColumn<User, String>> col_matricula,
        Optional<TableColumn<User, String>> col_name,
        Optional<TableColumn<User, String>> col_email
    ) {
        return apply(
            users,
            getValueOfTextField(col_matricula),
            getValueOfTextField(col_name),
            getValueOfTextField(col_email)
        );
    }

    private static Optional<String> getValueOfTextField (Optional<TableColumn<User, String>> column) {
        if (!column.isPresent()) return Optional.empty();
        var header = column.get().getGraphic();
        if (header instanceof TextField) return Optional.of(((TextField)header).getText());
        if (header instanceof Label) return Optional.of(((Label)header).getText());
        return Optional.empty();
    }

    public static List<User> apply (
        List<User> users,
        Optional<String> matricula,
        Optional<String> name,
        Optional<String> email
    ) {
        return users.stream().filter((User cliente) -> {
            if (matricula.isPresent() && !cliente.getRegistration().contains(matricula.get())) return false;
            if (name.isPresent() && !cliente.getName().equals(name.get())) return false;
            if (email.isPresent() && !cliente.getEmail().equals(email.get())) return false;
            return true;
        }).collect(Collectors.toList());

    }
}
