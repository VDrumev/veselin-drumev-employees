package employeesProblem;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class EmployeeTimeCounter {

	private static Map<String, Long> projectTimes = new TreeMap<String, Long>();
	public final static String separator = "|_|";
	
	public static Map<String, Long> getProjectTimes() {
		return projectTimes;
	}

	private static void addEmployeesTime(String emp1, String emp2, String proj, Long time){
		String key;
		if ( emp1.compareTo(emp2) > 0 ) {
			key = emp1 + separator + emp2 + separator + proj;
		} else {
			key = emp2 + separator + emp1 + separator + proj;
		}
		
		Long curTime = 0l;
		if ( projectTimes.containsKey(key) ) {
			curTime = projectTimes.get(key);
		}
		projectTimes.put(key, time + curTime);
	}
	
	private static Long calcIntersection(EmployeeRecord emp1, EmployeeRecord emp2) {
		if ( !( emp1.getFrom().after(emp2.getTo()) || emp2.getFrom().after(emp1.getTo()) ) ) {
			LocalDate fromIntersection;
			LocalDate toIntersection;
			if ( emp1.getFrom().after(emp2.getFrom()) ){
				fromIntersection = new LocalDate(emp1.getFrom());
			} else {
				fromIntersection = new LocalDate(emp2.getFrom());
			}
			
			if ( emp1.getTo().before(emp2.getTo()) ){
				toIntersection = new LocalDate(emp1.getTo());
			} else {
				toIntersection = new LocalDate(emp2.getTo());
			}
			
			Long result = Days.daysBetween(fromIntersection, toIntersection).getDays() + 1l;
			
			return result;
		}
		
		return 0l; // if no intersection then no time spent together
	}
	
	public static boolean calcEmplTimes(List<EmployeeRecord> employeeData) {
		
		if ( employeeData == null || employeeData.size() < 1 ) return false;
		
		for ( int i = 0; i < employeeData.size() - 1; i++ ) {
			EmployeeRecord emp1 = employeeData.get(i);
			for ( int j = i+1; j < employeeData.size(); j++ ) {
				EmployeeRecord emp2 = employeeData.get(j);
				
				// we do this only if they are different people
				//if empl1 had worked on a project stopped then started to work again we will have 2 records for empl1
				if ( !emp1.getEmplId().equals(emp2.getEmplId()) 
						&& emp1.getProjectId().equals(emp2.getProjectId()) ) {
					addEmployeesTime(emp1.getEmplId(), emp2.getEmplId(), emp1.getProjectId(), calcIntersection(emp1, emp2));
				}
			}
		}
		
		return true;
	}
	
	public static String findEmployees() {
		String result = "";
		if ( projectTimes.isEmpty() ) {
		    return "No employees have worked together on a project.";
		}
		
		Map<String, Long> emplData = new HashMap<String, Long>();
		int i = 0;
		Iterator<String> iter = projectTimes.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			String[] data = StringUtils.split(key, separator);
			Long currTime = 0l;
			String newKey = data[0] + separator + data[1];
			
			if ( emplData.containsKey(newKey) ) {
				currTime = emplData.get(newKey);
			}
			
			emplData.put( newKey, currTime + projectTimes.get(key) );
		}
		
		String emplKey = "";
		Long time = 0l;
		for ( String key : emplData.keySet() ) {
			if ( time < emplData.get(key) ) {
				emplKey = key;
				time = emplData.get(key);
			}
		}
		
		String[] data = StringUtils.split( emplKey, separator);
		result = "Employee1: " + data[0] + " Employee2: " + data[1] + " worked together on projects " + time + " days.";
		
		return result;
	}
}
