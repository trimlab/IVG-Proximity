package Manager;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;

import com.leapmotion.leap.Vector;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

public class RecordManager {
	private String name;
	private boolean enabled = false;
	private WritableWorkbook workbook = null;
	private WritableSheet sheet = null;
	private int sheetRow = 2;
	private Lock recordLock;
	
	public RecordManager(String name, Lock recordLock){
		this.name = name;
		this.recordLock = recordLock;
		try {
			workbook = Workbook.createWorkbook(new File("output/" + name + ".xls"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	
	//Add new sheet with given name. Used for each task.
	public void addSheet(String sheetName){
		recordLock.lock();
		try{
			sheet = workbook.createSheet(sheetName, 0);//workbook.getSheets().length - 1);
			sheetRow = 2;
			
		    WritableCellFormat header = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.SINGLE));
		    header.setAlignment(Alignment.CENTRE);
		    WritableCellFormat title = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false, UnderlineStyle.SINGLE));
		    title.setAlignment(Alignment.CENTRE);
		    
		    CellView cv = new CellView();
		    cv.setFormat(header);

		    //merge for title
		    sheet.mergeCells(0, 0, 5, 0);
		    sheet.addCell(new Label(0, 0, sheetName, title));
		    
		    //Set up headers
		    sheet.setColumnView(0, 30);
		    sheet.setColumnView(1, 15);
		    sheet.setColumnView(2, 15);
		    sheet.setColumnView(3, 15);
		    sheet.setColumnView(4, 15);
		    sheet.setColumnView(5, 15);
		    sheet.setColumnView(6, 15);
		    
		    sheet.addCell(new Label(0, 1, "Timestamp", header));
		    sheet.addCell(new Label(1, 1, "Leap X", header));
		    sheet.addCell(new Label(2, 1, "Leap Y", header));
		    sheet.addCell(new Label(3, 1, "Leap Z", header));
		    sheet.addCell(new Label(4, 1, "Screen X", header));
		    sheet.addCell(new Label(5, 1, "Screen Y", header));
		    sheet.addCell(new Label(6, 1, "Clicks", header));
		    sheet.addCell(new Label(8, 8, "Start:", header));
		    sheet.addCell(new Label(8, 9, "End:", header));
		    sheet.addCell(new Label(8, 11, "Clicks:", header));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		recordLock.unlock();
	}
	
	public void addRecord(Vector v, Point p){
		if(sheet == null || !enabled) return;
		
		recordLock.lock();
		try {
			java.util.Date date= new java.util.Date();
			sheet.addCell(new Label(0, sheetRow, "" + new Timestamp(date.getTime())));
			//if we are initializing a sheet, mag will be 0
			if(v.magnitude() != 0){
				sheet.addCell(new Label(1, sheetRow, "" + v.getX()));
				sheet.addCell(new Label(2, sheetRow, "" + v.getY()));
				sheet.addCell(new Label(3, sheetRow, "" + v.getZ()));
				sheet.addCell(new Label(4, sheetRow, "" + p.x));
				sheet.addCell(new Label(5, sheetRow, "" + p.y));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sheetRow++;
		recordLock.unlock();
		//System.out.println(sheetRow);
	}
	
	public void addClick(String panelName){
		if(sheet == null) return;

		recordLock.lock();
		try {
			java.util.Date date= new java.util.Date();
			sheet.addCell(new Label(0, sheetRow, "" + new Timestamp(date.getTime())));
			sheet.addCell(new Label(6, sheetRow, panelName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		sheetRow++;
		recordLock.unlock();
	}
	
	//write final before closing
	public void finalWrite(){
		recordLock.lock();
		try {
			enabled(false);
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		recordLock.unlock();
	}
	
	//enable/disable recording
	public void enabled(boolean b){
		recordLock.lock();
	    try {
	    	//if disabling, write to stop time
			if(sheet != null && !b){
			    sheet.addCell(new Formula(9, 8, "A3"));
				sheet.addCell(new Formula(9, 9, "A" + sheetRow));
				sheet.addCell(new Formula(9, 11, "COUNTA(G3:G" + sheetRow + 1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		enabled = b;
		recordLock.unlock();
	}
}
