package com.sample.checkout.pricingrules;

/**
 * 
 * @author Khanh 
 * Pricing rule for an item with quality X for price of Y
 */

public class BuyXForY implements PricingRule {

	private int baseQuantity;
	private double specialPrice;

	public BuyXForY(int baseQuantity, double specialPrice) {
		this.baseQuantity = baseQuantity;
		this.specialPrice = specialPrice;
	}

	@Override
	public double calculatePrice(int quantity, double unitPrice) {

		int d = quantity / baseQuantity;
		int r = quantity % baseQuantity;

		return d * specialPrice + r * unitPrice;
	}

}
