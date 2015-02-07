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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ipcglobal.fredimport.item.DistinctCategoryItem;
import com.ipcglobal.fredimport.item.FredItem;
import com.ipcglobal.fredimport.item.SeriesIdItem;
import com.ipcglobal.fredimport.util.FredUtils;
import com.ipcglobal.fredimport.util.LogTool;


/**
 * The Class ProcessReadmeSeriesId.
 */
public class ProcessReadmeSeriesId {
	
	/** The log. */
	private static Log log = LogFactory.getLog(ProcessReadmeSeriesId.class);
	
	/** The reference. */
	private Reference reference;
	
	/** The properties. */
	private Properties properties;
	
	/** The path fred data. */
	private String inputPathFredData;
	
	/** The path table tsv files. */
	private String outputPathTableTsvFiles;
	
	/** The distinct not states. */
	private Map<String, Integer> distinctNotStates = new HashMap<String, Integer>();
	
	/** The currency country both. */
	private Map<String, Integer> currencyCountryBoth = new HashMap<String, Integer>();
	
	/** The distinct category items. */
	private Map<Integer, DistinctCategoryItem> distinctCategoryItems = new HashMap<Integer, DistinctCategoryItem>();
	
	/*** The output path. */
	private String outputPath;

	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		LogTool.initConsole();
		if (args.length < 1) {
			log.info("ERROR: Properties path/name is required");
			System.exit(8);
		}

