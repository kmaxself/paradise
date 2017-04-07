package kmax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Bill {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Float[] size = new Float[]{33f,35f,37f,41f,43f,47f,51f,55f,57f,59f,60f,61f,63f,66.5f,68f,69f,70f,70.5f,71f,72f,73f,74f,74.5f,78.5f,82.5f,83.5f,86f,105f};
		String[] paperStyle=new String[]{"90高强瓦楞芯纸","100高强瓦楞芯纸","120高强瓦楞芯纸","140高强瓦楞芯纸","110高强瓦楞芯纸","70高强瓦楞芯纸","105高强瓦楞芯纸","130高强瓦楞芯纸","160高强瓦楞芯纸"};
		float totalMoney = 200000f;   //单位 元
		float price = 2400f;  //单位 元/t
		float originalWeight = totalMoney*1000/price;   //单位kg
		System.out.println("originalWeight: "+originalWeight);
		BigDecimal   b   =   new   BigDecimal(originalWeight); 
		originalWeight =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
		//get 1-5 size from size pool in random
		Random r = new Random();
		int sizeLength = size.length;
		int sizeSum = r.nextInt(4)+1;
		int array[]=new int[sizeSum];
		int temp=0;
		float MIN;
		float MAX;
		Float orginalSize;
		int success=0;
		int superTimes = 0;
		//super circulation time<100,so if we can't get result using 10000 times,we will give up the data
		while(superTimes<10000){
			//random to produce size array
			for(int i=0;i<sizeSum;){
				temp=r.nextInt(sizeLength-2)+1;
				//temp not in array
				if(judgeDataInArray(array,temp)){
					array[i]=temp;
					i++;
				}
			}

			Arrays.sort(array);
			System.out.println("sort arrays:");
			for(int j=0;j<sizeSum;j++){
				System.out.println(size[array[j]]);
			}
			//produce object include min,max,size parameter
			Paper[] paperArray = new Paper[sizeSum];
			for(int j=0;j<sizeSum;j++){
				orginalSize = size[array[j]];
				MIN =22*orginalSize-150;
				MAX = 22*orginalSize+150;
				paperArray[j] = new Paper(MIN,MAX,orginalSize);
			}
			//
			float weight=originalWeight;
			int mark=0;
			//middle circulation time <100
			while(true){
				//judge if the request in anyone paper scale
				int result = judgeDataInPaper(paperArray,weight);
				if(result==1){
					success=1;
					System.out.println("superTimes: "+superTimes);
					for(int t=0;t<sizeSum;t++){
						if(paperArray[t].getWeight()!=null && paperArray[t].getWeight().size()>0){
							System.out.println("Size: "+paperArray[t].getSize());
							Iterator it1 = paperArray[t].getWeight().iterator();
							while(it1.hasNext()){
								System.out.println(it1.next());
							}
							System.out.println("=========");
						}
					}
					superTimes=10000;
					break;
				}
				if(result==-1){
					break;
				}
				//random produce one weight
				if(paperArray[mark].getMax()<weight){
					float weightTemp = paperArray[mark].getMin()+r.nextInt(300);
					paperArray[mark].getWeight().add(weightTemp);
					weight=weight-weightTemp;
				}
				mark=(mark+1)%sizeSum;
			}
			superTimes++;
		}
		//if process get the result,put it
		if(success==1){
			//
		}
		if(success!=1){
			System.out.println("fail to get result!");
		}
	}

	public static  boolean judgeDataInArray(int a[],int e){
		if(a==null) return false;
		for(int i=0;i<a.length;i++){
			if(a[i]==e) return false;
		}
		return true;
	}

	public static  int judgeDataInPaper(Paper a[],float weight){
		if(a==null) return -1;   //-1  it mean can't get result;
		int belowMark=0;
		for(int i=0;i<a.length;i++){
			if(a[i].getMin()<=weight && a[i].getMax()>=weight){
				a[i].getWeight().add(weight);
				return 1;
			}
			if(a[i].getMin()<=weight){
				belowMark=1;
			}
		}
		if(belowMark==0){
			return -1;
		}
		return 0;
	}
}

class Paper{
	float min;
	float max;
	float size;
	List weight=new ArrayList<Float>();

	public Paper(float min, float max, float size) {
		super();
		this.min = min;
		this.max = max;
		this.size = size;
	}
	public float getMin() {
		return min;
	}
	public void setMin(float min) {
		this.min = min;
	}
	public float getMax() {
		return max;
	}
	public void setMax(float max) {
		this.max = max;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public List getWeight() {
		return weight;
	}
	public void setWeight(List weight) {
		this.weight = weight;
	}


}
