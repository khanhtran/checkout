package com.sample.checkout;

import com.sample.checkout.pricingrules.PricingRule;

/**
 * 
 * @author Khanh
 *
 */
public class OrderTotalVisitor implements Visitor {

	private double total;

	public OrderTotalVisitor() {
		total = 0;
	}

	public double getTotal() {
		return total;
	}

	@Override
	public void visit(DefaultOrderItem orderItem, PricingRule pricingRule) {
		int quantity = orderItem.getQuantity();
		double price = orderItem.getUnitPrice();
		if (pricingRule == null) {
			total += quantity * price;
		} else {
			total += pricingRule.calculatePrice(quantity, price);
		}
	}
	
}
