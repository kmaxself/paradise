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
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
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
			if(paperArray == null || paperArray.length==0){
				break;
			}
			//set row height
			// the first row:佛山市南海蓝天鹅造纸有限公司送货单
			WritableFont first = new WritableFont(WritableFont.createFont("宋体"),20, WritableFont.BOLD); 
			WritableCellFormat firstFormat = new WritableCellFormat(first);
			firstWriteSheet.mergeCells(0, writeMark, 9, writeMark);
			Label label1 = new Label(0,writeMark,"佛山市南海蓝天鹅造纸有限公司送货单",firstFormat);
			firstWriteSheet.addCell(label1);
			writeMark = writeMark +1;

			WritableFont font2 = new WritableFont(WritableFont.createFont("宋体"),11, WritableFont.BOLD); 
			WritableCellFormat format = new WritableCellFormat(font2);
			format.setVerticalAlignment(VerticalAlignment .CENTRE);
			firstWriteSheet.mergeCells(0, writeMark, 9, writeMark);		
			Label label2 = new Label(0,writeMark,"地址：佛山市南海区西樵镇海舟村　　电话：86828868  86825555   传真：86815767",format);
			firstWriteSheet.addCell(label2);
			writeMark = writeMark +1;

			WritableFont font3 = new WritableFont(WritableFont.createFont("宋体"),14, WritableFont.BOLD); 
			WritableCellFormat format3 = new WritableCellFormat(font3);
			format3.setVerticalAlignment(VerticalAlignment .CENTRE);
			firstWriteSheet.mergeCells(0, writeMark, 6, writeMark);	
			firstWriteSheet.mergeCells(7, writeMark, 9, writeMark);	
			Label label3 = new Label(0,writeMark,"客户:"+c1.getContents().trim(),format3);
			firstWriteSheet.addCell(label3);
			WritableCellFormat format32 = new WritableCellFormat(font3);
			format32.setVerticalAlignment(VerticalAlignment .CENTRE);
			format32.setAlignment(Alignment.RIGHT);
			Label label4 = new Label(7,writeMark,"日期:"+dfSimple.format(d),format32);
			firstWriteSheet.addCell(label4);
			writeMark = writeMark +1;	

			WritableFont font41 = new WritableFont(WritableFont.createFont("宋体"),12, WritableFont.BOLD); 
			WritableCellFormat format41 = new WritableCellFormat(font41);
			format41.setVerticalAlignment(VerticalAlignment .CENTRE);
			format41.setAlignment(Alignment.CENTRE);
			firstWriteSheet.mergeCells(0, writeMark, 3, writeMark);	
			firstWriteSheet.mergeCells(4, writeMark, 5, writeMark);	
			firstWriteSheet.mergeCells(6, writeMark, 7, writeMark);	
			Label label5 = new Label(0,writeMark,"品名",format41);
			firstWriteSheet.addCell(label5);
			Label label6 = new Label(4,writeMark,"总件数",format41);
			firstWriteSheet.addCell(label6);
			Label label7 = new Label(6,writeMark,"总重量",format41);
			firstWriteSheet.addCell(label7);
			Label label8 = new Label(8,writeMark,"单价",format41);
			firstWriteSheet.addCell(label8);
			Label label9 = new Label(9,writeMark,"金额",format41);
			firstWriteSheet.addCell(label9);
			writeMark = writeMark +1;

			WritableFont font51 = new WritableFont(WritableFont.createFont("宋体"),11, WritableFont.BOLD); 
			WritableCellFormat format51 = new WritableCellFormat(font51);
			format51.setVerticalAlignment(VerticalAlignment .CENTRE);
			format51.setAlignment(Alignment.CENTRE);
			format51.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			firstWriteSheet.mergeCells(0, writeMark, 3, writeMark);	
			firstWriteSheet.mergeCells(4, writeMark, 5, writeMark);	
			firstWriteSheet.mergeCells(6, writeMark, 7, writeMark);	
			int paperStyleRandom = r.nextInt(paperStyle.length-1);
			Label label10 = new Label(0,writeMark,paperStyle[paperStyleRandom],format51);
			firstWriteSheet.addCell(label10);
			Label label11 = new Label(4,writeMark,""+countArrayElement(paperArray),format51);
			firstWriteSheet.addCell(label11);
			Float money = Float.parseFloat(c3.getContents());
			Float price = Float.parseFloat(c2.getContents());
			Float weightTotal = money*1000/price;
			BigDecimal   b   =   new   BigDecimal(weightTotal); 
			weightTotal =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
			Label label12 = new Label(6,writeMark,""+weightTotal,format51);
			firstWriteSheet.addCell(label12);
			Label label13 = new Label(8,writeMark,c2.getContents(),format51);
			firstWriteSheet.addCell(label13);
			Label label14 = new Label(9,writeMark,c3.getContents(),format51);
			firstWriteSheet.addCell(label14);
			writeMark = writeMark+1;
			
			//the important thing,put out the array
			List weightList = null;
			int colMark=0;
			//add border
			for(int rowBorder = writeMark; rowBorder<writeMark+5; rowBorder++){
				for(int c=0;c<10;c++){
					firstWriteSheet.setColumnView(c,12);
					Label labelAll = new Label(c,rowBorder,"",format51);
					firstWriteSheet.addCell(labelAll);
				}
			}
			for(int t=0; t<paperArray.length; t++){
				weightList = paperArray[t].getWeight();
				int weightSize = weightList.size();
				if(weightList !=null && weightSize != 0){
					float sizeTemp = paperArray[t].size;
					Label labelCol = null;
					if(sizeTemp%1==0){
						labelCol = new Label(colMark,writeMark,(int)sizeTemp+"",format51);
					}else{
						labelCol = new Label(colMark,writeMark,sizeTemp+"",format51);
					}
					firstWriteSheet.addCell(labelCol);
					
					Iterator iter = weightList.iterator();
					int rowMark = writeMark+1;
					int rowMod = 0;
					while(iter.hasNext()){
						Float temp = (Float) iter.next();
						Label label = new Label(colMark,rowMark,temp.intValue()+"",format51);
						firstWriteSheet.addCell(label);
						rowMod++;
						rowMark = rowMark+1;
						if(rowMod%4 == 0){
							colMark++;
							rowMark = writeMark+1;
						}
					}
					if(rowMark != writeMark+4 ){
						colMark++;
					}
				}
			}

			writeMark = writeMark+5;
			WritableCellFormat format111 = new WritableCellFormat(font51);
			format111.setVerticalAlignment(VerticalAlignment .CENTRE);
			Label label15 = new Label(0,writeMark,"发货人：琼",format111);
			firstWriteSheet.addCell(label15);
			Label label16 = new Label(4,writeMark,"车号：",format111);
			firstWriteSheet.addCell(label16);
			Label label17 = new Label(7,writeMark,"签收单位：",format111);
			firstWriteSheet.addCell(label17);
			writeMark = writeMark+1;
			
			WritableCellFormat format121 = new WritableCellFormat(font51);
			format121.setVerticalAlignment(VerticalAlignment .CENTRE);
			firstWriteSheet.mergeCells(0, writeMark, 6, writeMark);	
			Label label18 = new Label(0,writeMark,"(1)存根(白)　(2)收款(红)　(3)收货仓库记帐(蓝)　(4)财务(黄)",format121);
			firstWriteSheet.addCell(label18);
			
			writeMark = writeMark+3;
			for(int m=0; m<writeMark;m++){
				firstWriteSheet.setRowView(m,555,false);
			}
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
		Paper[] paperArray = null;
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
					if(countArrayElement(paperArray)<=28){
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

	public static int countArrayElement(Paper[] paperArray){
		int quantity= 0;
		if(paperArray == null || paperArray.length == 0){
			return quantity;
		}
		for(int t=0;t<paperArray.length;t++){
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
		return quantity;
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
