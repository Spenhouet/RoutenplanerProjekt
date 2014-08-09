package de.dhbw.horb.routePlanner.ui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.data.StAXMapGraphDataParser;

public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent> {

    private ComboBox<String> comboBox;
    private StringBuilder sb; // DELETE Robin: wenn nicht gebraucht
    private ObservableList<String> data; // DELETE Robin: wenn nicht gebraucht
    private boolean moveCaretToPos = false;
    private int caretPos;
    private Map<String, List<String>> nodes = null;

    public AutoCompleteComboBoxListener(final ComboBox<String> comboBox) {
	this.comboBox = comboBox;

	try {
	    nodes = StAXMapGraphDataParser.getNodeXMLMap();
	} catch (FileNotFoundException | XMLStreamException e) {
	    e.printStackTrace();
	}

	sb = new StringBuilder();
	data = comboBox.getItems();

	this.comboBox.setEditable(true);
	this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

	    @Override
	    public void handle(KeyEvent t) {
		comboBox.hide();
	    }
	});
	this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
    }

    @Override
    public void handle(KeyEvent event) {

	if (event.getCode() == KeyCode.UP) {
	    caretPos = -1;
	    moveCaret(comboBox.getEditor().getText().length());
	    return;
	} else if (event.getCode() == KeyCode.DOWN) {
	    if (!comboBox.isShowing()) {
		comboBox.show();
	    }
	    caretPos = -1;
	    moveCaret(comboBox.getEditor().getText().length());
	    return;
	} else if (event.getCode() == KeyCode.BACK_SPACE) {
	    moveCaretToPos = true;
	    caretPos = comboBox.getEditor().getCaretPosition();
	} else if (event.getCode() == KeyCode.DELETE) {
	    moveCaretToPos = true;
	    caretPos = comboBox.getEditor().getCaretPosition();
	}

	if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT || event.isControlDown()
		|| event.getCode() == KeyCode.HOME || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
	    return;
	}

	ObservableList<String> list = FXCollections.observableArrayList();

	String input = AutoCompleteComboBoxListener.this.comboBox.getEditor().getText().toLowerCase();
	List<String> names = new ArrayList<String>();

	if (nodes == null)
	    return;

	for (Map.Entry<String, List<String>> entry : nodes.entrySet()) {
	    String key = entry.getKey();
	    if (key == null)
		continue;

	    if (SupportMethods.isNumeric(key))
		continue;

	    if (key.toLowerCase().contains(input.toLowerCase()))
		names.add(key);
	}

	if (names != null) {
	    names = SupportMethods.sortListCompairedToEquality(names, input);
	}

	if (names == null)
	    return;

	for (String name : names) {
	    list.add(name);
	}

	String t = comboBox.getEditor().getText();
	comboBox.setItems(list);
	comboBox.getEditor().setText(t);
	if (!moveCaretToPos) {
	    caretPos = -1;
	}
	moveCaret(t.length());
	if (!list.isEmpty()) {
	    comboBox.show();
	}
    }

    private void moveCaret(int textLength) {
	if (caretPos == -1) {
	    comboBox.getEditor().positionCaret(textLength);
	} else {
	    comboBox.getEditor().positionCaret(caretPos);
	}
	moveCaretToPos = false;
    }

}