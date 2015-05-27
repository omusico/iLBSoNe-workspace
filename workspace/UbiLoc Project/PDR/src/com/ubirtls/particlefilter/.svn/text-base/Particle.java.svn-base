package com.ubirtls.particlefilter;
/**
 * 粒子类 包含粒子的位置（x,y）以及每个粒子的权重 weight
 * @author 胡旭科
 *
 */
public class Particle {
	/**
	 * 粒子坐标
	 */
	private double x;
	private double y;
	/**例子权重*/
	private double weight;
	/**
	 * 构造函数
	 * @param x 粒子x坐标
	 * @param y 粒子y坐标
	 * @param w 粒子权重
	 */
	public Particle(double x, double y, double w){
		setParticle(x,y,w);
	}
	public Particle(double x, double y){
		setParticle(x,y,0);
	}
	public Particle(){}
	/**
	 * 设置粒子的值
	 * @param x 粒子x坐标
	 * @param y 粒子y坐标
	 * @param w 粒子权重
	 */
	public void setParticle(double x, double y, double w){
		setCoorXY(x,y);
		setWeight(w);
	}
	public void setCoorXY(double x,double y){
		this.x = x;
		this.y = y;
	}
	public void setWeight(double w){
		this.weight = w;
	}
	public double getX(){return x;}
	public double getY(){return y;}
	public double getWeight(){return weight;}


}
