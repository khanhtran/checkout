package com.sample.checkout.pricingrules;

/**
 * 
 * @author Khanh 
 * Pricing rule for an item with quality X then get Y for free
 * For example: buy 3 get 1 free
 */

public class BuyXGetYFree implements PricingRule {

	private int baseQuantity;
	private int freeQuantity;
	
	public BuyXGetYFree(int baseQuantity, int freeQuantity) {
		this.baseQuantity = baseQuantity;
		this.freeQuantity = freeQuantity;
	}

	@Override
	public double calculatePrice(int quantity, double unitPrice) {

		int d = quantity / (baseQuantity + freeQuantity);
		int r = quantity % (baseQuantity + freeQuantity);

		return d * unitPrice*baseQuantity + r * unitPrice;
	}

}