		ProcessReadmeSeriesId analyzeReadmeSeriesId = null;
		try {
			analyzeReadmeSeriesId = new ProcessReadmeSeriesId(args[0]);
			analyzeReadmeSeriesId.process();

		} catch (Exception e) {
			log.info(e);
			e.printStackTrace();
		}
	}
	

	/**
	 * Instantiates a new process readme series id.
	 *
	 * @param pathNameProperties the path name properties
	 * @throws Exception the exception
	 */
	public ProcessReadmeSeriesId(String pathNameProperties) throws Exception {
		this.properties = new Properties();
		properties.load(new FileInputStream(pathNameProperties));
		this.reference = new Reference(properties.getProperty("inputPathReference"));
		
		this.inputPathFredData = FredUtils.readfixPath( "inputPathFredData", properties );
		this.outputPath = FredUtils.readfixPath( "outputPath", properties );		
		String outputSubdirTableTsvFiles = FredUtils.readfixPath( "outputSubdirTableTsvFiles", properties );
		this.outputPathTableTsvFiles = outputPath + outputSubdirTableTsvFiles;
	}
	
	
	/**
	 * Process.
	 *
	 * @throws Exception the exception
	 */
	public void process() throws Exception {
		try {
			FileUtils.forceDelete( new File(outputPath) );
		} catch( FileNotFoundException e ) {}
		
		long before = System.currentTimeMillis();
		List<SeriesIdItem> seriesIdItems = readReadmeSeriesId();
		log.info( "readReadmeSeriesId elapsed=" + (System.currentTimeMillis()-before));
		
		before = System.currentTimeMillis();
		List<FredItem> fredItems = createFredItems(seriesIdItems);
		log.info( "createfredItems elapsed=" + (System.currentTimeMillis()-before));
		
		before = System.currentTimeMillis();
		runCreateTableTsvs(fredItems);
		log.info( "createTableTsvs elapsed=" + (System.currentTimeMillis()-before));
		
		before = System.currentTimeMillis();
		new DistinctCategories(properties, distinctCategoryItems, outputPath ).createReportAndQlikViewSqlScripts();
		log.info( "createReportAndQlikViewSqlScripts elapsed=" + (System.currentTimeMillis()-before));
	}

	
	/**
	 * Run create table tsvs.
	 *
	 * @param fredItems the fred items
	 * @throws Exception the exception
	 */
	private void runCreateTableTsvs( List<FredItem> fredItems ) throws Exception {
		File filePathTableTsvFiles = new File(outputPathTableTsvFiles);
		FileUtils.forceMkdir(filePathTableTsvFiles);

		int numThreads = 2 * Runtime.getRuntime().availableProcessors();
		List<CreateTableTsvs> createTableTsvsThreads = new ArrayList<CreateTableTsvs>(numThreads);
		List<List<FredItem>> listFredItems = new ArrayList<List<FredItem>>(numThreads);
		for( int i=0 ; i<numThreads ; i++ ) {
			List<FredItem> newFredItems = new ArrayList<FredItem>();
			listFredItems.add( newFredItems );
			createTableTsvsThreads.add( new CreateTableTsvs( i, newFredItems ) );
		}
		int threadNum = 0;
		for( int i=0 ; i<fredItems.size() ; i++ ) {
			listFredItems.get(threadNum).add( fredItems.get(i));
			threadNum++;
			if( threadNum == numThreads ) threadNum = 0;
		}
		
		for( CreateTableTsvs createTableTsvs : createTableTsvsThreads ) createTableTsvs.start();
		for( CreateTableTsvs createTableTsvs : createTableTsvsThreads ) createTableTsvs.join();
		
		for( FredItem fredItem : fredItems ) {
			Integer hashCategories = fredItem.hashCodeCategories();
			DistinctCategoryItem distinctCategoryItem = distinctCategoryItems.get(hashCategories);
			if (distinctCategoryItem != null)
				distinctCategoryItem.updateDistinctFields(fredItem);
			else {
				distinctCategoryItem = new DistinctCategoryItem(fredItem);
				distinctCategoryItems.put(hashCategories, distinctCategoryItem);
			}
		}

	}
	
	
	/**
	 * The Class CreateTableTsvs.
	 */
	private class CreateTableTsvs extends Thread {
		
		/** The max before decimal. */
		private int maxBeforeDecimal = 0;
		
		/** The max after decimal. */
		private int maxAfterDecimal = 0;
		
		/** The thread offset. */
		private int threadOffset;
		
		/** The fred items. */
		private List<FredItem> fredItems;

		/**
		 * Instantiates a new creates the table tsvs.
		 *
		 * @param threadOffset the thread offset
		 * @param fredItems the fred items
		 */
		public CreateTableTsvs(int threadOffset, List<FredItem> fredItems) {
			super();
			this.threadOffset = threadOffset;
			this.fredItems = fredItems;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				log.info(("CreateTableTsvs: run() begins, threadOffset=" + threadOffset ));
				long totLines = 0;
				long numFiles = 0;
				int outFileNumber = 0;
				int rowsInOutFile = 0;
				final int maxRowsPerOutFile = 2500000;
				
				String outFilePathName = outputPathTableTsvFiles + threadOffset + "-" + outFileNumber + ".tsv.gz";
				GZIPOutputStream out = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(outFilePathName)));
	
				for (FredItem fredItem : fredItems) {
					numFiles++;
					LineIterator it = FileUtils.lineIterator(new File(inputPathFredData + "data" + File.separator + fredItem.getCsvFileName()), "UTF-8");
					try {
						it.nextLine(); // skip the header
						while (it.hasNext()) {
							String line = it.nextLine();
							totLines++;
							int posFirstComma = line.indexOf(",");
							String date = line.substring(0, posFirstComma);
							String value = line.substring(posFirstComma + 1);
							if (".".equals(value))
								value = "0"; // some very old values are set to "." so default these to zero
							
							// TEST TEST TEST
							int numAfter = 0;
							int posDot = value.indexOf(".");
							if( posDot > -1 ) {
								numAfter = value.length() - posDot -1;
								// log.info( value + " " + numAfter );
								if( numAfter > maxAfterDecimal ) maxAfterDecimal = numAfter;
								if( posDot > maxBeforeDecimal ) maxBeforeDecimal = posDot;
								
								// Impala has a max 38 restriction on precisions, 38,20 gives a max number of 18 positions and scale of 20
								// However, some numbers have a scale up to 28, so round them down to 20 
								if( numAfter > 20 ) {
									BigDecimal temp = new BigDecimal( value );
									value = temp.setScale(20,BigDecimal.ROUND_HALF_UP).toPlainString();
								}
							}
							
							fredItem.setDateSeries(date).setValue(value);
							if( rowsInOutFile > 0 ) out.write("\n".getBytes());
							out.write(fredItem.toTsv().getBytes());
							rowsInOutFile++;
							if( rowsInOutFile == maxRowsPerOutFile ) {
								//log.info("Closing: " + outFilePathName );
								out.finish();
								out.close();
								outFilePathName = outputPathTableTsvFiles + threadOffset + "-" + ++outFileNumber + ".tsv.gz";
								out = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(outFilePathName)));
								rowsInOutFile = 0;
							}	
						}
					} finally {
						LineIterator.closeQuietly(it);
					}
	
					if ((numFiles % 10000) == 0)
						log.info(("CreateTableTsvs: Process files, numFiles=" + numFiles + ", threadOffset=" + threadOffset ));
				}
				if( rowsInOutFile > 0 ) {
					//log.info("CreateTableTsvs: Closing: " + outFilePathName );
					out.finish();
					out.close();
				}
	
				// log.info("CreateTableTsvs: total lines: " + totLines + ", threadOffset=" + threadOffset );
				// log.info(">>> maxBeforeDecimal=" + maxBeforeDecimal + ", maxAfterDecimal=" + maxAfterDecimal );
			} catch( Exception e ) {
				log.info(e);
				e.printStackTrace();
			}
		}
		
