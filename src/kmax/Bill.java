package kmax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Bill {
	static Float[] size = new Float[]{33f,35f,37f,41f,43f,47f,51f,55f,57f,59f,60f,61f,63f,66.5f,68f,69f,70f,70.5f,71f,72f,73f,74f,74.5f,78.3f,78.5f,82.3f,82.5f,83.5f,86f,86.3f,105f};
	static String[] paperStyle=new String[]{"90高强瓦楞芯纸","100高强瓦楞芯纸","120高强瓦楞芯纸","140高强瓦楞芯纸","110高强瓦楞芯纸","70高强瓦楞芯纸","105高强瓦楞芯纸","130高强瓦楞芯纸","160高强瓦楞芯纸"};
	static Random r = new Random();
	
	public static void main(String[] args) throws BiffException, IOException, RowsExceededException, WriteException {
		// TODO Auto-generated method stub
		//read the source data from xls
		Workbook rwb = null;
		String sFilePath = "./file/bill.xls"; 
		InputStream is = new FileInputStream(sFilePath);
		rwb = Workbook.getWorkbook(is);
		Sheet firstSheet = rwb.getSheet(0);
		int rows = firstSheet.getRows();//获取工作表中的总行数 
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		SimpleDateFormat dfSimple = new SimpleDateFormat("MM月dd日");
		String fileNamePart = df.format(d);
		WritableWorkbook writeBook = Workbook.createWorkbook(new File("./file/bill-"+fileNamePart+".xls")); 
		WritableSheet firstWriteSheet = writeBook.createSheet("bill", 1);
		int writeMark=0;
		Paper[] paperArray = null;
		for(int s=1;s<rows;s++){
			Cell c1 = firstSheet.getCell(0,s);			//company name
			Cell c2 = firstSheet.getCell(1,s);			//price
			Cell c3 = firstSheet.getCell(2, s);		//money
			paperArray = produceData(Float.parseFloat(c2.getContents().trim()), Float.parseFloat(c3.getContents().trim()));
			// the first row:佛山市南海蓝天鹅造纸有限公司送货单
			firstWriteSheet.mergeCells(0, writeMark, 9, writeMark);
			Label label1 = new Label(0,writeMark,"佛山市南海蓝天鹅造纸有限公司送货单");
			firstWriteSheet.addCell(label1);
			writeMark = writeMark +1;
			
			firstWriteSheet.mergeCells(0, writeMark, 9, writeMark);		
			Label label2 = new Label(0,writeMark,"地址：佛山市南海区西樵镇海舟村　　电话：86828868  86825555   传真：86815767");
			firstWriteSheet.addCell(label2);
			writeMark = writeMark +1;
			
			firstWriteSheet.mergeCells(0, writeMark, 6, writeMark);	
			firstWriteSheet.mergeCells(7, writeMark, 9, writeMark);	
			Label label3 = new Label(0,writeMark,"客户:"+c1.getContents().trim());
			firstWriteSheet.addCell(label3);
			Label label4 = new Label(7,writeMark,"日期:"+dfSimple.format(d));
			firstWriteSheet.addCell(label4);
			writeMark = writeMark +1;	
			
			firstWriteSheet.mergeCells(0, writeMark, 3, writeMark);	
			firstWriteSheet.mergeCells(4, writeMark, 5, writeMark);	
			firstWriteSheet.mergeCells(6, writeMark, 7, writeMark);	
			Label label5 = new Label(0,writeMark,"品名");
			firstWriteSheet.addCell(label5);
			Label label6 = new Label(4,writeMark,"总价数");
			firstWriteSheet.addCell(label6);
			Label label7 = new Label(6,writeMark,"总重量");
			firstWriteSheet.addCell(label7);
			Label label8 = new Label(8,writeMark,"单价");
			firstWriteSheet.addCell(label8);
			Label label9 = new Label(9,writeMark,"金额");
			firstWriteSheet.addCell(label9);
			writeMark = writeMark +1;
			
			firstWriteSheet.mergeCells(0, writeMark, 3, writeMark);	
			firstWriteSheet.mergeCells(4, writeMark, 5, writeMark);	
			firstWriteSheet.mergeCells(6, writeMark, 7, writeMark);	
			int paperStyleRandom = r.nextInt(paperStyle.length-1);
			Label label10 = new Label(0,writeMark,paperStyle[paperStyleRandom]);
			firstWriteSheet.addCell(label10);
			

			writeMark = writeMark+2;
		}
		writeBook.write();
		writeBook.close();
	}

	public static Paper[] produceData(float price, float totalMoney){
		float originalWeight = totalMoney*1000/price;   //单位kg
		System.out.println("originalWeight: "+originalWeight);
		BigDecimal   b   =   new   BigDecimal(originalWeight); 
		originalWeight =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
		//get 1-5 size from size pool in random

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
		int quantity=0;
		Paper[] paperArray = null;
		while(superTimes<10000){
			quantity=0;
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
//			System.out.println("sort arrays:");
//			for(int j=0;j<sizeSum;j++){
//				System.out.println(size[array[j]]);
//			}
			//produce object include min,max,size parameter
			paperArray = new Paper[sizeSum];
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
					System.out.println("superTimes: "+superTimes);
					for(int t=0;t<sizeSum;t++){
						if(paperArray[t].getWeight()!=null && paperArray[t].getWeight().size()>0){
							quantity = quantity + paperArray[t].getWeight().size();
							System.out.println("Size: "+paperArray[t].getSize()+"  quantity: "+paperArray[t].getWeight().size());
							Iterator it1 = paperArray[t].getWeight().iterator();
							while(it1.hasNext()){
								System.out.println(it1.next());
							}
							System.out.println("=========");
						}
					}
					System.out.println("total quantity: "+quantity);
					if(quantity<=40){
						success=1;
						superTimes=10000;
					}
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
			return paperArray;
		}
		if(success!=1){
			System.out.println("fail to get result!");
			return null;
		}
		return null;
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
