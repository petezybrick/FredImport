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
package com.ipcglobal.fredimport.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ipcglobal.fredimport.item.DistinctCategoryItem;
import com.ipcglobal.fredimport.util.FredUtils;
import com.ipcglobal.fredimport.util.LogTool;
import com.ipcglobal.fredimport.xls.DistinctCategoriesSpreadsheet;


/**
 * The Class DistinctCategories.
 */
public class DistinctCategories {
	
	/** The log. */
	private static Log log = LogFactory.getLog(DistinctCategories.class);
	
	/** The distinct category items. */
	private Map<Integer, DistinctCategoryItem> distinctCategoryItems;
	
	/** The sorted distinct category items. */
	List<DistinctCategoryItem> sortedDistinctCategoryItems;
	
	/** The path qlik view sql scripts. */
	private String outputPathRptSql;
	
	/** The properties. */
	private Properties properties;
	
	/*** The output path. */
	private String outputPath;
	
	/*** The name of the Fred table. */
	private String tableNameFred;
	
	
	/**
	 * Instantiates a new distinct categories.DistinctCategories
	 *
	 * @param properties the properties
	 * @param distinctCategoryItems the distinct category items
	 * @param outputPath the output path
	 * @throws Exception the exception
	 */
	public DistinctCategories(Properties properties, Map<Integer, DistinctCategoryItem> distinctCategoryItems, String outputPath ) throws Exception {
		super();
		this.properties = properties;
		this.distinctCategoryItems = distinctCategoryItems;		
		this.outputPath = outputPath;
		this.tableNameFred = properties.getProperty("tableNameFred").trim();

		String outputSubdirRptSql = FredUtils.readfixPath( "outputSubdirRptSql", properties );
		outputPathRptSql = outputPath + outputSubdirRptSql;
		FileUtils.forceMkdir(new File(outputPathRptSql) );
	}

	
	/**
	 * Creates the report and qlik view sql scripts.
	 *
	 * @throws Exception the exception
	 */
	public void createReportAndQlikViewSqlScripts() throws Exception {
		sortedDistinctCategoryItems = new ArrayList<DistinctCategoryItem>(distinctCategoryItems.values());
		Collections.sort(sortedDistinctCategoryItems);
		createReport();
		createQlikViewSqlScripts();
		gzipRptSql();
	}
	
	
	/**
	 * Gzip rpt sql.
	 *
	 * @throws Exception the exception
	 */
	public void gzipRptSql( ) throws Exception {
		String outputNameExtRptSqlGZip = properties.getProperty( "outputNameExtRptSqlGZip" ).trim();
		String tarGzPath = outputPath + outputNameExtRptSqlGZip;
		FredUtils.createTarGzOfDirectory( outputPathRptSql, tarGzPath );
	}

	
	/**
	 * Creates the report.
	 *
	 * @throws Exception the exception
	 */
	private void createReport() throws Exception {
		DistinctCategoriesSpreadsheet distinctCategoriesSpreadsheet = 
				new DistinctCategoriesSpreadsheet( properties, outputPath );
		List<DistinctCategoryItem> sortedDistinctCategoryItems = new ArrayList<DistinctCategoryItem>( distinctCategoryItems.values() );
		Collections.sort( sortedDistinctCategoryItems );
		distinctCategoriesSpreadsheet.createSheet( sortedDistinctCategoryItems );
		distinctCategoriesSpreadsheet.writeWorkbook();
	}

	/**
	 * Creates the qlik view sql scripts.
	 *
	 * @throws Exception the exception
	 */
	private void createQlikViewSqlScripts() throws Exception {
		final int numFilesSubDir = 100;
		FileOutputStream fop = null;
		String currCategory1Name = "";
		File fileOut = null;
		int fileCnt = 0;
		String subDir = "t2/";
		FileUtils.forceMkdir(new File(outputPathRptSql + subDir) );
		// 
		try {
			int xlsRow = 2;
			for (DistinctCategoryItem item : sortedDistinctCategoryItems) {
				String entry = createEntry(xlsRow, item);
				if( !currCategory1Name.equals(item.getCategory1()) ) {
					if( fop != null ) fop.close();
					fileCnt++;
					if( (fileCnt % numFilesSubDir) == 0 ) {
						subDir = "t" + xlsRow + "/";
						FileUtils.forceMkdir(new File(outputPathRptSql + subDir) );
					}
					currCategory1Name = item.getCategory1();
					String outPathNameExt = createPathNameExtSqlScript( outputPathRptSql, subDir, currCategory1Name, xlsRow );
					fileOut = new File( outPathNameExt );
					//log.info("Writing to: " + fileOut.getPath() );
					fop = new FileOutputStream(fileOut);
				}
				fop.write( entry.getBytes() );
				xlsRow++;
			}
			fop.close();


		} finally {
			if (fop != null)fop.close();
		}
	}

