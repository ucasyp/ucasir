package ucas.ir.action;

import java.util.Comparator;
import ucas.ir.pojo.*;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
class SortByTime implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		News n1=(News) o1;
		News n2=(News) o2;
		
		return n2.getTime().compareTo(n1.getTime());
	}
	
}
