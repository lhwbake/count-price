package com.bake.price.entity;

public class Person {
	
	/*
	 * 用户名称
	 */
	private String name;
	
	/*
	 * 是否是会员
	 */
	private boolean isMember;
	
	/*
	 * 优惠率
	 */
	private double discountsPercent;
	
	/*
	 * 餐费总额
	 */
	private double totalPrice;
	
	/*
	 * 实际应付金额
	 */
	private double realPayable;

	public Person(String name, boolean isMember) {
		this.name = name;
		this.isMember = isMember;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMember() {
		return isMember;
	}

	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}

	public double getDiscountsPercent() {
		return discountsPercent;
	}

	public void setDiscountsPercent(double discountsPercent) {
		this.discountsPercent = discountsPercent;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getRealPayable() {
		return realPayable;
	}

	public void setRealPayable(double realPayable) {
		this.realPayable = realPayable;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", isMember=" + isMember + ", discountsPercent=" + discountsPercent
				+ ", totalPrice=" + totalPrice + ", realPayable=" + realPayable + "]";
	}
		

}
