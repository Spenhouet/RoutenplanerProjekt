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
    @SuppressWarnings("unused")
    private StringBuilder sb;
    @SuppressWarnings("unused")
    private ObservableList<String> data;
    private boolean moveCaretToPos = false;
    private int caretPos;
    private Map<String, List<String>> nodes = null;
    private Map<String, List<Map<String, String>>> routes = null;

    public AutoCompleteComboBoxListener(final ComboBox<String> comboBox) {
	this.comboBox = comboBox;

	try {
	    this.nodes = StAXMapGraphDataParser.getNodeXMLMap();
	    this.routes = StAXMapGraphDataParser.getRouteXMLMap();
	} catch (FileNotFoundException | XMLStreamException e) {
	    e.printStackTrace();
	}

	this.sb = new StringBuilder();
	this.data = comboBox.getItems();

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
	    this.caretPos = -1;
	    moveCaret(this.comboBox.getEditor().getText().length());
	    return;
	} else if (event.getCode() == KeyCode.DOWN) {
	    if (!this.comboBox.isShowing()) this.comboBox.show();
	    this.caretPos = -1;
	    moveCaret(this.comboBox.getEditor().getText().length());
	    return;
	} else if (event.getCode() == KeyCode.BACK_SPACE) {
	    this.moveCaretToPos = true;
	    this.caretPos = this.comboBox.getEditor().getCaretPosition();
	} else if (event.getCode() == KeyCode.DELETE) {
	    this.moveCaretToPos = true;
	    this.caretPos = this.comboBox.getEditor().getCaretPosition();
	}

	if ((event.getCode() == KeyCode.RIGHT) || (event.getCode() == KeyCode.LEFT) || event.isControlDown()
		|| (event.getCode() == KeyCode.HOME) || (event.getCode() == KeyCode.END)
		|| (event.getCode() == KeyCode.TAB)) return;

	ObservableList<String> list = FXCollections.observableArrayList();

	String input = AutoCompleteComboBoxListener.this.comboBox.getEditor().getText().toLowerCase();
	List<String> names = new ArrayList<String>();

	if (this.nodes == null) return;

	for (Map.Entry<String, List<String>> entry : this.nodes.entrySet()) {
	    String key = entry.getKey();
	    List<String> value = entry.getValue();

	    if ((key == null) || (value == null)) continue;

	    if (SupportMethods.isNumeric(key)) continue;

	    List<String> s = this.nodes.get(key);
	    if ((s == null) || s.isEmpty() || (s.size() < 2)) continue;

	    Boolean hasRoute = false;
	    for (String id : value) {
		if (id == null) continue;

		if (this.routes.get(id) != null) {
		    hasRoute = true;
		    break;
		}
	    }

	    if (hasRoute && key.toLowerCase().contains(input.toLowerCase())) names.add(key);
	}

	if (names != null) names = SupportMethods.sortListCompairedToEquality(names, input);

	if (names == null) return;

	for (String name : names)
	    list.add(name);

	String t = this.comboBox.getEditor().getText();
	this.comboBox.setItems(list);
	this.comboBox.getEditor().setText(t);
	if (!this.moveCaretToPos) this.caretPos = -1;
	moveCaret(t.length());
	if (!list.isEmpty()) this.comboBox.show();
    }

    private void moveCaret(int textLength) {
	if (this.caretPos == -1)
	    this.comboBox.getEditor().positionCaret(textLength);
	else this.comboBox.getEditor().positionCaret(this.caretPos);
	this.moveCaretToPos = false;
    }

}