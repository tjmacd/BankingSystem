package helpers;

import java.util.Comparator;

class IdComparator implements Comparator<Accounts> {

	@Override
	public int compare(Accounts a1, Accounts a2) {
		if(a1.number < a2.number) {
			return -1;
		} else if(a1.number > a2.number) {
			return 1;
		}
		return 0;
	}

}
