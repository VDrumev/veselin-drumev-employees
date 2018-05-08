package employeesProblem;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;

import java.awt.Font;
import java.awt.Color;

public class EmployeesMainWindow {

	private JFrame frame;
	private JTextField textFileName;
	private JLabel lblEmployeesresult;
	private JTable tableResults;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeesMainWindow window = new EmployeesMainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EmployeesMainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		panel = new JPanel();
		frame.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 30, 50, 30};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblChooseFileTextBox = new JLabel("Choose a file:");
		GridBagConstraints gbc_lblChooseFileTextBox = new GridBagConstraints();
		gbc_lblChooseFileTextBox.anchor = GridBagConstraints.WEST;
		gbc_lblChooseFileTextBox.insets = new Insets(0, 0, 5, 5);
		gbc_lblChooseFileTextBox.gridx = 0;
		gbc_lblChooseFileTextBox.gridy = 0;
		panel.add(lblChooseFileTextBox, gbc_lblChooseFileTextBox);
		
		textFileName = new JTextField();
		GridBagConstraints gbc_textFileName = new GridBagConstraints();
		gbc_textFileName.insets = new Insets(0, 0, 5, 5);
		gbc_textFileName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFileName.gridx = 1;
		gbc_textFileName.gridy = 0;
		panel.add(textFileName, gbc_textFileName);
		textFileName.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fc = new JFileChooser();
				
				fc.setCurrentDirectory(Paths.get(".").toFile());
				fc.setDialogTitle("Choose an input file");
				fc.setMultiSelectionEnabled(false);
				if ( fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ){
					Path input = Paths.get(fc.getSelectedFile().getAbsolutePath());
					if ( !Files.isReadable(input)) {
						JOptionPane.showMessageDialog(frame, "The chosen file is unreadable or a directory. \nPlease choose a propper input.");
					} else {
						textFileName.setText(fc.getSelectedFile().getAbsolutePath());
					}
				}
			}
		});
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 0);
		gbc_btnBrowse.gridx = 2;
		gbc_btnBrowse.gridy = 0;
		panel.add(btnBrowse, gbc_btnBrowse);
		
		JButton btnRunChecks = new JButton("Run Checks");
		btnRunChecks.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if ( !textFileName.getText().isEmpty() ) {
					int count = EmployeesMain.readFile(textFileName.getText());
					
					if ( count > 0 ) {
						EmployeeTimeCounter.calcEmplTimes(EmployeesMain.getEmployeeRecords());
						String result = EmployeeTimeCounter.findEmployees();
						lblEmployeesresult.setText(result);

						Map<String, Long> projTimes = EmployeeTimeCounter.getProjectTimes();
						
						DefaultTableModel tableModel = new DefaultTableModel(new String[] {
								"Employee 1", "Employee 2", "Project", "Time on project"
							}, 0);

						for( String key : projTimes.keySet() ) {
							String[] split = StringUtils.split(key, EmployeeTimeCounter.SEPARATOR);
							
							String[] row = { split[0], split[1], split[2], projTimes.get(key).toString() };
							
							tableModel.addRow(row);
						}

						tableResults = new JTable(tableModel);
						JScrollPane scrollPane = new JScrollPane(tableResults);
						tableResults.setFillsViewportHeight(true);
						GridBagConstraints gbc_table = new GridBagConstraints();
						gbc_table.gridwidth = 3;
						gbc_table.insets = new Insets(0, 0, 0, 5);
						gbc_table.fill = GridBagConstraints.BOTH;
						gbc_table.gridx = 0;
						gbc_table.gridy = 3;
						panel.add(scrollPane, gbc_table);
					}
				}
			}
		});
		GridBagConstraints gbc_btnRunChecks = new GridBagConstraints();
		gbc_btnRunChecks.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRunChecks.gridwidth = 3;
		gbc_btnRunChecks.insets = new Insets(0, 0, 5, 0);
		gbc_btnRunChecks.gridx = 0;
		gbc_btnRunChecks.gridy = 1;
		panel.add(btnRunChecks, gbc_btnRunChecks);
		
		lblEmployeesresult = new JLabel("EmployeesResult");
		lblEmployeesresult.setForeground(new Color(0, 128, 0));
		lblEmployeesresult.setFont(new Font("Tahoma", Font.BOLD, 13));
		GridBagConstraints gbc_lblEmployeesresult = new GridBagConstraints();
		gbc_lblEmployeesresult.gridwidth = 3;
		gbc_lblEmployeesresult.insets = new Insets(0, 0, 5, 0);
		gbc_lblEmployeesresult.gridx = 0;
		gbc_lblEmployeesresult.gridy = 2;
		panel.add(lblEmployeesresult, gbc_lblEmployeesresult);

	}

}
