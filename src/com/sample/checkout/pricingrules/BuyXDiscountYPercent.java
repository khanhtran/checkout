package com.sample.checkout.pricingrules;

/**
 * 
 * @author Khanh 
 * Pricing rule for an item with quality X then discount Y percent
 *         For example: buy 3 discount 10%
 */

public class BuyXDiscountYPercent implements PricingRule {

	private int baseQuantity;
	private double percentage;

	public BuyXDiscountYPercent(int baseQuantity, double percentage) {
		this.baseQuantity = baseQuantity;
		this.percentage = percentage;
	}

	@Override
	public double calculatePrice(int quantity, double unitPrice) {
		int d = quantity / baseQuantity;
		int r = quantity % baseQuantity;

		double price = d * baseQuantity * unitPrice;
		price = price - (percentage / 100.0) * price;
		price = price + r * unitPrice;
		return price;
	}

}
