package com.sample.checkout;

import com.sample.checkout.pricingrules.PricingRule;

/**
 * 
 * @author Khanh
 *
 */
public class DefaultOrderItem implements OrderItem {
	private String itemId;
	private int quantity;
	private double unitPrice;

	public DefaultOrderItem(String itemId, int quantity, double unitPrice) {
		this.itemId = itemId;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
	}

	@Override
	public void accept(Visitor visitor, PricingRule pricingRule) {
		visitor.visit(this, pricingRule);
	}

	@Override
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
}
