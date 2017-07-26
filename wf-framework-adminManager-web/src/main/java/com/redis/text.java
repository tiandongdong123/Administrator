package com.redis;

public class text {

	public static void main(String[] args) {
		String [] a = new String[]{"1","2","3"};
		String [] b = new String[]{"1","2","3","4"};
		
		for(String bx : b){
			int x = 0;
			for(String ax:a){
				if(ax==bx){
					x=1;
				}
			}
			
			if(x==1){
				System.out.println(bx+"check");
			}else{
				System.out.println(bx);
			}
		}
	}
}
