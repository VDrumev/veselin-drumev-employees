package employeesProblem;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class EmployeesMain {

	private static List<EmployeeRecord> employeeRecords = new ArrayList<EmployeeRecord>();
	
	private static String specialDateSeparator = "$$$";
	
	public static int readFile(String ifn) {
		Path inputFile = Paths.get(ifn);
		
		try {
			BufferedReader br = Files.newBufferedReader(inputFile);
			
			String line = "";
			while( (line = br.readLine()) != null ) {
				try {
					EmployeeRecord empl = new EmployeeRecord();
					
					int pos = line.indexOf(',');
					
					String emplId = line.substring(0, pos);
					line = line.substring(pos + 1);
					pos = line.indexOf(',');
					
					String projId = line.substring(0, pos);
					line = line.substring(pos + 1);
	
					pos = line.indexOf(specialDateSeparator);
					
					Date from = null;
					Date to = null;
					Parser dateParser = new Parser();
					if ( pos > 0 ) {
						String[] dates = StringUtils.split(line, specialDateSeparator); // if we have special separator we use it for complex date formats containing spaces
						List<DateGroup> groups = dateParser.parse(dates[0]);
						from = groups.get(0).getDates().get(0);
						groups = dateParser.parse(dates[1]);
						if (groups != null) {
							to = groups.get(0).getDates().get(0);
						}
					} else {
						String[] dates = StringUtils.split(line, ','); // if no special separator then we assume that dates don't contain spaces
						List<DateGroup> groups = dateParser.parse(dates[0]);
						from = groups.get(0).getDates().get(0);
						groups = dateParser.parse(dates[1]);
						if (groups != null && groups.size() > 0) {
							to = groups.get(0).getDates().get(0);
						}
					}
					
					empl.setEmplId(emplId);
					empl.setProjectId(projId);
					empl.setFrom(from);
					empl.setTo(to);
					
					employeeRecords.add(empl);
				} catch (Exception e) {
					// possible natty exception - so we ignore the record if unparsable
										
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return employeeRecords.size();
	}
	
	public static void main(String[] args) {
		int count = readFile("./input.txt");		

		if ( count > 0 ) {
			EmployeeTimeCounter.calcEmplTimes(employeeRecords);
			System.out.println(EmployeeTimeCounter.findEmployees());
		}
	}

	public static List<EmployeeRecord> getEmployeeRecords() {
		return employeeRecords;
	}

}
