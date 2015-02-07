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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;


/**
 * The Class Reference.
 */
public class Reference {
	
	/** The Constant FILENAME_COUNTRIES. */
	private static final String FILENAME_COUNTRIES = "countries.txt";
	
	/** The Constant FILENAME_CURRENCIES_COUNTRIES. */
	private static final String FILENAME_CURRENCIES_COUNTRIES = "currencies_countries.txt";
	
	/** The Constant FILENAME_US_STATE_NAMES_ABBREVS. */
	private static final String FILENAME_US_STATE_NAMES_ABBREVS = "us_state_names_abbrevs.txt";
	
	/** The Constant FILENAME_US_CITIES_STATES. */
	private static final String FILENAME_US_CITIES_STATES = "us_cities_states.txt";
	
	/** The Constant FILENAME_WORLD_REGIONS. */
	private static final String FILENAME_WORLD_REGIONS = "world_regions.txt";
	
	/** The Constant FILENAME_INSTITUTIONS. */
	private static final String FILENAME_INSTITUTIONS = "institutions.txt";
	
	/** The Constant FILENAME_SEXES. */
	private static final String FILENAME_SEXES = "sexes.txt";
	
	/** The Constant europePrefixes. */
	private static final String[] europePrefixes = {"Euro Area", "Euro area", "European Economic Area", "European Union", "the Euro Area", "the Euro area", "the European Economic Area", "the European Union"};
	
	/** The ref cities by states. */
	private Map<String, String> refCitiesByStates;
	
	/** The ref state abbrevs by names. */
	private Map<String, String> refStateAbbrevsByNames;
	
	/** The ref valid state abbrevs. */
	private Map<String, Object> refValidStateAbbrevs;
	
	/** The ref countries. */
	private Map<String, String> refCountries;
	
	/** The ref countries by currencies. */
	private Map<String, String> refCountriesByCurrencies;
	
	/** The ref currencies by countries. */
	private Map<String, String> refCurrenciesByCountries;
	
	/** The ref world regions. */
	private Map<String, Object> refWorldRegions;
	
	/** The ref institutions. */
	private Map<String, Object> refInstitutions;
	
