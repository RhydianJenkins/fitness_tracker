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
	public static final int SAVE_SUCCESS = 0;
	public static final int SAVE_FAIL = 1;

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
		int optionPaneResult;
		int saveResult;

		// build panel
		myPanel.add(new JLabel("Weight:"));
		myPanel.add(weightField);
		myPanel.add(new JLabel("Bodyfat %:"));
		myPanel.add(bfField);

		// gather results
		optionPaneResult = JOptionPane.showConfirmDialog(null, myPanel, TITLE, JOptionPane.OK_CANCEL_OPTION);

		// if user didn't click 'ok', return
		if (optionPaneResult != JOptionPane.OK_OPTION) {
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
		saveResult = saveToCsv(LocalDateTime.now(), weight, bf);

		// if save was not successful, return
		if (saveResult != SAVE_SUCCESS) {
			return;
		}
	}

	/**
	 * Saves data to a csv file.
	 * 
	 * @param ldt
	 *            The date to save to.
	 * @param weight
	 *            The weight to save.
	 * @param bodyfat
	 *            The bodyfat to save.
	 * @return SAVE_SUCCESS if successful, else SAVE_FAIL
	 */
	static int saveToCsv(LocalDateTime ldt, double weight, double bodyfat) {
		// init vars
		String date;
		FileWriter pw;
		StringBuilder sb;

		// open db file
		try {
			pw = new FileWriter(new File(DB_FILENAME), true);
		} catch (IOException e) {
			System.err.println("Error while loading csv. Data not saved.");
			e.printStackTrace();
			return SAVE_FAIL;
		}

		// format date
		date = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH).format(ldt);

		// gen string to save
		sb = new StringBuilder();
		sb.append(date);
		sb.append(",");
		sb.append(weight);
		sb.append(",");
		sb.append(bodyfat);
		sb.append('\n');

		// save to db file
		try {
			pw.write(sb.toString());
			pw.close();
		} catch (IOException e) {
			System.err.println("Error while saving to csv. Data not saved.");
			e.printStackTrace();
			return SAVE_FAIL;
		}

		// return success
		return SAVE_SUCCESS;
	}
}
