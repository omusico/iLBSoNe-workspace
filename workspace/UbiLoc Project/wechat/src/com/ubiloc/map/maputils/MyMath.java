// Created by plusminus on 20:36:01 - 26.09.2008
package com.ubiloc.map.maputils;

public class MyMath {
	
	public static int mod(int number, final int modulus){
		if(number > 0)
			return number % modulus;
		
		while(number < 0)
			number += modulus;
		
		return number;
	}

	}
