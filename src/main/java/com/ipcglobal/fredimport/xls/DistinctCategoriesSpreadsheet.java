/**
 *    Copyright 2015 IPC Global (http://www.ipc-global.com) and others.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.ipcglobal.fredimport.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.ipcglobal.fredimport.item.DistinctCategoryItem;
import com.ipcglobal.fredimport.util.FredUtils;


/**
 * MetricSpreadsheet uses POI to generate the spreadsheet
 * 
 * One sheet is created per set of property values, i.e. if namespace.0=AWS/EC2 and namespace.1=AWS/ELB,
 * then two sheets will be created: AWS-EC2-0 and AWS-ELB-1.
 */
public class DistinctCategoriesSpreadsheet extends BaseXls {
	
	/** The log. */
	private static Log log = LogFactory.getLog(DistinctCategoriesSpreadsheet.class);
	
	/** The properties. */
	private Properties properties;
	
	/*** The output path. */
	private String outputPath;
	
	
	/**
	 * Instantiates a new metric spreadsheet.
	 *
	 * @param properties the properties
	 * @param outputPath the output path
	 * @throws Exception the exception
	 */
	public DistinctCategoriesSpreadsheet( Properties properties, String outputPath ) throws Exception {
		super();
		this.properties = properties;
		this.outputPath = outputPath;
	}

	
	/**
	 * Creates the sheet.
	 *
	 * @param distinctCategoryItems the distinct category items
	 * @throws Exception the exception
	 */
	public void createSheet( Collection<DistinctCategoryItem> distinctCategoryItems ) throws Exception {
		List<XlsDefItem> xlsDefItems = initHhdrWidthItems( );
		String sheetName = "DistinctCategoryItems";
		Sheet sheet = wb.createSheet( sheetName );
		processColumnWidths(sheet, xlsDefItems );
		sheet.createFreezePane(0, 1, 0, 1); // freeze top row
		sheet.setAutoFilter(CellRangeAddress.valueOf("A1:AB1"));	// hack - i know the number of columns
		sheet.getPrintSetup().setLandscape(true);
		sheet.setAutobreaks(true);
		sheet.getPrintSetup().setFitWidth((short) 1);
		sheet.getPrintSetup().setFitHeight((short) 1);

		int rowCnt = 0;

		// Header
		int colCnt = 0;
		Row rowHdr = sheet.createRow(rowCnt);
		for (XlsDefItem xlsDefItem : xlsDefItems) {
			Cell cellHdr = rowHdr.createCell(colCnt, Cell.CELL_TYPE_STRING);
			CellStyle style = findCellStyle("Arial", HSSFColor.WHITE.index,	(short) 11, XSSFFont.BOLDWEIGHT_BOLD, cellStyleFromHdrAlign(HdrAlign.Left), XSSFCellStyle.VERTICAL_TOP, HSSFColor.LIGHT_BLUE.index, CellBorder.All_Thin, formatGeneral);
			style.setWrapText(true);
			cellHdr.setCellStyle(style);
			cellHdr.setCellValue( xlsDefItem.getName() );
			colCnt++;
		}
		rowCnt++;
		
		// Data
		for( DistinctCategoryItem distinctCategoryItem : distinctCategoryItems )  {
			Row rowData = sheet.createRow(rowCnt);
			int colNum = 0;
			for (XlsDefItem xlsDefItem : xlsDefItems) 
				populateCell( rowData, colNum++, xlsDefItem.getDataType(), getByNameAsString( distinctCategoryItem, xlsDefItem.getName() ) );
			rowCnt++;
		}
	}