	/*
	 * Replace all characters that don't conform to file naming with an underscore
	 */
	/**
	 * Creates the file name.
	 *
	 * @param outputPathRptSql the output path rpt sql
	 * @param subDir the sub dir
	 * @param currCategory1Name the curr category1 name
	 * @param xlsRow the xls row
	 * @return the string
	 */
	private String createPathNameExtSqlScript( String outputPathRptSql, String subDir, String currCategory1Name, int xlsRow ) {
		// Percent is a special case - change % to Pct
		currCategory1Name = currCategory1Name.replace("%", "Pct");
		// Slash is a special case - probably a date - change to dash
		currCategory1Name = currCategory1Name.replace("/", "-");
		final String[] searchList =      {"\\", "(", ")", ".", ",", "~", "`", "!", "@", "#", "$", "^", "&", "*", "+", "=", "{", "}", "[", "]", ":", ";", "'", "\"", "?", "<", ">" };
		final String[] replacementList = {"",   "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",  "",   "",  "",  "", };
		//final String[] replacementList = {"_",  "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_", "_",  "_", "_", "_", };
		String result = outputPathRptSql + subDir + "t"+xlsRow+"_"+ StringUtils.replaceEach(currCategory1Name, searchList, replacementList);
		if( result.length() > 96 ) result = result.substring(0,96);	// 100 is max for tar
		return result + ".qvs";
	}
	
