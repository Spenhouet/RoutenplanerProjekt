package de.dhbw.horb.routePlanner.ui;

import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import de.dhbw.horb.routePlanner.parser.DomStAXMapNodeParser;

public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent> {

	private ComboBox<T> comboBox;
	private StringBuilder sb; // DELETE wenn nicht gebraucht
	private ObservableList<T> data; // DELETE wenn nicht gebraucht
	private boolean moveCaretToPos = false;
	private int caretPos;

	public AutoCompleteComboBoxListener(final ComboBox<T> comboBox) {
		this.comboBox = comboBox;
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

		ObservableList<T> list = FXCollections.observableArrayList();

		List<String> names;
		String input = AutoCompleteComboBoxListener.this.comboBox.getEditor().getText().toLowerCase();

		names = DomStAXMapNodeParser.getStAXNodeParser().containsName(input);
		Collections.sort(names);
		for (String name : names) {
			list.add((T) name); // FIXME Typsicherheit
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