	/**  The ref sexes. */
	private Map<String, String> refSexes;

	
	/**
	 * Instantiates a new reference.
	 *
	 * @param path the path
	 * @throws Exception the exception
	 */
	public Reference( String path ) throws Exception {
		if( !path.endsWith(File.separator)) path = path + File.separator;
		createRefUSStates( path );
		createRefCountries( path );
		createRefCurrenciesCountries( path );
		createRefWorldRegions( path );
		createRefInstitutions( path );
		createRefUSCitiesStates( path );
		createRefSexes( path );
	}
	
	
	/**
	 * Creates the ref sexes.
	 *
	 * @param path the path
	 * @throws Exception the exception
	 */
	private void createRefSexes( String path ) throws Exception {
		refSexes = new HashMap<String, String>();

		LineIterator it = FileUtils.lineIterator(new File( path+FILENAME_SEXES ), "UTF-8");
		try {
			while( it.hasNext() ) {
				// Format: <identifier>|<M|F|A>
				// Example: All Persons|A
				String line = it.nextLine();
				String[] fields = line.split("[|]");
				refSexes.put( fields[0].trim(), fields[1].trim() );
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
	}
	

	/**
	 * Check europe prefixes.
	 *
	 * @param lastCategory the last category
	 * @return true, if successful
	 */
	public boolean checkEuropePrefixes( String lastCategory ) {
		return StringUtils.startsWithAny( lastCategory, europePrefixes);
	}
	
	
	/**
	 * Creates the ref us cities states.
	 *
	 * @param path the path
	 * @throws Exception the exception
	 */
	private void createRefUSCitiesStates( String path ) throws Exception {
		refCitiesByStates = new HashMap<String, String>();

		LineIterator it = FileUtils.lineIterator(new File( path+FILENAME_US_CITIES_STATES ), "UTF-8");
		try {
			while( it.hasNext() ) {
				// Format: <cities>|<AA-BB-CC>
				String line = it.nextLine();
				String[] fields = line.split("[|]");
				fields[0] = fields[0].trim();
				fields[1] = fields[1].trim();
				refCitiesByStates.put( fields[1], fields[0] );
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
	}
	
	/**
	 * Creates the ref us states.
	 *
	 * @param path the path
	 * @throws Exception the exception
	 */
	private void createRefUSStates( String path ) throws Exception {
		refStateAbbrevsByNames = new HashMap<String, String>();
		refValidStateAbbrevs = new HashMap<String, Object>();

		LineIterator it = FileUtils.lineIterator(new File( path+FILENAME_US_STATE_NAMES_ABBREVS ), "UTF-8");
		try {
			while( it.hasNext() ) {
				// Format: <stateName>|<stateAbbrev>
				String line = it.nextLine();
				String[] fields = line.split("[|]");
				fields[0] = fields[0].trim();
				fields[1] = fields[1].trim();
				refStateAbbrevsByNames.put( fields[0], fields[1] );
				refValidStateAbbrevs.put( fields[1],  null );
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
	}
	
	
	/**
	 * Creates the ref countries.
	 *
	 * @param path the path
	 * @throws Exception the exception
	 */
	private void createRefCountries( String path ) throws Exception {
		refCountries = new HashMap<String, String>();
		
		LineIterator it = FileUtils.lineIterator(new File( path+FILENAME_COUNTRIES ), "UTF-8");
		try {
			while( it.hasNext() ) {
				// Format: <commonCountryName>|akaCountryName1|akaCountryName2|...
				// For example: United States|the U.S.|the United States
				//	 All three will match as a valid country, and "United States" will always be used as the common country name
				String[] countries = it.nextLine().split("[|]");
				String commonCountryName = countries[0].trim();
				refCountries.put( commonCountryName, commonCountryName );
				for( int i=1 ; i<countries.length ; i++ ) 
					refCountries.put( countries[i].trim(), commonCountryName );
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
	}	
	
	
	/**
	 * Creates the ref currencies countries.
	 *
	 * @param path the path
	 * @throws Exception the exception
	 */
	private void createRefCurrenciesCountries( String path ) throws Exception {
		refCountriesByCurrencies = new HashMap<String, String>();
		refCurrenciesByCountries = new HashMap<String, String>();
		
		LineIterator it = FileUtils.lineIterator(new File( path+FILENAME_CURRENCIES_COUNTRIES ), "UTF-8");
		try {
			while( it.hasNext() ) {
				// Format: <countryName>|<primaryCurrencyName>|<akaCurrencyName1>|<akaCurrencyName2>|...
				String line = it.nextLine();
				String[] fields = line.split("[|]");
				for( int i=0 ; i<fields.length ; i++ ) fields[i] = fields[i].trim();
				fields[1] = fields[1].trim();
				// When looking up by country, always return the primary currency
				refCurrenciesByCountries.put( fields[0], fields[1] );
				for( int i=1 ; i<fields.length ; i++ ) 
					refCountriesByCurrencies.put( fields[i], fields[0] );
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
	}
	
	
	/**
	 * Creates the ref world regions.
	 *
	 * @param path the path
	 * @throws Exception the exception
	 */
	private void createRefWorldRegions( String path ) throws Exception {
		refWorldRegions = new HashMap<String, Object>();
		
		LineIterator it = FileUtils.lineIterator(new File( path+FILENAME_WORLD_REGIONS ), "UTF-8");
		try {
			while( it.hasNext() ) {
				// Format: <worldRegion>
				String worldRegion = it.nextLine().trim();
				refWorldRegions.put( worldRegion, null );
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
	}
	
	
	/**
	 * Creates the ref institutions.
	 *
	 * @param path the path
	 * @throws Exception the exception
	 */
	private void createRefInstitutions( String path ) throws Exception {
		refInstitutions = new HashMap<String, Object>();
		
		LineIterator it = FileUtils.lineIterator(new File( path+FILENAME_INSTITUTIONS ), "UTF-8");
		try {
			while( it.hasNext() ) {
				// Format: <institution>
				String institution = it.nextLine().trim();
				refWorldRegions.put( institution, null );
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
	}


	/**
	 * Gets the ref state abbrevs by names.
	 *
	 * @return the ref state abbrevs by names
	 */
	public Map<String, String> getRefStateAbbrevsByNames() {
		return refStateAbbrevsByNames;
	}


	/**
	 * Gets the ref countries.
	 *
	 * @return the ref countries
	 */
	public Map<String, String> getRefCountries() {
		return refCountries;
	}


	/**
	 * Gets the ref countries by currencies.
	 *
	 * @return the ref countries by currencies
	 */
	public Map<String, String> getRefCountriesByCurrencies() {
		return refCountriesByCurrencies;
	}


	/**
	 * Gets the ref currencies by countries.
	 *
	 * @return the ref currencies by countries
	 */
	public Map<String, String> getRefCurrenciesByCountries() {
		return refCurrenciesByCountries;
	}

	/**
	 * Gets the ref world regions.
	 *
	 * @return the ref world regions
	 */
	public Map<String, Object> getRefWorldRegions() {
		return refWorldRegions;
	}

	/**
	 * Gets the ref institutions.
	 *
	 * @return the ref institutions
	 */
	public Map<String, Object> getRefInstitutions() {
		return refInstitutions;
	}


	/**
	 * Gets the ref valid state abbrevs.
	 *
	 * @return the ref valid state abbrevs
	 */
	public Map<String, Object> getRefValidStateAbbrevs() {
		return refValidStateAbbrevs;
	}

	/**
	 * Gets the ref cities by states.
	 *
	 * @return the ref cities by states
	 */
	public Map<String, String> getRefCitiesByStates() {
		return refCitiesByStates;
	}


	/**
	 * Gets the ref sexes.
	 *
	 * @return the ref sexes
	 */
	public Map<String, String> getRefSexes() {
		return refSexes;
	}

}
