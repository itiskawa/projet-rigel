package ch.epfl.rigel.bonus;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.*;

/**
 * Text field that shows possible word completions to what was typed in,
 * Completions are chosen from a given SortedSet
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class PredictiveTextField extends TextField {
    private final SortedSet<String> storedStrings;
    private final ContextMenu matchesMenu;

    private final static int MAX_NB_MATCHES = 15;

    /**
     * public constructor
     * @param givenEntries - set of strings from which possible matches are chosen
     */
    public PredictiveTextField(Set<String> givenEntries) {
        super();
        storedStrings = new TreeSet<>(givenEntries);
        matchesMenu = new ContextMenu();
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 0) {
                matchesMenu.hide();
            }
            else {
                List<String> completions = new ArrayList<>(storedStrings.subSet(getText().toUpperCase(), getText().toUpperCase() + 'a'));
                if (storedStrings.size() > 0) {
                    fillMatchesMenu(completions);

                    if (!matchesMenu.isShowing()) {
                        matchesMenu.show(PredictiveTextField.this, Side.BOTTOM, 0, 0);
                    }
                }
                else {
                    matchesMenu.hide();
                }
            }
        });
    }

    /**
     * Fills the drop down list with the possible completions. Total number is limited to MAX_NB_MATCHES
     * @param completions - the possible matches
     */
    private void fillMatchesMenu(List<String> completions) {
        List<CustomMenuItem> completionsDropDown = new ArrayList<>();
        int matchNb = Math.min(completions.size(), MAX_NB_MATCHES);
        for (int i = 0; i < matchNb; ++i) {
            String result = completions.get(i);
            CustomMenuItem match = new CustomMenuItem(new Label(result), true);
            match.setOnAction(event -> {
                setText(result);
                matchesMenu.hide();
            });
            completionsDropDown.add(match);
        }
        matchesMenu.getItems().clear();
        matchesMenu.getItems().addAll(completionsDropDown);
    }
}