//		private String roundNumberToFitColumn( String value ) {
//			BigDecimal bd = new BigDecimal( value );
//			
//		}
	}
	


	/**
	 * Creates the fred items.
	 *
	 * @param seriesIdItems the series id items
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<FredItem> createFredItems(List<SeriesIdItem> seriesIdItems) throws Exception {
		List<FredItem> fredItems = new ArrayList<FredItem>();
		for (SeriesIdItem seriesIdItem : seriesIdItems) {
			FredItem fredItem = new FredItem(seriesIdItem);
			List<String> categories = splitGroups(seriesIdItem.getTitle());
			// Title consists of multiple categories, separated by ":", " for ", " in " and " based on "
			// if the last category contains phrases that denote groups then process separately, i.e.
			// look for occurrences of common groupings - they tend to have " for <country" or " in <somewhere>"
			// these denote a common subtitle, i.e. "Whatever for United States" and "Whatever for Great Britain"
			// should both be under the subtitle "Whatever" - later post-processing will extract the grouping (i.e. country)
			if (categories.size() > 1)
				updatefredItemCommon(fredItem, categories, seriesIdItem);

			// TODO: post processing for age and sex

			// this is a hack, probably a smarter way to do this
			int catSize = categories.size();
			if (catSize > 0)
				fredItem.setCategory1(categories.get(0));
			if (catSize > 1)
				fredItem.setCategory2(categories.get(1));
			if (catSize > 2)
				fredItem.setCategory3(categories.get(2));
			if (catSize > 3)
				fredItem.setCategory4(categories.get(3));
			if (catSize > 4)
				fredItem.setCategory5(categories.get(4));
			if (catSize > 5)
				fredItem.setCategory6(categories.get(5));
			if (catSize > 6)
				fredItem.setCategory7(categories.get(6));
			if (catSize > 7)
				fredItem.setCategory8(categories.get(7));
			if (catSize > 8)
				fredItem.setCategory9(categories.get(8));
			if (catSize > 9)
				fredItem.setCategory10(categories.get(9));
			if (catSize > 10)
				fredItem.setCategory11(categories.get(10));
			if (catSize > 11)
				fredItem.setCategory12(categories.get(11));

			fredItems.add(fredItem);

		}
		return fredItems;
	}

	/**
	 * Read readme series id.
	 *
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<SeriesIdItem> readReadmeSeriesId() throws Exception {
		List<SeriesIdItem> seriesIdItems = new ArrayList<SeriesIdItem>();
		boolean isHeaderRows = true;
		boolean isFooterRows = false;
		LineIterator it = FileUtils.lineIterator(new File(inputPathFredData + "README_SERIES_ID_SORT.txt"), "UTF-8");
		int numLines = 0;
		try {
			while (it.hasNext()) {
				String line = it.nextLine();
				numLines++;
				if (isHeaderRows) {
					if (line.startsWith("File"))
						isHeaderRows = false;
				} else if (isFooterRows) {

				} else {
					if (line.length() == 0) {
						isFooterRows = true;
						continue;
					}
					// Data row
					// File;Title; Units; Frequency; Seasonal Adjustment; Last Updated
					// Bypass all (DISCONTINUED SERIES) rows;
					if( line.indexOf( "(DISCONTINUED SERIES)" ) > -1 || line.indexOf( "(DISCONTINUED)" ) > -1 || line.indexOf( "(Discontinued Series)" ) > -1 ) continue;
					String[] fields = splitFields(line);

					seriesIdItems.add(new SeriesIdItem().setCsvFileName(fields[0]).setTitle(fields[1].replace("©", "")).setUnits(fields[2])
							.setFrequency(fields[3]).setSeasonalAdj(fields[4]).setLastUpdated(fields[5]));
				}

				if ((numLines % 25000) == 0)
					log.info("readReadmeSeriesId: read lines: " + numLines);
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
		return seriesIdItems;
	}

	/*
	 * Split by ": ", " for ", " in " and " based on " into a list of categories
	 */
	/**
	 * Split groups.
	 *
	 * @param title the title
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<String> splitGroups(String title) throws Exception {
		// TODO: put list in filefoundSex or properties
		final List<String> splitBys = Arrays.asList(": ", " for ", " in ", " based on ");
		// First get all of positions of the splitBys in the title
		List<SplitByItem> splitByItems = new ArrayList<SplitByItem>();
		for (String splitBy : splitBys) {
			int offset = -1;
			do {
				offset = title.indexOf(splitBy, offset + 1);
				if (offset > -1) {
					splitByItems.add(new SplitByItem(offset, splitBy.length()));
				}
			} while (offset > -1);
		}
		Collections.sort(splitByItems);
		List<String> groups = new ArrayList<String>();
		int beginIndex = 0;
		for (SplitByItem splitByItem : splitByItems) {
			groups.add(title.substring(beginIndex, splitByItem.offset).trim());
			beginIndex = splitByItem.offset + splitByItem.length;
		}
		// Last field (or only field if no splits occured
		groups.add(title.substring(beginIndex).trim());

		return groups;
	}

	/**
	 * The Class SplitByItem.
	 */
	private static class SplitByItem implements Comparable<SplitByItem> {
		
		/** The offset. */
		public int offset;
		
		/** The length. */
		public int length;

		/**
		 * Instantiates a new split by item.
		 *
		 * @param offset the offset
		 * @param length the length
		 */
		public SplitByItem(int offset, int length) {
			super();
			this.offset = offset;
			this.length = length;
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(SplitByItem that) {
			return this.offset - that.offset;
		}
	}

	
	/**
	 * Update FRED item common.
	 *
	 * @param fredItemCommon the fred item common
	 * @param categories the categories
	 * @param seriesIdItem the series id item
	 * @throws Exception the exception
	 */
	private void updatefredItemCommon(FredItem fredItemCommon, List<String> categories, SeriesIdItem seriesIdItem) throws Exception {
		boolean isFound = false;
		String lastCategory = categories.get(categories.size() - 1);

		if (lastCategory.endsWith(")") && lastCategory.lastIndexOf("(") > -1) {
			lastCategory = lastCategory.substring(0, lastCategory.lastIndexOf("(")).trim();
		}
		// TODO: validation. for example, there is one bad row with "St Louis MO-IL," which should be "St Louis, MO-IL"
		if ("Bil. of US $".equals(seriesIdItem.getUnits())) {
			// Could be Currency, Country, WorldRegion
			// If currency, set the currency and country
			// Else if country, set the country
			// Else if world region, set the regionWorld
			if (reference.getRefCountriesByCurrencies().containsKey(lastCategory)) {
				fredItemCommon.setCurrency(lastCategory);
				fredItemCommon.setCountry(reference.getRefCountriesByCurrencies().get(lastCategory));
				isFound = true;
			} else if (reference.getRefCountries().containsKey(lastCategory)) {
				fredItemCommon.setCountry(reference.getRefCountries().get(lastCategory));
				isFound = true;
			} else if (reference.checkEuropePrefixes(lastCategory)) {
				fredItemCommon.setCountry(lastCategory);
				isFound = true;
			} else if (reference.getRefWorldRegions().containsKey(lastCategory)) {
				fredItemCommon.setRegionWorld(lastCategory);
				isFound = true;
			} else if (reference.getRefInstitutions().containsKey(lastCategory)) {
				fredItemCommon.setInstitution(lastCategory);
				isFound = true;
			} else {
				log.info(">>> currency, country or both: " + lastCategory + ", title=" + seriesIdItem.getTitle());
				Integer count = currencyCountryBoth.get(lastCategory);
				count = (count != null) ? new Integer(count + 1) : new Integer(1);
				currencyCountryBoth.put(lastCategory, count);
			}

		} else if (checkAndSetValidCountryCityState(fredItemCommon, lastCategory)) {
			isFound = true;
		} else if (checkFrbDistrict(fredItemCommon, lastCategory, seriesIdItem)) {
			isFound = true;			
		} else if (lastCategory.endsWith(" Region")) {
			fredItemCommon.setRegionUS(lastCategory.substring(0, lastCategory.lastIndexOf(" Region")));
			isFound = true;
		} else {
			// Could be US state or a country name
			// else assume StateFullName, lookup to get abbrev
			String stateAbbrev = reference.getRefStateAbbrevsByNames().get(lastCategory);
			if (stateAbbrev != null) {
				fredItemCommon.setState(stateAbbrev);
				isFound = true;
			} else {
				if (reference.getRefCountries().containsKey(lastCategory)) {
					fredItemCommon.setCountry(reference.getRefCountries().get(lastCategory));
					isFound = true;
				} else if (reference.checkEuropePrefixes(lastCategory)) {
					fredItemCommon.setCountry(lastCategory);
					isFound = true;
				} else if (reference.getRefWorldRegions().containsKey(lastCategory)) {
					fredItemCommon.setRegionWorld(lastCategory);
					isFound = true;
				} else if (reference.getRefInstitutions().containsKey(lastCategory)) {
					fredItemCommon.setInstitution(lastCategory);
					isFound = true;
				} else if (reference.getRefCountriesByCurrencies().containsKey(lastCategory)) {
					fredItemCommon.setCurrency(lastCategory);
					fredItemCommon.setCountry(reference.getRefCountriesByCurrencies().get(lastCategory));
					isFound = true;
				}
			}
			// TODO: log a warning here
			if (!isFound) {
				Integer count = distinctNotStates.get(lastCategory);
				count = (count != null) ? new Integer(count + 1) : new Integer(1);
				distinctNotStates.put(lastCategory, count);
				// log.info("WARN: state abbrev not found for: " + suffixLastCategory );
			}
		}

		if (isFound) {
			categories.remove(categories.size() - 1);
			lastCategory = categories.get(categories.size() - 1);
		}
		
		
		// Is the new LastCategory the sex?
		if( categories.size() > 1 ) {
			String sex = reference.getRefSexes().get(lastCategory);
			if( sex != null ) {
				fredItemCommon.setSex(sex);
				categories.remove(categories.size() - 1);
			} 
		} 
		
		// There are a small number of time series' that contain the sex as part of a category name (24 as of 2015-01)
		// Example: Unemployment Rate, Married Males, Spouse Present for United States
		// For these, set the sex but don't change the category.
		if( fredItemCommon.getSex() == null ) {
			for( Map.Entry<String,String> entry : reference.getRefSexes().entrySet() ) {
				if( seriesIdItem.getTitle().indexOf( entry.getKey() ) > -1 ) {
					fredItemCommon.setSex( entry.getValue() );
					break;
				}
			}
		}
	}
	
	
	/**
	 * Check frb district.
	 *
	 * @param fredItemCommon the fred item common
	 * @param lastCategory the last category
	 * @param seriesIdItem the series id item
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	private boolean checkFrbDistrict(FredItem fredItemCommon, String lastCategory, SeriesIdItem seriesIdItem) throws Exception {
		if( 
			lastCategory.startsWith("FRB -") 
			||
			lastCategory.startsWith("FRB-") 
		) {
			int posEnd = lastCategory.length();
			final String[] lastEnds = {" District", " District States", " Division"};
			for( String lastEnd : lastEnds ) {
				if( lastCategory.endsWith( lastEnd )) {
					posEnd = lastCategory.lastIndexOf( lastEnd );
					break;
				}
			}

			String frbDistrict = lastCategory.substring( lastCategory.indexOf("-")+1, posEnd ).trim();
			fredItemCommon.setFrbDistrict(frbDistrict);
			return true;
		} 

		return false;
	}

	

	/**
	 * Check and set valid country city state.
	 *
	 * @param fredItemCommon the fred item common
	 * @param lastCategory the last category
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	private boolean checkAndSetValidCountryCityState(FredItem fredItemCommon, String lastCategory) throws Exception {
		// Special case - DC
		if( "Washington D.C.".equals( lastCategory )) {
			fredItemCommon.setState( "DC" );
			return true;
		}
		
		int posComma = lastCategory.indexOf(',');
		if (posComma > -1) {
			// can end in (MSA), (MD), etc. - if so, then trim off before checking for country or state
			int posLeftParen = lastCategory.indexOf("(", posComma);
			if (posLeftParen > -1)
				lastCategory = lastCategory.substring(0, posLeftParen);

			// First check if valid country
			if (reference.getRefCountries().containsKey(lastCategory)) {
				fredItemCommon.setCountry(reference.getRefCountries().get(lastCategory));
				return true;

			} else {
				String[] cityState = lastCategory.split("[,]");
				if (cityState.length == 2) {
					// Could be City, StateAbbrev or City, StateName  (Atlanta, GA  or  Atlanta, Georgia) 
					String stateCheck = cityState[1].trim();//		
//					for( SeriesIdItem seriesIdItem : seriesIdItems ) {
//					if( 
////							seriesIdItem.getTitle().toUpperCase().indexOf("FRB") > -1  
//							seriesIdItem.getTitle().toUpperCase().indexOf("FRB-") > -1 
//							||
//							seriesIdItem.getTitle().toUpperCase().indexOf("FRB -") > -1 
		//
//					) {
//						log.info(seriesIdItem.toString());
//						cnt++;
//					}
//				}

					if (	reference.getRefValidStateAbbrevs().containsKey(stateCheck) 
							|| 
							reference.getRefStateAbbrevsByNames().containsKey(stateCheck) 
							|| 
							reference.getRefCitiesByStates().containsKey(stateCheck)) {
						if( reference.getRefStateAbbrevsByNames().containsKey(stateCheck) ) 
							fredItemCommon.setState(reference.getRefStateAbbrevsByNames().get(stateCheck));	// set to abbrev based on state name
						else fredItemCommon.setState(stateCheck);
						String cityCheck = cityState[0].trim();
						if (!cityCheck.endsWith(" County"))
							fredItemCommon.setCity(cityCheck);
						else
							fredItemCommon.setCounty(cityCheck.substring(0, cityCheck.lastIndexOf(" County")));
						return true;
					} 
				}
			}
		}
		return false;
	}
	

	/**
	 * Split fields.
	 *
	 * @param line the line
	 * @return the string[]
	 */
	private String[] splitFields(String line) {
		int fromIndex = 0;
		String[] fields = new String[6];
		// File;Title; Units; Frequency; Seasonal Adjustment; Last Updated
		for (int i = 0; i < 6; i++) {
			String delim = ";";
			if ('"' == line.charAt(fromIndex)) {
				delim = "\";";
				fromIndex++;
			}

			int posSemiColon = line.indexOf(delim, fromIndex);
			if (posSemiColon == -1)
				posSemiColon = line.length();
			fields[i] = line.substring(fromIndex, posSemiColon).trim();
			fromIndex = posSemiColon + delim.length();
		}
		return fields;
	}

}
