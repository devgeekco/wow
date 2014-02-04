package devgeek.expense.algo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Ankit Singh
 * Copyright DevGeeks Lab 2014-15
 *
 */
public class GroupDistAlgo {

	private  HashMap<String, BigDecimal> groupUsers; 
	private BigDecimal totalExpense = new BigDecimal(0);
	private BigDecimal avgExpense = new BigDecimal(0);

	public GroupDistAlgo() {
		super();
		groupUsers  = new HashMap<String, BigDecimal>();
		populateData();
	}

	/**
	 * Distribution algorithm.
	 */
	public void decisionMaker() {
		//HashMap<String, BigDecimal> diffAvg = new HashMap<String, BigDecimal>();
		HashMap<String, BigDecimal> positiveUsers = new HashMap<String, BigDecimal>();; // who paid more than average or nothing to pay
		HashMap<String, BigDecimal> negativeUsers = new HashMap<String, BigDecimal>();; // who paid less than average

		BigDecimal tempCalc = new BigDecimal(0);

		// STEP 1: Get difference of each users expense from Average and separate them in positive and negative
		for(Entry<String, BigDecimal> getIndiExp : groupUsers.entrySet()) {
			tempCalc = getIndiExp.getValue().subtract(avgExpense);

			if (tempCalc.compareTo(BigDecimal.ZERO) > 0 || tempCalc.compareTo(BigDecimal.ZERO) == 0 ) {
				positiveUsers.put(getIndiExp.getKey(), tempCalc);
			} else
				negativeUsers.put(getIndiExp.getKey(), tempCalc);
		}

	//	System.out.println("\n## Positive Entry Below:");	
		//printHashMap(positiveUsers);
		//System.out.println("\n## Negative Entry Below:");
		//printHashMap(negativeUsers);

		// STEP 2: Sort

		negativeUsers = sortByValue(negativeUsers, 1); // 1: Decreasing order
		//System.out.println("\n## Negative Sorted Entry Below:");	
		//printHashMap(negativeUsers);

		positiveUsers = sortByValue(positiveUsers,0); // 0: Increasing order
		//System.out.println("\n## Positive Sorted Entry Below:");	
		//printHashMap(positiveUsers);

		System.out.println("\n# Generating Report on who owes who?\n ");
		generateReport(positiveUsers, negativeUsers);
		/*
		for (Entry<String, BigDecimal> entry  : entriesSortedByValues(negativeUsers)) {
			System.out.println(entry.getKey()+":"+entry.getValue());
		}*/
	}

	/**
	 * Core Algorithm for finding who owes money to other travel partners for 
	 * joint expenditure in the tour.
	 * @param positiveUsers
	 * @param negativeUsers
	 * @return
	 */
	private boolean generateReport(HashMap<String, BigDecimal> positiveUsers,
			HashMap<String, BigDecimal> negativeUsers) {

		if(positiveUsers.size() == 0 || negativeUsers.size() == 0)
			return true;	

		Iterator<Map.Entry<String, BigDecimal>> posEntryIt = null;
		Entry<String, BigDecimal> posEntry = null;

		if(positiveUsers.entrySet().iterator().hasNext()) {
			posEntryIt= positiveUsers.entrySet().iterator();
			posEntry = posEntryIt.next();
		}

		Iterator<Map.Entry<String, BigDecimal>> negEntryIt = null;
		Entry<String, BigDecimal> negEntry = null;

		if(negativeUsers.entrySet().iterator().hasNext()) {
			negEntryIt= negativeUsers.entrySet().iterator();
			negEntry = negEntryIt.next();
		}

		//System.out.println("# Number of Entries in PosEntry: "+positiveUsers.size()+ " NegEntry: "+negativeUsers.size());

		BigDecimal tempResult = posEntry.getValue().add(negEntry.getValue());

		//System.out.println("# Temp RESULT::: "+tempResult);

		if(tempResult.compareTo(BigDecimal.ZERO) == 0) {
			System.out.println(negEntry.getKey()+" OWES "+posEntry.getKey()+" --> "+posEntry.getValue());
			posEntryIt.remove(); negEntryIt.remove();
		} else if (tempResult.compareTo(BigDecimal.ZERO) > 0) {
			System.out.println(negEntry.getKey()+" OWES "+posEntry.getKey()+" --> "+negEntry.getValue().abs());
			positiveUsers.put(posEntry.getKey(), tempResult); negEntryIt.remove();
		} else if (tempResult.compareTo(BigDecimal.ZERO) < 0) {
			System.out.println(negEntry.getKey()+" OWES "+posEntry.getKey()+" --> "+posEntry.getValue());
			posEntryIt.remove(); negativeUsers.put(negEntry.getKey(), tempResult);
		}

		generateReport(positiveUsers, negativeUsers);

		return true;

	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, BigDecimal> sortByValue(HashMap<String, BigDecimal> map, final int order) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				if (order == 0)
					return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
				else
					return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		HashMap result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/*

	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K, V>> entriesSortedByValues(HashMap<K,V> map) {
		SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
				new Comparator<Map.Entry<K,V>>() {
					@Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
						return e1.getValue().compareTo(e2.getValue());
					}
				}
		);
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}*/

	private void calcTotalExpense() {
		for (BigDecimal indExp : groupUsers.values()) {
			//	System.out.println("Values: "+indExp.toString());
			totalExpense = totalExpense.add(indExp);
		}

//		System.out.println("## Total Expense: "+totalExpense.toString()+" Number of Group Members: "+groupUsers.size());

		avgExpense = totalExpense.divide(new BigDecimal(groupUsers.size()), 2, RoundingMode.HALF_UP);

		//System.out.println("## Avg. Expense: "+avgExpense);
	}

	private void printHashMap(HashMap<String, BigDecimal> hm) {
		//System.out.println("\n=== DEBUG ==== ");
		for(Entry<String, BigDecimal> entry : hm.entrySet()) {		
			System.out.println("## Key:: "+entry.getKey()+"        ## Value:: "+entry.getValue().toString());
		}
	}

	/**
	 * Populate dummy data
	 */
	private void populateData() {
		groupUsers.put("Jamesbond", new BigDecimal(2480));
		groupUsers.put("Supercommando Dhruv", new BigDecimal(100));
		groupUsers.put("Nagraj", new BigDecimal(45));
		groupUsers.put("Superwoman", new BigDecimal(20.2));
		groupUsers.put("Superman", new BigDecimal(222.23));
		groupUsers.put("Mogambo", new BigDecimal(1120));
		groupUsers.put("MrIndia", new BigDecimal(127.5));
		groupUsers.put("Obama", new BigDecimal(910));
		groupUsers.put("Modi", new BigDecimal(3251));

		calcTotalExpense();	
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GroupDistAlgo gda = new GroupDistAlgo();
		gda.decisionMaker();
	}

}
