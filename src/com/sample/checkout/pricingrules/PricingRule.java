package com.sample.checkout.pricingrules;
/**
 * 
 * @author Khanh
 *
 */
public interface PricingRule {
	public double calculatePrice(int quantity, double unitPrice);
}
