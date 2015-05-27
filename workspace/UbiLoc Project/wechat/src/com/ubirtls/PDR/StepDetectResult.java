package com.ubirtls.PDR;

public class StepDetectResult {
	/**标示是否捕获到两步 */
	public boolean isStep = true;
    /**计算的两步步长*/
	public double stepOneLength = 0.;
	public double stepTwoLength = 0.;

	/**两步经过的加速度样本个数*/
	public int stepSampleCount = 0;
	/**构造函数*/
	public StepDetectResult(boolean isStep, double stepOneLength, double stepTwoLength, int stepSampleCount){
		this.isStep = isStep;
		this.stepOneLength = stepOneLength;
		this.stepTwoLength = stepTwoLength;
		this.stepSampleCount = stepSampleCount;
	}
	public StepDetectResult(){
	}
	public void setResult(boolean isStep, double stepOneLength, double stepTwoLength, int stepSampleCount){
		this.isStep = isStep;
		this.stepOneLength = stepOneLength;
		this.stepTwoLength = stepTwoLength;
		this.stepSampleCount = stepSampleCount;
	}
}
