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
package com.ipcglobal.fredimport.item;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * The Class DistinctCategoryItem.
 */
public class DistinctCategoryItem implements Serializable, Comparable<DistinctCategoryItem> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6538634839129960362L;
	
	/** The category1. */
	private String category1;
	
	/** The category2. */
	private String category2;
	
	/** The category3. */
	private String category3;
	
	/** The category4. */
	private String category4;
	
	/** The category5. */
	private String category5;
	
	/** The category6. */
	private String category6;
	
	/** The category7. */
	private String category7;
	
	/** The category8. */
	private String category8;
	
	/** The category9. */
	private String category9;
	
	/** The category10. */
	private String category10;
	
	/** The category11. */
	private String category11;
	
	/** The category12. */
	private String category12;
	
	/** The distinct units. */
	private Map<String,Object> distinctUnits = new HashMap<String,Object>();
	
	/** The distinct frequency. */
	private Map<String,Object> distinctFrequency = new HashMap<String,Object>();
	
	/** The distinct seasonal adj. */
	private Map<String,Object> distinctSeasonalAdj = new HashMap<String,Object>();
	
	/** The distinct last updated. */
	private Map<String,Object> distinctLastUpdated = new HashMap<String,Object>();
	
	/** The distinct date series. */
	private Map<String,Object> distinctDateSeries = new HashMap<String,Object>();
	
	/** The distinct value. */
	private Map<String,Object> distinctValue = new HashMap<String,Object>();
	
	/** The distinct country. */
	private Map<String,Object> distinctCountry = new HashMap<String,Object>();
	
	/** The distinct city. */
	private Map<String,Object> distinctCity = new HashMap<String,Object>();
	
	/** The distinct county. */
	private Map<String,Object> distinctCounty = new HashMap<String,Object>();
	
	/** The distinct state. */
	private Map<String,Object> distinctState = new HashMap<String,Object>();
	
	/** The distinct region us. */
	private Map<String,Object> distinctRegionUS = new HashMap<String,Object>();
	
	/** The distinct region world. */
	private Map<String,Object> distinctRegionWorld = new HashMap<String,Object>();
	
	/** The distinct institution. */
	private Map<String,Object> distinctInstitution = new HashMap<String,Object>();
	
	/** The distinct frb district. */
	private Map<String,Object> distinctFrbDistrict = new HashMap<String,Object>();
	
	/** The distinct sex. */
	private Map<String,Object> distinctSex = new HashMap<String,Object>();
	
	/** The distinct currency. */
	private Map<String,Object> distinctCurrency = new HashMap<String,Object>();

	/** The sort by. */
	private String sortBy;
	
	
	/**
	 * Instantiates a new distinct category item.
	 *
	 * @param fredItem the fred item
	 * @throws Exception the exception
	 */
	public DistinctCategoryItem( FredItem fredItem ) throws Exception {
		this.category1 = fredItem.getCategory1();
		this.category2 = fredItem.getCategory2();
		this.category3 = fredItem.getCategory3();
		this.category4 = fredItem.getCategory4();
		this.category5 = fredItem.getCategory5();
		this.category6 = fredItem.getCategory6();
		this.category7 = fredItem.getCategory7();
		this.category8 = fredItem.getCategory8();
		this.category9 = fredItem.getCategory9();
		this.category10 = fredItem.getCategory10();
		this.category11 = fredItem.getCategory11();
		this.category12 = fredItem.getCategory12();
		
		sortBy = this.category1 + "|" + 
				nullAsEmptyString(this.category2) + "|" + 
				nullAsEmptyString(this.category3) + "|" + 
				nullAsEmptyString(this.category4) + "|" + 
				nullAsEmptyString(this.category5) + "|" + 
				nullAsEmptyString(this.category6) + "|" + 
				nullAsEmptyString(this.category7) + "|" + 
				nullAsEmptyString(this.category8) + "|" + 
				nullAsEmptyString(this.category9) + "|" + 
				nullAsEmptyString(this.category10) + "|" + 
				nullAsEmptyString(this.category11) + "|" + 
				nullAsEmptyString(this.category12);
		updateDistinctFields( fredItem );
	}
	
	
	/**
	 * Null as empty string.
	 *
	 * @param in the in
	 * @return the string
	 */
	private static String nullAsEmptyString( String in ) {
		if( in != null ) return in;
		else return "";
	}
	
	
	/**
	 * Update distinct fields.
	 *
	 * @param fredItem the fred item
	 * @throws Exception the exception
	 */
	public void updateDistinctFields( FredItem fredItem ) throws Exception {
		if( fredItem.getUnits() != null ) distinctUnits.put( fredItem.getUnits(), null );
		if( fredItem.getFrequency() != null ) distinctFrequency.put( fredItem.getFrequency(), null );
		if( fredItem.getSeasonalAdj() != null ) distinctSeasonalAdj.put( fredItem.getSeasonalAdj(), null );
		if( fredItem.getLastUpdated() != null ) distinctLastUpdated.put( fredItem.getLastUpdated(), null );
		if( fredItem.getDateSeries() != null ) distinctDateSeries.put( fredItem.getDateSeries(), null );
		if( fredItem.getValue() != null ) distinctValue.put( fredItem.getValue(), null );
		if( fredItem.getCountry() != null ) distinctCountry.put( fredItem.getCountry(), null );
		if( fredItem.getCity() != null ) distinctCity.put( fredItem.getCity(), null );
		if( fredItem.getCounty() != null ) distinctCounty.put( fredItem.getCounty(), null );
		if( fredItem.getState() != null ) distinctState.put( fredItem.getState(), null );
		if( fredItem.getRegionUS() != null ) distinctRegionUS.put( fredItem.getRegionUS(), null );
		if( fredItem.getRegionWorld() != null ) distinctRegionWorld.put( fredItem.getRegionWorld(), null );
		if( fredItem.getInstitution() != null ) distinctInstitution.put( fredItem.getInstitution(), null );
		if( fredItem.getFrbDistrict() != null ) distinctFrbDistrict.put( fredItem.getFrbDistrict(), null );	
		if( fredItem.getSex() != null ) distinctSex.put( fredItem.getSex(), null );
		if( fredItem.getCurrency() != null ) distinctCurrency.put( fredItem.getCurrency(), null );
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DistinctCategoryItem that) {
		return this.sortBy.compareTo( that.sortBy );
	}
	
	
	/**
	 * To report hdr.
	 *
	 * @return the string
	 */
	public static String toReportHdr() {
		return
			"Category1\tCategory2\tCategory3\tCategory4\tCategory5\tCategory6\tCategory7\tCategory8\tCategory9\tCategory10\tCategory11\tCategory12\t" + 
			"distinct(Units)\tdistinct(Frequency)\tdistinct(SeasonalAdj)\tdistinct(LastUpdated)\tdistinct(DateSeries)\tdistinct(Value)\tdistinct(Country)\t" +
			"distinct(City)\tdistinct(County)\tdistinct(State)\tdistinct(RegionUS)\tdistinct(RegionWorld)\tistinct(Institution)\tdistinct(FrbDistrict)\t" +
			"distinct(Sex)\tdistinct(Currency)\n";
	}
	
	
	/**
	 * To report data.
	 *
	 * @return the string
	 */
	public String toReportData() {
		return
			nullAsEmptyString(category1) + "\t" +
			nullAsEmptyString(category2) + "\t" +
			nullAsEmptyString(category3) + "\t" +
			nullAsEmptyString(category4) + "\t" +
			nullAsEmptyString(category5) + "\t" +
			nullAsEmptyString(category6) + "\t" +
			nullAsEmptyString(category7) + "\t" +
			nullAsEmptyString(category8) + "\t" +
			nullAsEmptyString(category9) + "\t" +
			nullAsEmptyString(category10) + "\t" +
			nullAsEmptyString(category11) + "\t" +
			nullAsEmptyString(category12) + "\t" +
			distinctUnits.size() + "\t" +
			distinctFrequency.size() + "\t" +
			distinctSeasonalAdj.size() + "\t" +
			distinctLastUpdated.size() + "\t" +
			distinctDateSeries.size() + "\t" +
			distinctValue.size() + "\t" +
			distinctCountry.size() + "\t" +
			distinctCity.size() + "\t" +
			distinctCounty.size() + "\t" +
			distinctState.size() + "\t" +
			distinctRegionUS.size() + "\t" +
			distinctRegionWorld.size() + "\t" +
			distinctInstitution.size() + "\t" +
			distinctFrbDistrict.size() + "\t" +
			distinctSex.size() + "\t" +
			distinctCurrency.size() + "\n";
	}


	/**
	 * Gets the category1.
	 *
	 * @return the category1
	 */
	public String getCategory1() {
		return category1;
	}


	/**
	 * Sets the category1.
	 *
	 * @param category1 the new category1
	 */
	public void setCategory1(String category1) {
		this.category1 = category1;
	}


	/**
	 * Gets the category2.
	 *
	 * @return the category2
	 */
	public String getCategory2() {
		return category2;
	}


	/**
	 * Sets the category2.
	 *
	 * @param category2 the new category2
	 */
	public void setCategory2(String category2) {
		this.category2 = category2;
	}


	/**
	 * Gets the category3.
	 *
	 * @return the category3
	 */
	public String getCategory3() {
		return category3;
	}


	/**
	 * Sets the category3.
	 *
	 * @param category3 the new category3
	 */
	public void setCategory3(String category3) {
		this.category3 = category3;
	}


	/**
	 * Gets the category4.
	 *
	 * @return the category4
	 */
	public String getCategory4() {
		return category4;
	}


	/**
	 * Sets the category4.
	 *
	 * @param category4 the new category4
	 */
	public void setCategory4(String category4) {
		this.category4 = category4;
	}


	/**
	 * Gets the category5.
	 *
	 * @return the category5
	 */
	public String getCategory5() {
		return category5;
	}


	/**
	 * Sets the category5.
	 *
	 * @param category5 the new category5
	 */
	public void setCategory5(String category5) {
		this.category5 = category5;
	}


	/**
	 * Gets the category6.
	 *
	 * @return the category6
	 */
	public String getCategory6() {
		return category6;
	}


	/**
	 * Sets the category6.
	 *
	 * @param category6 the new category6
	 */
	public void setCategory6(String category6) {
		this.category6 = category6;
	}


	/**
	 * Gets the category7.
	 *
	 * @return the category7
	 */
	public String getCategory7() {
		return category7;
	}


	/**
	 * Sets the category7.
	 *
	 * @param category7 the new category7
	 */
	public void setCategory7(String category7) {
		this.category7 = category7;
	}


	/**
	 * Gets the category8.
	 *
	 * @return the category8
	 */
	public String getCategory8() {
		return category8;
	}


	/**
	 * Sets the category8.
	 *
	 * @param category8 the new category8
	 */
	public void setCategory8(String category8) {
		this.category8 = category8;
	}


	/**
	 * Gets the category9.
	 *
	 * @return the category9
	 */
	public String getCategory9() {
		return category9;
	}


	/**
	 * Sets the category9.
	 *
	 * @param category9 the new category9
	 */
	public void setCategory9(String category9) {
		this.category9 = category9;
	}


	/**
	 * Gets the category10.
	 *
	 * @return the category10
	 */
	public String getCategory10() {
		return category10;
	}


	/**
	 * Sets the category10.
	 *
	 * @param category10 the new category10
	 */
	public void setCategory10(String category10) {
		this.category10 = category10;
	}


	/**
	 * Gets the category11.
	 *
	 * @return the category11
	 */
	public String getCategory11() {
		return category11;
	}


	/**
	 * Sets the category11.
	 *
	 * @param category11 the new category11
	 */
	public void setCategory11(String category11) {
		this.category11 = category11;
	}


	/**
	 * Gets the category12.
	 *
	 * @return the category12
	 */
	public String getCategory12() {
		return category12;
	}


	/**
	 * Sets the category12.
	 *
	 * @param category12 the new category12
	 */
	public void setCategory12(String category12) {
		this.category12 = category12;
	}


	/**
	 * Gets the distinct units.
	 *
	 * @return the distinct units
	 */
	public Map<String, Object> getDistinctUnits() {
		return distinctUnits;
	}


	/**
	 * Sets the distinct units.
	 *
	 * @param distinctUnits the distinct units
	 */
	public void setDistinctUnits(Map<String, Object> distinctUnits) {
		this.distinctUnits = distinctUnits;
	}


	/**
	 * Gets the distinct frequency.
	 *
	 * @return the distinct frequency
	 */
	public Map<String, Object> getDistinctFrequency() {
		return distinctFrequency;
	}


	/**
	 * Sets the distinct frequency.
	 *
	 * @param distinctFrequency the distinct frequency
	 */
	public void setDistinctFrequency(Map<String, Object> distinctFrequency) {
		this.distinctFrequency = distinctFrequency;
	}


	/**
	 * Gets the distinct seasonal adj.
	 *
	 * @return the distinct seasonal adj
	 */
	public Map<String, Object> getDistinctSeasonalAdj() {
		return distinctSeasonalAdj;
	}


	/**
	 * Sets the distinct seasonal adj.
	 *
	 * @param distinctSeasonalAdj the distinct seasonal adj
	 */
	public void setDistinctSeasonalAdj(Map<String, Object> distinctSeasonalAdj) {
		this.distinctSeasonalAdj = distinctSeasonalAdj;
	}


	/**
	 * Gets the distinct last updated.
	 *
	 * @return the distinct last updated
	 */
	public Map<String, Object> getDistinctLastUpdated() {
		return distinctLastUpdated;
	}


	/**
	 * Sets the distinct last updated.
	 *
	 * @param distinctLastUpdated the distinct last updated
	 */
	public void setDistinctLastUpdated(Map<String, Object> distinctLastUpdated) {
		this.distinctLastUpdated = distinctLastUpdated;
	}


	/**
	 * Gets the distinct date series.
	 *
	 * @return the distinct date series
	 */
	public Map<String, Object> getDistinctDateSeries() {
		return distinctDateSeries;
	}


	/**
	 * Sets the distinct date series.
	 *
	 * @param distinctDateSeries the distinct date series
	 */
	public void setDistinctDateSeries(Map<String, Object> distinctDateSeries) {
		this.distinctDateSeries = distinctDateSeries;
	}


	/**
	 * Gets the distinct value.
	 *
	 * @return the distinct value
	 */
	public Map<String, Object> getDistinctValue() {
		return distinctValue;
	}


	/**
	 * Sets the distinct value.
	 *
	 * @param distinctValue the distinct value
	 */
	public void setDistinctValue(Map<String, Object> distinctValue) {
		this.distinctValue = distinctValue;
	}


	/**
	 * Gets the distinct country.
	 *
	 * @return the distinct country
	 */
	public Map<String, Object> getDistinctCountry() {
		return distinctCountry;
	}


	/**
	 * Sets the distinct country.
	 *
	 * @param distinctCountry the distinct country
	 */
	public void setDistinctCountry(Map<String, Object> distinctCountry) {
		this.distinctCountry = distinctCountry;
	}


	/**
	 * Gets the distinct city.
	 *
	 * @return the distinct city
	 */
	public Map<String, Object> getDistinctCity() {
		return distinctCity;
	}


	/**
	 * Sets the distinct city.
	 *
	 * @param distinctCity the distinct city
	 */
	public void setDistinctCity(Map<String, Object> distinctCity) {
		this.distinctCity = distinctCity;
	}


	/**
	 * Gets the distinct county.
	 *
	 * @return the distinct county
	 */
	public Map<String, Object> getDistinctCounty() {
		return distinctCounty;
	}


	/**
	 * Sets the distinct county.
	 *
	 * @param distinctCounty the distinct county
	 */
	public void setDistinctCounty(Map<String, Object> distinctCounty) {
		this.distinctCounty = distinctCounty;
	}


	/**
	 * Gets the distinct state.
	 *
	 * @return the distinct state
	 */
	public Map<String, Object> getDistinctState() {
		return distinctState;
	}


	/**
	 * Sets the distinct state.
	 *
	 * @param distinctState the distinct state
	 */
	public void setDistinctState(Map<String, Object> distinctState) {
		this.distinctState = distinctState;
	}


	/**
	 * Gets the distinct region us.
	 *
	 * @return the distinct region us
	 */
	public Map<String, Object> getDistinctRegionUS() {
		return distinctRegionUS;
	}


	/**
	 * Sets the distinct region us.
	 *
	 * @param distinctRegionUS the distinct region us
	 */
	public void setDistinctRegionUS(Map<String, Object> distinctRegionUS) {
		this.distinctRegionUS = distinctRegionUS;
	}


	/**
	 * Gets the distinct region world.
	 *
	 * @return the distinct region world
	 */
	public Map<String, Object> getDistinctRegionWorld() {
		return distinctRegionWorld;
	}


	/**
	 * Sets the distinct region world.
	 *
	 * @param distinctRegionWorld the distinct region world
	 */
	public void setDistinctRegionWorld(Map<String, Object> distinctRegionWorld) {
		this.distinctRegionWorld = distinctRegionWorld;
	}


	/**
	 * Gets the distinct institution.
	 *
	 * @return the distinct institution
	 */
	public Map<String, Object> getDistinctInstitution() {
		return distinctInstitution;
	}


	/**
	 * Sets the distinct institution.
	 *
	 * @param distinctInstitution the distinct institution
	 */
	public void setDistinctInstitution(Map<String, Object> distinctInstitution) {
		this.distinctInstitution = distinctInstitution;
	}


	/**
	 * Gets the distinct frb district.
	 *
	 * @return the distinct frb district
	 */
	public Map<String, Object> getDistinctFrbDistrict() {
		return distinctFrbDistrict;
	}


	/**
	 * Sets the distinct frb district.
	 *
	 * @param distinctFrbDistrict the distinct frb district
	 */
	public void setDistinctFrbDistrict(Map<String, Object> distinctFrbDistrict) {
		this.distinctFrbDistrict = distinctFrbDistrict;
	}


	/**
	 * Gets the distinct sex.
	 *
	 * @return the distinct sex
	 */
	public Map<String, Object> getDistinctSex() {
		return distinctSex;
	}


	/**
	 * Sets the distinct sex.
	 *
	 * @param distinctSex the distinct sex
	 */
	public void setDistinctSex(Map<String, Object> distinctSex) {
		this.distinctSex = distinctSex;
	}


	/**
	 * Gets the distinct currency.
	 *
	 * @return the distinct currency
	 */
	public Map<String, Object> getDistinctCurrency() {
		return distinctCurrency;
	}


	/**
	 * Sets the distinct currency.
	 *
	 * @param distinctCurrency the distinct currency
	 */
	public void setDistinctCurrency(Map<String, Object> distinctCurrency) {
		this.distinctCurrency = distinctCurrency;
	}


	/**
	 * Gets the sort by.
	 *
	 * @return the sort by
	 */
	public String getSortBy() {
		return sortBy;
	}


	/**
	 * Sets the sort by.
	 *
	 * @param sortBy the new sort by
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
}

