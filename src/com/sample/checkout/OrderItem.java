package com.sample.checkout;

import com.sample.checkout.pricingrules.PricingRule;

/**
 * 
 * @author Khanh
 *
 */
public interface OrderItem {
	public void accept(Visitor visitor, PricingRule pricingRule);
	public String getItemId();
}
