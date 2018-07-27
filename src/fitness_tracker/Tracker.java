package fitness_tracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Tracker {
	public static final String TITLE = "Fitness Tracker";
	public static final String DB_FILENAME = "db.csv";
	public static final String DATE_FORMAT = "dd-MM-yyyy";

	public static void main(String[] args) {
		start();
	}

	static void start() {
		// init vars
		JTextField weightField = new JTextField(5);
		JTextField bfField = new JTextField(5);
		double weight;
		double bf;
		JPanel myPanel = new JPanel();

		// build panel
		myPanel.add(new JLabel("Weight:"));
		myPanel.add(weightField);
		// myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("Bodyfat %:"));
		myPanel.add(bfField);

		// gather results
		int result = JOptionPane.showConfirmDialog(null, myPanel, TITLE, JOptionPane.OK_CANCEL_OPTION);

		// if user didn't click 'ok', return
		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		// try to parse weight and bodyfat
		try {
			weight = Double.parseDouble(weightField.getText());
			bf = Double.parseDouble(bfField.getText());
		} catch (NumberFormatException e) {
			System.err.println("Invalid input. Nothing was saved.");
			return;
		}

		// save to file
		saveToCsv(LocalDateTime.now(), weight, bf);
	}

	static void saveToCsv(LocalDateTime ldt, double weight, double bodyfat) {
		String date = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH).format(ldt);
		FileWriter pw;
		try {
			pw = new FileWriter(new File(DB_FILENAME), true);
		} catch (IOException e) {
			System.err.println("Error while loading csv. Data not saved.");
			e.printStackTrace();
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(date);
		sb.append(",");
		sb.append(weight);
		sb.append(",");
		sb.append(bodyfat);
		sb.append('\n');

		try {
			pw.write(sb.toString());
			pw.close();
		} catch (IOException e) {
			System.err.println("Error while saving to csv. Data not saved.");
		}

		System.out.println("Saving:");
		System.out.println("Weight: " + weight);
		System.out.println("Bodydfat: " + bodyfat);
		System.out.println("Date: " + date);
	}
}