	/**
	 * Populate cell.
	 *
	 * @param rowData the row data
	 * @param colCnt the col cnt
	 * @param dataType the data type
	 * @param obj the obj
	 * @throws Exception the exception
	 */
	private void populateCell( Row rowData, int colCnt, DataType dataType, Object obj ) throws Exception {
		int cellType = 0;
		
		if (dataType == DataType.Numeric)
			cellType = XSSFCell.CELL_TYPE_NUMERIC;
		else if (dataType == DataType.NumericDec2)
			cellType = XSSFCell.CELL_TYPE_NUMERIC;
		else if (dataType == DataType.Text)
			cellType = XSSFCell.CELL_TYPE_STRING;
		else if (dataType == DataType.Date)
			cellType = XSSFCell.CELL_TYPE_STRING;
		else if (dataType == DataType.Accounting)
			cellType = XSSFCell.CELL_TYPE_NUMERIC;
		else if (dataType == DataType.Percent)
			cellType = XSSFCell.CELL_TYPE_NUMERIC;
		Cell cellData = rowData.createCell(colCnt, cellType);

		short findFormat = -1;
		if (dataType == DataType.Date)
			findFormat = formatMmDdYyyy;
		else if (dataType == DataType.Percent)
			findFormat = formatPercent;
		else if (dataType == DataType.Accounting)
			findFormat = formatAccounting;
		else if (dataType == DataType.Numeric)
			findFormat = formatNumeric;
		else if (dataType == DataType.NumericDec2)
			findFormat = formatNumericDec2;
		else
			findFormat = formatGeneral;
		CellStyle style = findCellStyle("Arial", HSSFColor.BLACK.index,
				(short) 11, XSSFFont.BOLDWEIGHT_NORMAL,
				cellStyleFromDataAlign(findAlignByDataType(dataType)),
				XSSFCellStyle.VERTICAL_TOP, BG_COLOR_NONE,
				CellBorder.All_Thin, findFormat );
		cellData.setCellStyle(style);

		if (dataType == DataType.Numeric || dataType == DataType.NumericDec2 
				|| dataType == DataType.Accounting || dataType == DataType.Percent) {
			if (obj == null)
				; // leave the cell empty
			else if (obj instanceof BigDecimal) {
				BigDecimal value = (BigDecimal) obj;
				if (value != null)
					cellData.setCellValue(value.doubleValue());
			} else if (obj instanceof Integer) {
				Integer value = (Integer) obj;
				if (value != null)
					cellData.setCellValue(value.intValue());
			} else if (obj instanceof Long) {
				Long value = (Long) obj;
				if (value != null)
					cellData.setCellValue(value.longValue());
			} else if (obj instanceof Double) {
				Double value = (Double) obj;
				if (value != null)
					cellData.setCellValue(value.doubleValue());
			} else if (obj instanceof Short) {
				Short value = (Short) obj;
				if (value != null)
					cellData.setCellValue(value.shortValue());
			} else if (obj instanceof String) {
				String value = (String) obj;
				if (value != null)
					cellData.setCellValue( value );
			} else
				throw new Exception("Unsupported numeric type: "
						+ obj.getClass().getSimpleName());
		} else if (dataType == DataType.Date) {
			Date date = (Date)obj;
			if (date != null)
				cellData.setCellValue(date);
		} else {
			cellData.setCellValue((String) obj );
		}
	}	
	
	
	/**
	 * Write workbook.
	 *
	 * @throws Exception the exception
	 */
	public void writeWorkbook( ) throws Exception {
		String pathDistinctCategoriesReport = outputPath + FredUtils.readfixPath( "outputSubdirRptSql", properties ) 
				+  properties.getProperty( "outputNameExtDistinctCategoriesReport" ).trim();
		log.info("Writing workbook: " + pathDistinctCategoriesReport );
		new File(pathDistinctCategoriesReport).delete();
		FileOutputStream fos = new FileOutputStream(pathDistinctCategoriesReport);
		wb.write( fos );
		fos.close();
	}

	/**
	 * Process column widths.
	 *
	 * @param sheet the sheet
	 * @param hdrWidthItems the hdr width items
	 * @throws Exception the exception
	 */
	private void processColumnWidths(Sheet sheet, List<XlsDefItem> hdrWidthItems )
			throws Exception {
		int colNum = 0;
		for (XlsDefItem hdrWidthItem : hdrWidthItems) 
			sheet.setColumnWidth(colNum++, hdrWidthItem.getWidth() * COLUMN_WIDTH_FACTOR);
	}
	
	/**
	 * Inits the hhdr width items.
	 *
	 * @return the list
	 * @throws Exception the exception
	 */
	private List<XlsDefItem> initHhdrWidthItems( ) throws Exception {
		List<XlsDefItem> hdrWidthItems = new ArrayList<XlsDefItem>();
		hdrWidthItems.add( new XlsDefItem( "Category1", 50, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category2", 40, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category3", 40, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category4", 20, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category5", 5, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category6", 2, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category7", 2, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category8", 2, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category9", 2, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category10", 2, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category11", 2, DataType.Text ));
		hdrWidthItems.add( new XlsDefItem( "Category12", 2, DataType.Text ));
		
		hdrWidthItems.add( new XlsDefItem( "distinct(Units)", 13, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(Frequency)", 16, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(SeasonalAdj)", 17, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(LastUpdated)", 15, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(DateSeries)", 15, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(Value)", 13, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(Country)", 14, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(City)", 13, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(County)", 14, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(State)", 13, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(RegionUS)", 15, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(RegionWorld)", 17, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(Institution)", 15, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(FrbDistrict)", 16, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(Sex)", 12, DataType.Numeric ));
		hdrWidthItems.add( new XlsDefItem( "distinct(Currency)", 15, DataType.Numeric ));
		
		return hdrWidthItems;
	}
	
