
public class BoardMember extends Employee{
	BoardMember(int yearsWorked) { // constructor
		super(yearsWorked);
	}
	final double YEARLY_BONUS = 20000; // $/year
	final int PAID_VACATION = 20; // days
	final int UNPAID_VACATION = 10; // days
	final int SICK_LEAVE = 10; // days
	final double HEALTH_INSURANCE = 20000;//$/year
	final double INCOME = 200000; // $/year
	int usedUnpaidVacation; // keeps track of how much
	//unpaid vacation has been used.
	int usedVacation; // keeps track of how much paid vacation
	// has been used.
	int usedSickDays; // keeps track of how many sick days
	// have been used.
	void usePaidVacation() { // adds one to usedVacation
	 //instance variable.
		usedVacation++;
	}
	void useUnpaidVacation() { // adds one to
	// usedUnpaidVacation
		usedUnpaidVacation++;
	}
	void useSickDay() { // adds one to usedSickDays
		usedSickDays++;
	}
	void workYear() { // adds one to yearsWorked.
		setYears(getYears() + 1);
	}
	double YTDValue() {/* overridden from Employee class.
	YTDValue() is calculated by taking the sum of:
	YEARLY_BONUS
	HEALTH_INSURANCE
	INCOME
	PAID_VACATION * (INCOME / 260)
	(SICK_LEAVE – usedSickDays) * ((INCOME /
	260) / 2) */
		return (YEARLY_BONUS + HEALTH_INSURANCE + INCOME + PAID_VACATION * (INCOME / 260) + (SICK_LEAVE - usedSickDays) * ((INCOME /260) / 2));
	}
	int yearsTillRetirement() { /* overridden from Employee
	class. This should be rounded up to the nearest int. The
	calculation should take place as floating point arithmetic to ensure
	precision (hint: cast as double where necessary). This number
	should not be below zero. This is calculated by: 35 –
	(yearsWorked + (usedUnpaidVacation / 260) +
	((usedPaidVacation / 260) * 2) + (usedSickDays /
	260))
	*/
		float h = (float)(35 - (getYears() + (usedUnpaidVacation / 260) + ((usedVacation / 260) * 2) + (usedSickDays / 260)));
		return (int)h;
	}
}
