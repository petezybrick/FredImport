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
 * The Class SeriesIdItem.
 */
public class SeriesIdItem implements Comparable<SeriesIdItem> {
	
	/** The csv file name. */
	private String csvFileName;
	
	/** The title. */
	private String title;
	
	/** The units. */
	private String units;
	
	/** The frequency. */
	private String frequency;
	
	/** The seasonal adj. */
	private String seasonalAdj;
	
	/** The last updated. */
	private String lastUpdated;
	
	/**
	 * Instantiates a new series id item.
	 */
	public SeriesIdItem() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SeriesIdItem that) {
		return this.title.compareTo( that.title );
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
	 * @return the series id item
	 */
	public SeriesIdItem setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
		return this;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the title
	 * @return the series id item
	 */
	public SeriesIdItem setTitle(String title) {
		this.title = title;
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
	 * @return the series id item
	 */
	public SeriesIdItem setUnits(String units) {
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
	 * @return the series id item
	 */
	public SeriesIdItem setFrequency(String frequency) {
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
	 * @return the series id item
	 */
	public SeriesIdItem setSeasonalAdj(String seasonalAdj) {
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
	 * @return the series id item
	 */
	public SeriesIdItem setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;
	}
	
	/**
	 * To tsv header.
	 *
	 * @return the string
	 */
	public static String toTsvHeader() {
		return "File\tTitle\tUnits\tFrequency\tSeasonal Adjustment\tLast Updated";
	}
	
	/**
	 * To tsv data.
	 *
	 * @return the string
	 */
	public String toTsvData() {
		return csvFileName + "\t" + title + "\t" + units + "\t" + frequency + "\t" + seasonalAdj + "\t" + lastUpdated;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SeriesIdItem [csvFileName=" + csvFileName + ", title=" + title + ", units=" + units + ", frequency=" + frequency + ", seasonalAdj="
				+ seasonalAdj + ", lastUpdated=" + lastUpdated + "]";
	}

}
