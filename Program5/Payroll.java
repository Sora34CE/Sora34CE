
public class Payroll {
	Payroll(int x) { // Constructor
		Employee[] e = new Employee[x];
		employees = e;
	}
	Employee[] employees; // array of x Employee objects.
	// See constructor.
	void sortEmployeesByRetirement() { /* sort employees
	in descending order of when the employee will retire. Employees
	retiring the soonest will go at the beginning of the array. */
		for (int i = 0; i < employees.length; i++) {
			for (int j = 1; j < employees.length - i; j++) {
				if (employees[j].yearsTillRetirement() < employees[j-1].yearsTillRetirement()) {
					Employee temp = employees[j-1];
					employees[j-1] = employees[j];
					employees[j] = temp;
				}
			}
		}
	}
	void sortEmployeesByCost() { /* sort employees in
	descending order of the YTDValue() of each employee.
	Employees having the lowest YTDValue() will go at the beginning
	of the array. */
		for (int i = 0; i < employees.length; i++) {
			for (int j = 1; j < employees.length - i; j++) {
				if (employees[j-1].YTDValue() < employees[j].YTDValue()) {
					Employee temp = employees[j];
					employees[j] = employees[j-1];
					employees[j-1] = temp;
				}
			}
		}
	}
	Employee[] getEmployees() { //gets the employees
		return employees;
	}
}