	/**
	 * Creates the entry.
	 *
	 * @param xlsRow the xls row
	 * @param item the item
	 * @return the string
	 * @throws Exception the exception
	 */
	public String createEntry(int xlsRow, DistinctCategoryItem item) throws Exception {
		String tName = "t" + xlsRow;
		// Note: there is always a Category1
		StringBuilder sbComment = new StringBuilder("// ").append(tName).append(" ").append(item.getCategory1());
		StringBuilder sbLoad = new StringBuilder("// [").append(tName).append("]:\n").append("// LOAD category1 as [").append(tName).append(" Category1]");
		StringBuilder sbSqlColumns = new StringBuilder("// SQL SELECT category1");
		StringBuilder sbSqlWhere = new StringBuilder(" FROM " + tableNameFred + " where category1='").append(item.getCategory1().replace("'", "''")).append("'");
		
		if (item.getCategory2() != null) {
			sbComment.append(" | ").append(item.getCategory2());
			sbLoad.append( ", category2 as [").append(tName).append(" Category2]");
			sbSqlColumns.append( ", category2" );
			sbSqlWhere.append( " AND category2='").append(item.getCategory2().replace("'", "''")).append("'");
			
			if (item.getCategory3() != null) {
				sbComment.append(" | ").append(item.getCategory3());
				sbLoad.append( ", category3 as [").append(tName).append(" Category3]");
				sbSqlColumns.append( ", category3" );
				sbSqlWhere.append( " AND category3='").append(item.getCategory3().replace("'", "''")).append("'");
				
				if (item.getCategory4() != null) {
					sbComment.append(" | ").append(item.getCategory4());
					sbLoad.append( ", category4 as [").append(tName).append(" Category4]");
					sbSqlColumns.append( ", category4" );
					sbSqlWhere.append( " AND category4='").append(item.getCategory4().replace("'", "''")).append("'");
				
					if (item.getCategory5() != null) {
						sbComment.append(" | ").append(item.getCategory5());
						sbLoad.append( ", category5 as [").append(tName).append(" Category5]");
						sbSqlColumns.append( ", category5" );
						sbSqlWhere.append( " AND category5='").append(item.getCategory5().replace("'", "''")).append("'");
						
						if (item.getCategory6() != null) {
							sbComment.append(" | ").append(item.getCategory6());
							sbLoad.append( ", category6 as [").append(tName).append(" Category6]");
							sbSqlColumns.append( ", category6" );
							sbSqlWhere.append( " AND category6='").append(item.getCategory6().replace("'", "''")).append("'");
							
							if (item.getCategory7() != null) {
								sbComment.append(" | ").append(item.getCategory7());
								sbLoad.append( ", category7 as [").append(tName).append(" Category7]");
								sbSqlColumns.append( ", category7" );
								sbSqlWhere.append( " AND category7='").append(item.getCategory7().replace("'", "''")).append("'");
								
								if (item.getCategory8() != null) {
									sbComment.append(" | ").append(item.getCategory8());
									sbLoad.append( ", category8 as [").append(tName).append(" Category8]");
									sbSqlColumns.append( ", category8" );
									sbSqlWhere.append( " AND category8='").append(item.getCategory8().replace("'", "''")).append("'");
									
									if (item.getCategory9() != null) {
										sbComment.append(" | ").append(item.getCategory9());
										sbLoad.append( ", category9 as [").append(tName).append(" Category9]");
										sbSqlColumns.append( ", category9" );
										sbSqlWhere.append( " AND category9='").append(item.getCategory9().replace("'", "''")).append("'");
										
										if (item.getCategory10() != null) {
											sbComment.append(" | ").append(item.getCategory10());
											sbLoad.append( ", category10 as [").append(tName).append(" Category10]");
											sbSqlColumns.append( ", category10" );
											sbSqlWhere.append( " AND category10='").append(item.getCategory10().replace("'", "''")).append("'");
											
											if (item.getCategory11() != null) {
												sbComment.append(" | ").append(item.getCategory11());
												sbLoad.append( ", category11 as [").append(tName).append(" Category11]");
												sbSqlColumns.append( ", category11" );
												sbSqlWhere.append( " AND category11='").append(item.getCategory11().replace("'", "''")).append("'");
												
												if (item.getCategory12() != null) {
													sbComment.append(" | ").append(item.getCategory12());
													sbLoad.append( ", category12 as [").append(tName).append(" Category12]");
													sbSqlColumns.append( ", category12" );
													sbSqlWhere.append( " AND category12='").append(item.getCategory12().replace("'", "''")).append("'");
													
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		if( item.getDistinctUnits().size() > 0 ) {
			sbLoad.append( ", units as [").append(tName).append(" Units]");
			sbSqlColumns.append( ", units" );
		}
		
		if( item.getDistinctFrequency().size() > 0 ) {
			sbLoad.append( ", frequency as [").append(tName).append(" Frequency]");
			sbSqlColumns.append( ", frequency" );
		}
		
		if( item.getDistinctSeasonalAdj().size() > 0 ) {
			sbLoad.append( ", seasonal_adj as [").append(tName).append(" Seasonal Adj]");
			sbSqlColumns.append( ", seasonal_adj" );
		}
		
		if( item.getDistinctLastUpdated().size() > 0 ) {
			sbLoad.append( ", last_updated as [").append(tName).append(" Last Updated]");
			sbSqlColumns.append( ", last_updated" );
		}
		
		if( item.getDistinctDateSeries().size() > 0 ) {
			sbLoad.append( ", date_series as [").append(tName).append(" Date]");
			sbSqlColumns.append( ", date_series" );
		}
		
		if( item.getDistinctValue().size() > 0 ) {
			sbLoad.append( ", value as [").append(tName).append(" Value]");
			sbSqlColumns.append( ", value" );
		}
		
		if( item.getDistinctCountry().size() > 0 ) {
			sbLoad.append( ", country as [").append(tName).append(" Country]");
			sbSqlColumns.append( ", country" );
		}
		
		if( item.getDistinctCity().size() > 0 ) {
			sbLoad.append( ", city as [").append(tName).append(" City]");
			sbSqlColumns.append( ", city" );			
		}
		
		if( item.getDistinctCounty().size() > 0 ) {
			sbLoad.append( ", county as [").append(tName).append(" County]");
			sbSqlColumns.append( ", county" );			
		}
		
		if( item.getDistinctState().size() > 0 ) {
			sbLoad.append( ", state as [").append(tName).append(" State]");
			sbSqlColumns.append( ", state" );			
		}
		
		if( item.getDistinctRegionUS().size() > 0 ) {
			sbLoad.append( ", region_us as [").append(tName).append(" Region US]");
			sbSqlColumns.append( ", region_us" );						
		}
		
		if( item.getDistinctRegionWorld().size() > 0 ) {
			sbLoad.append( ", region_world as [").append(tName).append(" Region World]");
			sbSqlColumns.append( ", region_world" );						
		}
		
		if( item.getDistinctInstitution().size() > 0 ) {
			sbLoad.append( ", institution as [").append(tName).append(" Institution]");
			sbSqlColumns.append( ", institution" );						
		}
		
		if( item.getDistinctFrbDistrict().size() > 0 ) {
			sbLoad.append( ", frb_district as [").append(tName).append(" FRB Disctrict]");
			sbSqlColumns.append( ", frb_district" );									
		}
		
		if( item.getDistinctSex().size() > 0 ) {
			sbLoad.append( ", sex as [").append(tName).append(" Sex]");
			sbSqlColumns.append( ", sex" );									
		}
		
		if( item.getDistinctCurrency().size() > 0 ) {
			sbLoad.append( ", currency as [").append(tName).append(" Currency]");
			sbSqlColumns.append( ", currency" );									
		}

		String entry = new StringBuilder()
			.append( sbComment ).append("\n")
			.append( sbLoad ).append(";\n")
			.append( sbSqlColumns ).append( sbSqlWhere ).append(";\n")
			.append("\n")
			.toString();

		return entry;
	}

}
