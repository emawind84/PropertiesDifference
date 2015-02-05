package util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class PropertiesDifference {
	
	private static boolean onlyDiff = true;

	public static void main(String args[]) {
		String testOldFile = "C:/Users/Disco/Downloads/message_ko-19632.properties";
		String testNewFile = "C:/Users/Disco/Downloads/message_ko.properties";
		String resultFile = "C:/Users/Disco/Downloads/result_ko.properties";
		
		try{
			doCheck(testOldFile, testNewFile, resultFile);
		}finally{
			System.exit(0);
		}
	}
	
	public static void doCheck(String oldfile, String newfile, String result){
		Properties propertiesOld = new Properties();
		Properties propertiesNew = new Properties();
		File fileOld = new File(oldfile);
		File fileNew = new File(newfile);
		File fileTest = new File(result);
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		BufferedReader reader1 = null;
		
		try {
			propertiesOld.load(new FileInputStream(fileOld));
			propertiesNew.load(new FileInputStream(fileNew));
			
			reader = new BufferedReader(new InputStreamReader(
					new DataInputStream(new FileInputStream(fileOld)),"ISO-8859-1"));
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileTest),"ISO-8859-1"));
			String valueNew = "";
			String valueOld = "";
			String propertyName = "";

			
			while ((propertyName = reader.readLine()) != null) {
			//read the property key from p1 file
				String[] split=propertyName.split("=");
				propertyName=split[0].trim();
				
				//get the property value from p1 and p2 files
				valueOld = propertiesOld.getProperty(propertyName);
				valueNew = propertiesNew.getProperty(propertyName);
				
				//if there no value in p1 file just write the property key
				if(valueOld==null && !onlyDiff){
					writer.append(propertyName);
					writer.append("\n");
				}else{
				//if there is no value for the property key in p2 file  use the value in the p1 file
					if (valueNew == null && !onlyDiff) {
						writer.append(propertyName+"="+valueOld);
						writer.append("\n");
					} else {
						//else for the property key write the value that is in p2 file
						if(!valueNew.equals(valueOld)){
							writer.append("#Modified old: " + valueOld + " new: " + valueNew + "\n");
							writer.append(propertyName+"="+valueNew);
							writer.append("\n");
						} else if( !onlyDiff ) {
							writer.append(propertyName+"="+valueNew);
							writer.append("\n");
						}
					}

				}
				
			}
			// finally write the properties that are only in p2 file
			reader1=new BufferedReader(new InputStreamReader(
					new DataInputStream(new FileInputStream(fileNew))));
			writer.append("\n\n\n\n");
			writer.append("#New added properties");
			writer.append("\n\n\n\n");
			
			while ((propertyName = reader1.readLine()) != null) {
				String[] split=propertyName.split("=");
				propertyName=split[0].trim();
				
				valueOld = propertiesOld.getProperty(propertyName);
				valueNew = propertiesNew.getProperty(propertyName);
				if (valueOld == null) {
					
					writer.append(propertyName+"="+valueNew);
				    writer.append("\n");
				} 

			}
			 writer.flush();
			 
			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(reader1);
			IOUtils.closeQuietly(writer);
		}
	}
}