package com.sample.checkout;

import com.sample.checkout.pricingrules.PricingRule;

/**
 * 
 * @author Khanh
 *
 */
public interface Visitor {
	public void visit(DefaultOrderItem orderItem, PricingRule pricingRule);
}
