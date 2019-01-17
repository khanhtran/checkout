package com.sample.checkout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sample.checkout.pricingrules.PricingRule;

/**
 * Implement order amount calculation with separation of concerns
 * - Separate pricing rules from items
 * - Separate calculation logic from items
 * - Implemented based on Visitor design pattern
 * - Use java reflex to load pricing rules definitions
 * @author Khanh
 *
 */
public class Main {
	private static final String FILE_UNIT_PRICE = "unit-price.txt";
	private static final String FILE_SPECIAL_PRICE = "special-price.txt";

	private Map<String, Double> unitPrices;

	public Main() throws IOException {
		unitPrices = readUnitPrice();
	}

	public static void main(String[] args) throws IOException {
		Map<String, PricingRule> pricingRules = readPricingRules();
		System.out.println(new Main().calculateOrderTotal("A", pricingRules) + " Expecting 50");
		System.out.println(new Main().calculateOrderTotal("AB", pricingRules) + " Expecting 80");
		System.out.println(new Main().calculateOrderTotal("CDBA", pricingRules) + "Expecting 115");
		System.out.println(new Main().calculateOrderTotal("AA", pricingRules) + " Expecting 100");
		System.out.println(new Main().calculateOrderTotal("AAA", pricingRules) + " Expecting 130");
		System.out.println(new Main().calculateOrderTotal("AAAA", pricingRules) + " Expecting 180");
		System.out.println(new Main().calculateOrderTotal("AAAAA", pricingRules) + " Expecting 230");
		System.out.println(new Main().calculateOrderTotal("AAAAAA", pricingRules) + " Expecting 260");
		System.out.println(new Main().calculateOrderTotal("AAB", pricingRules) + " Expecting 160");
		System.out.println(new Main().calculateOrderTotal("AAABB", pricingRules) + " Expecting 175");
		System.out.println(new Main().calculateOrderTotal("AAABBD", pricingRules) + " Expecting 190");
		System.out.println(new Main().calculateOrderTotal("DABABA", pricingRules) + " Expecting 190");
	}

	/**
	 * 
	 * @param strOrder
	 * @param pricingRules
	 * @return
	 * @throws IOException
	 */
	private Double calculateOrderTotal(String strOrder, Map<String, PricingRule> pricingRules) throws IOException {
		List<OrderItem> items = readOrder(strOrder);
		OrderTotalVisitor orderTotalVisitor = new OrderTotalVisitor();
		for (OrderItem item : items) {
			PricingRule pricingRule = pricingRules.get(item.getItemId());
			item.accept(orderTotalVisitor, pricingRule);
		}
		double orderTotal = orderTotalVisitor.getTotal();
		return orderTotal;
	}

	/**
	 * Parse order from String
	 * @param order
	 * @return
	 * @throws IOException
	 */
	private List<OrderItem> readOrder(String order) throws IOException {
		List<OrderItem> items = new ArrayList<>();
		Map<String, Integer> map = new LinkedHashMap<>();
		for (int i = 0; i < order.length(); i++) {
			char c = order.charAt(i);
			int count = map.getOrDefault(c + "", 0);
			map.put(c + "", count + 1);
		}

		for (String key : map.keySet()) {
			OrderItem item = new DefaultOrderItem(key, map.get(key), unitPrices.get(key));
			items.add(item);
		}
		return items;
	}

	private static Map<String, PricingRule> readPricingRules() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_SPECIAL_PRICE)));
		String line;
		Map<String, PricingRule> rules = new HashMap<>();
		while ((line = reader.readLine()) != null) {
			String tmp[] = line.split(",");
			if (tmp.length >= 4) {
				String itemId = tmp[0];
				PricingRule rule = createPricingRule(tmp);
				if (rule != null) {
					rules.put(itemId, rule);
				}
			}
		}
		reader.close();
		return rules;
	}

	/**
	 * Create PricingRule from definition [ItemId, ClassName, attributes...]
	 * 
	 * @param row
	 * @return
	 */
	private static PricingRule createPricingRule(String[] row) {
		String className = row[1];
		try {
			Class<?> ruleClass = Class.forName(className);
			Constructor<?> constructor = ruleClass.getConstructors()[0];
			Class<?>[] params = constructor.getParameterTypes();
			Object[] paramValues = new Object[params.length];
			for (int i = 0; i < paramValues.length; i++) {
				String str = row[i + 2];
				if ("int".equals(params[i].getTypeName())) {
					paramValues[i] = Integer.parseInt(str);
				} else if ("double".equals(params[i].getTypeName())) {
					paramValues[i] = Double.parseDouble(str);
				}
			}
			PricingRule rule = (PricingRule) constructor.newInstance(paramValues);
			return rule;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, Double> readUnitPrice() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_UNIT_PRICE)));
		String line;
		Map<String, Double> unitPrices = new HashMap<>();
		while ((line = reader.readLine()) != null) {
			String tmp[] = line.split(",");
			if (tmp.length >= 2) {
				String itemId = tmp[0];
				try {
					double unitPrice = Double.parseDouble(tmp[1]);
					unitPrices.put(itemId, unitPrice);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		reader.close();
		return unitPrices;
	}

}
