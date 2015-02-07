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

/**
 * The Class FredItem.
 */
public class FredItem {
	
	/** The csv file name. */
	private String csvFileName;
	
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
	
	/** The units. */
	private String units;
	
	/** The frequency. */
	private String frequency;
	
	/** The seasonal adj. */
	private String seasonalAdj;
	
	/** The last updated. */
	private String lastUpdated;
	
	/** The date series. */
	private String dateSeries;
	
	/** The value. */
	private String value;
	
	/** The country. */
	private String country;
	
	/** The city. */
	private String city;
	
	/** The county. */
	private String county;
	
	/** The state. */
	private String state;
	
	/** The region us. */
	private String regionUS;
	
	/** The region world. */
	private String regionWorld;
	
	/** The institution. */
	private String institution;
	
	/** The frb district. */
	private String frbDistrict;
	
	/** The sex. */
	private String sex;
	
	/** The currency. */
	private String currency;
	
	/**
	 * Instantiates a new fred item.
	 *
	 * @param seriesIdItem the series id item
	 */
	public FredItem( SeriesIdItem seriesIdItem ) {
		super();
		this.csvFileName = seriesIdItem.getCsvFileName().replace("\\","/");
		this.units = seriesIdItem.getUnits();
		this.frequency = seriesIdItem.getFrequency();
		this.seasonalAdj = seriesIdItem.getSeasonalAdj().toUpperCase();		// sometimes they are in lowercase, i.e. nsa
		this.lastUpdated = seriesIdItem.getLastUpdated();
	}
	

	/**
	 * Gets the csv file name.
	 *
	 * @return the csv file name
	 */
	public String getCsvFileName() {
		return csvFileName;
	}

