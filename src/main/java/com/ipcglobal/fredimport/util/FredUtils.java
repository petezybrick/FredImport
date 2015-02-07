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
package com.ipcglobal.fredimport.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;


/**
 * The Class FredUtils.
 */
public class FredUtils {

	/**
	 * Creates a tar.gz file at the specified path with the contents of the specified directory.
	 *
	 * @param directoryPath the directory path
	 * @param tarGzPath the tar gz path
	 * @throws IOException             If anything goes wrong
	 */
	public static void createTarGzOfDirectory(String directoryPath, String tarGzPath) throws IOException {
		FileOutputStream fOut = null;
		BufferedOutputStream bOut = null;
		GzipCompressorOutputStream gzOut = null;
		TarArchiveOutputStream tOut = null;
		try {
			fOut = new FileOutputStream(new File(tarGzPath));
			bOut = new BufferedOutputStream(fOut);
			gzOut = new GzipCompressorOutputStream(bOut);
			tOut = new TarArchiveOutputStream(gzOut);
			addFileToTarGz(tOut, directoryPath, "/");
		} finally {
			tOut.finish();
			tOut.close();
			gzOut.close();
			bOut.close();
			fOut.close();
		}
	}

	/**
	 * Creates a tar entry for the path specified with a name built from the base passed in and the file/directory name. If the path is a directory, a recursive
	 * call is made such that the full directory is added to the tar.
	 *
	 * @param tOut
	 *            The tar file's output stream
	 * @param path
	 *            The filesystem path of the file/directory being added
	 * @param base
	 *            The base prefix to for the name of the tar file entry
	 *
	 * @throws IOException
	 *             If anything goes wrong
	 */
	private static void addFileToTarGz(TarArchiveOutputStream tOut, String path, String base) throws IOException {
		try {
			File f = new File(path);
			String entryName = base + f.getName();
			TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
			tOut.putArchiveEntry(tarEntry);
			if (f.isFile()) {
				// had to do this in 3 steps due to memory leak as per http://stackoverflow.com/questions/13461393/compress-directory-to-tar-gz-with-commons-compress/23524963#23524963 
		        FileInputStream in = new FileInputStream(f);
		        IOUtils.copy(in, tOut);
		        in.close();
		        
				tOut.closeArchiveEntry();
			} else {
				tOut.closeArchiveEntry();
				File[] children = f.listFiles();
				if (children != null) {
					for (File child : children) {
						addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/" );
					}
				}
			}
		} catch( Exception e ) {
			System.out.println(e);
			e.printStackTrace();
			throw e;
		}
	}

	
	/**
	 * Convert m secs to h mm ss.
	 *
	 * @param msecs the msecs
	 * @return the string
	 */
	public static String convertMSecsToHMmSs(long msecs) {
		return convertSecsToHMmSs(msecs/1000L);
	}
	
	
	/**
	 * Convert secs to h mm ss.
	 *
	 * @param seconds the seconds
	 * @return the string
	 */
	public static String convertSecsToHMmSs(long seconds) {
	    long s = seconds % 60;
	    long m = (seconds / 60) % 60;
	    long h = (seconds / (60 * 60)) % 24;
	    return String.format("%02d:%02d:%02d", h,m,s);
	}

	
	/**
	 * Readfix path.
	 *
	 * @param propertyName the property name
	 * @param properties the properties
	 * @return the string
	 */
	public static String readfixPath( String propertyName, Properties properties ) {
		String pathName = properties.getProperty( propertyName ).trim();
		if ( pathName.endsWith(File.separator)) return pathName;
		else return pathName + File.separator;
	}

}
