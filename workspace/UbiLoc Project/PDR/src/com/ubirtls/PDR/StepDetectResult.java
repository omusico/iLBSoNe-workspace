package com.ubirtls.PDR;

public class StepDetectResult {
	/**��ʾ�Ƿ񲶻����� */
	public boolean isStep = true;
    /**�������������*/
	public double stepOneLength = 0.;
	public double stepTwoLength = 0.;

	/**���������ļ��ٶ���������*/
	public int stepSampleCount = 0;
	/**���캯��*/
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