	/**
	 * Sets the csv file name.
	 *
	 * @param csvFileName the csv file name
	 * @return the fred item
	 */
	public FredItem setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
		return this;
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
	 * @param category1 the category1
	 * @return the fred item
	 */
	public FredItem setCategory1(String category1) {
		this.category1 = category1;
		return this;
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
	 * @param category2 the category2
	 * @return the fred item
	 */
	public FredItem setCategory2(String category2) {
		this.category2 = category2;
		return this;
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
	 * @param category3 the category3
	 * @return the fred item
	 */
	public FredItem setCategory3(String category3) {
		this.category3 = category3;
		return this;
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
	 * @param category4 the category4
	 * @return the fred item
	 */
	public FredItem setCategory4(String category4) {
		this.category4 = category4;
		return this;
	}

	/**
	 * Gets the units.
	 *
	 * @return the units
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * Sets the units.
	 *
	 * @param units the units
	 * @return the fred item
	 */
	public FredItem setUnits(String units) {
		this.units = units;
		return this;
	}

	/**
	 * Gets the frequency.
	 *
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * Sets the frequency.
	 *
	 * @param frequency the frequency
	 * @return the fred item
	 */
	public FredItem setFrequency(String frequency) {
		this.frequency = frequency;
		return this;
	}

	/**
	 * Gets the seasonal adj.
	 *
	 * @return the seasonal adj
	 */
	public String getSeasonalAdj() {
		return seasonalAdj;
	}

	/**
	 * Sets the seasonal adj.
	 *
	 * @param seasonalAdj the seasonal adj
	 * @return the fred item
	 */
	public FredItem setSeasonalAdj(String seasonalAdj) {
		this.seasonalAdj = seasonalAdj;
		return this;
	}

	/**
	 * Gets the last updated.
	 *
	 * @return the last updated
	 */
	public String getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Sets the last updated.
	 *
	 * @param lastUpdated the last updated
	 * @return the fred item
	 */
	public FredItem setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;
	}

	/**
	 * Gets the date series.
	 *
	 * @return the date series
	 */
	public String getDateSeries() {
		return dateSeries;
	}

	/**
	 * Sets the date series.
	 *
	 * @param dateSeries the date series
	 * @return the fred item
	 */
	public FredItem setDateSeries(String dateSeries) {
		this.dateSeries = dateSeries;
		return this;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the value
	 * @return the fred item
	 */
	public FredItem setValue(String value) {
		this.value = value;
		return this;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets the country.
	 *
	 * @param country the country
	 * @return the fred item
	 */
	public FredItem setCountry(String country) {
		this.country = country;
		return this;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the city
	 * @return the fred item
	 */
	public FredItem setCity(String city) {
		this.city = city;
		return this;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the state
	 * @return the fred item
	 */
	public FredItem setState(String state) {
		this.state = state;
		return this;
	}

	/**
	 * Gets the region us.
	 *
	 * @return the region us
	 */
	public String getRegionUS() {
		return regionUS;
	}

	/**
	 * Sets the region us.
	 *
	 * @param regionUS the region us
	 * @return the fred item
	 */
	public FredItem setRegionUS(String regionUS) {
		this.regionUS = regionUS;
		return this;
	}

	/**
	 * Gets the sex.
	 *
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Sets the sex.
	 *
	 * @param sex the sex
	 * @return the fred item
	 */
	public FredItem setSex(String sex) {
		this.sex = sex;
		return this;
	}

	/**
	 * Gets the currency.
	 *
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Sets the currency.
	 *
	 * @param currency the currency
	 * @return the fred item
	 */
	public FredItem setCurrency(String currency) {
		this.currency = currency;
		return this;
	}

	/**
	 * Gets the county.
	 *
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * Sets the county.
	 *
	 * @param county the county
	 * @return the fred item
	 */
	public FredItem setCounty(String county) {
		this.county = county;
		return this;
	}

	/**
	 * Gets the region world.
	 *
	 * @return the region world
	 */
	public String getRegionWorld() {
		return regionWorld;
	}

	/**
	 * Sets the region world.
	 *
	 * @param regionWorld the region world
	 * @return the fred item
	 */
	public FredItem setRegionWorld(String regionWorld) {
		this.regionWorld = regionWorld;
		return this;
	}

	/**
	 * Gets the frb district.
	 *
	 * @return the frb district
	 */
	public String getFrbDistrict() {
		return frbDistrict;
	}

	/**
	 * Sets the frb district.
	 *
	 * @param frbDistrict the frb district
	 * @return the fred item
	 */
	public FredItem setFrbDistrict(String frbDistrict) {
		this.frbDistrict = frbDistrict;
		return this;
	}

	/**
	 * Gets the institution.
	 *
	 * @return the institution
	 */
	public String getInstitution() {
		return institution;
	}

	/**
	 * Sets the institution.
	 *
	 * @param institution the new institution
	 */
	public void setInstitution(String institution) {
		this.institution = institution;
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
	 * @param category5 the category5
	 * @return the fred item
	 */
	public FredItem setCategory5(String category5) {
		this.category5 = category5;
		return this;
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
	 * @param category6 the category6
	 * @return the fred item
	 */
	public FredItem setCategory6(String category6) {
		this.category6 = category6;
		return this;
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
	 * @param category7 the category7
	 * @return the fred item
	 */
	public FredItem setCategory7(String category7) {
		this.category7 = category7;
		return this;
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
	 * @param category8 the category8
	 * @return the fred item
	 */
	public FredItem setCategory8(String category8) {
		this.category8 = category8;
		return this;
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
	 * @param category9 the category9
	 * @return the fred item
	 */
	public FredItem setCategory9(String category9) {
		this.category9 = category9;
		return this;
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
	 * @param category10 the category10
	 * @return the fred item
	 */
	public FredItem setCategory10(String category10) {
		this.category10 = category10;
		return this;
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
	 * @param category11 the category11
	 * @return the fred item
	 */
	public FredItem setCategory11(String category11) {
		this.category11 = category11;
		return this;
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
	 * @param category12 the category12
	 * @return the fred item
	 */
	public FredItem setCategory12(String category12) {
		this.category12 = category12;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FredItem [csvFileName=" + csvFileName + ", category1=" + category1 + ", category2=" + category2 + ", category3=" + category3 + ", category4="
				+ category4 + ", category5=" + category5 + ", category6=" + category6 + ", category7=" + category7 + ", category8=" + category8
				+ ", category9=" + category9 + ", category10=" + category10 + ", category11=" + category11 + ", category12=" + category12 + ", units=" + units
				+ ", frequency=" + frequency + ", seasonalAdj=" + seasonalAdj + ", lastUpdated=" + lastUpdated + ", dateSeries=" + dateSeries + ", value="
				+ value + ", country=" + country + ", city=" + city + ", county=" + county + ", state=" + state + ", regionUS=" + regionUS + ", regionWorld="
				+ regionWorld + ", institution=" + institution + ", frbDistrict=" + frbDistrict + ", sex=" + sex + ", currency=" + currency + "]";
	}
	
	/**
	 * Null as empty string.
	 *
	 * @param in the in
	 * @return the string
	 */
	private static String nullAsEmptyString( String in ) {
		return (in != null ) ? in : "";
	}
	
	/**
	 * To tsv.
	 *
	 * @return the string
	 */
	public String toTsv() {
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
			nullAsEmptyString(units) + "\t" +
			nullAsEmptyString(frequency) + "\t" +
			nullAsEmptyString(seasonalAdj) + "\t" +
			nullAsEmptyString(lastUpdated) + "\t" +
			nullAsEmptyString(dateSeries) + "\t" +
			nullAsEmptyString(value) + "\t" +
			nullAsEmptyString(country) + "\t" +
			nullAsEmptyString(city) + "\t" +
			nullAsEmptyString(county) + "\t" +
			nullAsEmptyString(state) + "\t" +
			nullAsEmptyString(regionUS) + "\t" +
			nullAsEmptyString(regionWorld) + "\t" +
			nullAsEmptyString(institution) + "\t" +
			nullAsEmptyString(frbDistrict) + "\t" +
			nullAsEmptyString(sex) + "\t" +
			nullAsEmptyString(currency);
	}



	/**
	 * Hash code categories.
	 *
	 * @return the int
	 */
	public int hashCodeCategories() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category1 == null) ? 0 : category1.hashCode());
		result = prime * result + ((category10 == null) ? 0 : category10.hashCode());
		result = prime * result + ((category11 == null) ? 0 : category11.hashCode());
		result = prime * result + ((category12 == null) ? 0 : category12.hashCode());
		result = prime * result + ((category2 == null) ? 0 : category2.hashCode());
		result = prime * result + ((category3 == null) ? 0 : category3.hashCode());
		result = prime * result + ((category4 == null) ? 0 : category4.hashCode());
		result = prime * result + ((category5 == null) ? 0 : category5.hashCode());
		result = prime * result + ((category6 == null) ? 0 : category6.hashCode());
		result = prime * result + ((category7 == null) ? 0 : category7.hashCode());
		result = prime * result + ((category8 == null) ? 0 : category8.hashCode());
		result = prime * result + ((category9 == null) ? 0 : category9.hashCode());
		return result;
	}



	/**
	 * Equals categories.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	public boolean equalsCategories(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FredItem other = (FredItem) obj;
		if (category1 == null) {
			if (other.category1 != null)
				return false;
		} else if (!category1.equals(other.category1))
			return false;
		if (category10 == null) {
			if (other.category10 != null)
				return false;
		} else if (!category10.equals(other.category10))
			return false;
		if (category11 == null) {
			if (other.category11 != null)
				return false;
		} else if (!category11.equals(other.category11))
			return false;
		if (category12 == null) {
			if (other.category12 != null)
				return false;
		} else if (!category12.equals(other.category12))
			return false;
		if (category2 == null) {
			if (other.category2 != null)
				return false;
		} else if (!category2.equals(other.category2))
			return false;
		if (category3 == null) {
			if (other.category3 != null)
				return false;
		} else if (!category3.equals(other.category3))
			return false;
		if (category4 == null) {
			if (other.category4 != null)
				return false;
		} else if (!category4.equals(other.category4))
			return false;
		if (category5 == null) {
			if (other.category5 != null)
				return false;
		} else if (!category5.equals(other.category5))
			return false;
		if (category6 == null) {
			if (other.category6 != null)
				return false;
		} else if (!category6.equals(other.category6))
			return false;
		if (category7 == null) {
			if (other.category7 != null)
				return false;
		} else if (!category7.equals(other.category7))
			return false;
		if (category8 == null) {
			if (other.category8 != null)
				return false;
		} else if (!category8.equals(other.category8))
			return false;
		if (category9 == null) {
			if (other.category9 != null)
				return false;
		} else if (!category9.equals(other.category9))
			return false;
		return true;
	}
	

}
