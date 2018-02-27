package com.bake.price.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bake.price.entity.Person;
import com.bake.price.entity.Product;
import com.bake.price.entity.ProductOrder;

public class CountService {
	
	private static final Logger logger = LoggerFactory.getLogger(CountService.class);
	
	/*
	 * 订餐人数
	 */
	private final BigDecimal personNum;
	
	/*
	 * 配送费
	 */
	private final BigDecimal distributionCosts;
	
	/*
	 * 每个人应付的平均配送费
	 */
	private final BigDecimal aveDistributionCosts;
	
	/*
	 * 会员优惠
	 */
	private final BigDecimal memberDiscounts;
	
	/*
	 * 在线立减优惠
	 */
	private final BigDecimal onlineDiscounts;
	
	/*
	 *折 
	 */
	private final BigDecimal discounts;
	
	/*
	 * 优惠比例浮动
	 */
	private BigDecimal increase = new BigDecimal(0);
	
	/*
	 * 修正因子
	 */
	private final BigDecimal correctionFactors;
	
	public CountService(int personNum, double distributionCosts, double memberDiscounts, double onlineDiscounts, double discounts, double correctionFactors) {
		this.personNum = new BigDecimal(personNum);
		this.distributionCosts = new BigDecimal(distributionCosts);
		this.memberDiscounts = new BigDecimal(memberDiscounts);
		this.onlineDiscounts = new BigDecimal(onlineDiscounts);
		this.discounts = new BigDecimal(discounts);
		this.aveDistributionCosts = this.distributionCosts.divide(this.personNum, 5, BigDecimal.ROUND_HALF_UP);
		this.correctionFactors = new BigDecimal(correctionFactors);
				
	}
	
	public CountService(int personNum, double distributionCosts, double onlineDiscounts, double discounts) {
		this.personNum = new BigDecimal(personNum);
		this.distributionCosts = new BigDecimal(distributionCosts);
		this.memberDiscounts = new BigDecimal(0);
		this.onlineDiscounts = new BigDecimal(onlineDiscounts);
		this.discounts = new BigDecimal(discounts);
		this.aveDistributionCosts = this.distributionCosts.divide(this.personNum, 5, BigDecimal.ROUND_HALF_UP);
		this.correctionFactors = new BigDecimal(1);
				
	}
	
	public Map<String,Double> count(Map<String, Person> persons, Map<String, Product> products, List<ProductOrder> productOrders) {
				
		double totalPrice = 0;
		for(ProductOrder productOrder : productOrders) {
			logger.info("订单(含餐盒费):{}", productOrder);
			
			/*所有人餐费总价(不包括配送费)*/
			totalPrice += productOrder.getTotalPrice();
		}
		BigDecimal bdTotalPrice = new BigDecimal(totalPrice);
		logger.info("所有人餐费总价-{}", totalPrice);
		logger.info("配送费-{}元", this.distributionCosts);
		logger.info("总优惠-{}元", this.onlineDiscounts.doubleValue());
		
		/*每个人的总价*/
		getTotalPrice(productOrders);
		
		/*每个人的优惠率*/
		getDiscountsPercent(persons, bdTotalPrice);
		
		/*每个人实际应付总价*/
		getSum(persons);
		
		Map<String,Double> result = new HashMap<String, Double>(3);
		double total = 0;
		for(Person person : persons.values()) {
			result.put(person.getName(), person.getRealPayable());
			total += person.getRealPayable();
		}
		result.put("total", total);
		
		return result;
	}
	
	/**
	 * 计算每个人实际应付的餐费金额
	 * @param person
	 * @return
	 */
	
	public synchronized void getSum(Map<String, Person> persons) {
		
		for(Person person : persons.values()) {
			BigDecimal realPayable = new BigDecimal(person.getTotalPrice()).subtract(this.onlineDiscounts.add(this.discounts).add(this.memberDiscounts).multiply(new BigDecimal(person.getDiscountsPercent()))).add(this.aveDistributionCosts);
			person.setRealPayable(realPayable.doubleValue());
			logger.info("{}实际应付金额:{}元", person.getName(),person.getRealPayable());
		}
	}
	
	/**
	 * 计算每个人的优惠率
	 * @param person
	 * @param bdTotalPrice
	 * @return
	 */
	private synchronized void getDiscountsPercent(Map<String, Person> persons, BigDecimal bdTotalPrice ) {
		
		for(Person person : persons.values()){
			BigDecimal memberDiscountsPercent = this.memberDiscounts.divide(this.memberDiscounts.add(this.onlineDiscounts).add(this.discounts), 5, BigDecimal.ROUND_HALF_UP);
			BigDecimal discountsPercent = new BigDecimal(person.getTotalPrice()).divide(bdTotalPrice, 5, BigDecimal.ROUND_HALF_UP);
			
			if(person.isMember()) {
				//增加的比例，修正因子
				this.increase = discountsPercent.multiply(memberDiscountsPercent).multiply(this.correctionFactors);
				discountsPercent = discountsPercent.add(this.increase);
			}else{
				discountsPercent = discountsPercent.subtract(this.increase.divide(this.personNum.subtract(new BigDecimal(1)), 5, BigDecimal.ROUND_HALF_UP));
			}
			logger.info("修正因子-{}", this.increase);
			person.setDiscountsPercent(discountsPercent.doubleValue());
			logger.info(person.getName()+"优惠率-{}", discountsPercent.doubleValue());
		}
	}
	
	/**
	 * 求每个人的总价 
	 * @param order
	 * @return
	 */
	private synchronized void getTotalPrice(List<ProductOrder> orders) {
		for(ProductOrder order : orders){
			BigDecimal totalPrice = new BigDecimal(order.getTotalPrice());
			Person person = order.getPerson();
			person.setTotalPrice(person.getTotalPrice()+totalPrice.doubleValue());	
			logger.info("{}的总价:{}元,", person.getName(), person.getTotalPrice());
		}
	}

}