	/**
	 * The Class XlsDefItem.
	 */
	private static class XlsDefItem {
		
		/** The name. */
		private String name;
		
		/** The width. */
		private int width;
		
		/** The data type. */
		private DataType dataType;
		
		/**
		 * Instantiates a new xls def item.
		 *
		 * @param hdr the hdr
		 * @param width the width
		 * @param dataType the data type
		 */
		public XlsDefItem(String hdr, int width, DataType dataType) {
			super();
			this.name = hdr;
			this.width = width;
			this.dataType = dataType;
		}
		
		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Sets the name.
		 *
		 * @param name the new name
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		/**
		 * Gets the width.
		 *
		 * @return the width
		 */
		public int getWidth() {
			return width;
		}
		
		/**
		 * Sets the width.
		 *
		 * @param width the new width
		 */
		public void setWidth(int width) {
			this.width = width;
		}
		
		/**
		 * Gets the data type.
		 *
		 * @return the data type
		 */
		public DataType getDataType() {
			return dataType;
		}
		
		/**
		 * Sets the data type.
		 *
		 * @param dataType the new data type
		 */
		public void setDataType(DataType dataType) {
			this.dataType = dataType;
		}	
	}
	
	
	/**
	 * Gets the by name as string.
	 *
	 * @param item the item
	 * @param name the name
	 * @return the by name as string
	 * @throws Exception the exception
	 */
	public String getByNameAsString( DistinctCategoryItem item, String name ) throws Exception {
		if( "Category1".equals(name) ) return item.getCategory1();
		else if( "Category2".equals(name) ) return item.getCategory2();
		else if( "Category3".equals(name) ) return item.getCategory3();
		else if( "Category4".equals(name) ) return item.getCategory4();
		else if( "Category5".equals(name) ) return item.getCategory5();
		else if( "Category6".equals(name) ) return item.getCategory6();
		else if( "Category7".equals(name) ) return item.getCategory7();
		else if( "Category8".equals(name) ) return item.getCategory8();
		else if( "Category9".equals(name) ) return item.getCategory9();
		else if( "Category10".equals(name) ) return item.getCategory10();
		else if( "Category11".equals(name) ) return item.getCategory11();
		else if( "Category12".equals(name) ) return item.getCategory12();
		else if( "Category12".equals(name) ) return item.getCategory12();
		else if( "distinct(Units)".equals(name) ) return String.valueOf( item.getDistinctUnits().size() );
		else if( "distinct(Frequency)".equals(name) ) return String.valueOf( item.getDistinctFrequency().size() );
		else if( "distinct(SeasonalAdj)".equals(name) ) return String.valueOf( item.getDistinctSeasonalAdj().size() );
		else if( "distinct(LastUpdated)".equals(name) ) return String.valueOf( item.getDistinctLastUpdated().size() );
		else if( "distinct(DateSeries)".equals(name) ) return String.valueOf( item.getDistinctDateSeries().size() );
		else if( "distinct(Value)".equals(name) ) return String.valueOf( item.getDistinctValue().size() );
		else if( "distinct(Country)".equals(name) ) return String.valueOf( item.getDistinctCountry().size() );
		else if( "distinct(City)".equals(name) ) return String.valueOf( item.getDistinctCity().size() );
		else if( "distinct(County)".equals(name) ) return String.valueOf( item.getDistinctCounty().size() );
		else if( "distinct(State)".equals(name) ) return String.valueOf( item.getDistinctState().size() );
		else if( "distinct(RegionUS)".equals(name) ) return String.valueOf( item.getDistinctRegionUS().size() );
		else if( "distinct(RegionWorld)".equals(name) ) return String.valueOf( item.getDistinctRegionWorld().size() );
		else if( "distinct(Institution)".equals(name) ) return String.valueOf( item.getDistinctInstitution().size() );
		else if( "distinct(FrbDistrict)".equals(name) ) return String.valueOf( item.getDistinctFrbDistrict().size() );
		else if( "distinct(Sex)".equals(name) ) return String.valueOf( item.getDistinctSex().size() );
		else if( "distinct(Currency)".equals(name) ) return String.valueOf( item.getDistinctCurrency().size() );
		else throw new Exception("Invalid field name: " + name );
	}


}
