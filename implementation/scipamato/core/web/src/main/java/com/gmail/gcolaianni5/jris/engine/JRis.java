/*
 * JRis.java
 * 
 * 22 apr 2017
 */
package com.gmail.gcolaianni5.jris.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.gcolaianni5.jris.bean.Record;
import com.gmail.gcolaianni5.jris.bean.Type;
import com.gmail.gcolaianni5.jris.exception.JRisException;

/**
 * {@code RIS} format parsers and builder. </br>
 * This is the core class of the module. It is capable to parse a {@code RIS} file from different source types
 * and to build a well formatted {@code RIS} format.
 *
 * @author Gianluca Colaianni -- g.colaianni5@gmail.com
 * @version 1.0
 * @since 22 apr 2017
 */
public class JRis {

	/*
	 * 
	 */
	private final static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");

	/*
	 * End record code.
	 */
	private final static String ER = "ER";

	/*
	 * PDF link code.
	 */
	private final static String L1 = "L1";

	/*
	 * Abstract code.
	 */
	private final static String AB = "AB";

	/*
	 * Tag separator.
	 */
	private final static String TAG_SEPARATOR = "  - ";

	/*
	 * Line separator.
	 */
	private final static String LINE_SEPARATOR = System.getProperty("line.separator");

	/*
	 * 
	 */
	private final static LinkedHashMap<String, MethodTypeMapping> TAG_METHOD_DICTIONARY = new LinkedHashMap<String, MethodTypeMapping>();

	static {
		TAG_METHOD_DICTIONARY.put("TY", new MethodTypeMapping("setType", Type.class));
		TAG_METHOD_DICTIONARY.put("A1", new MethodTypeMapping("addFirstAuthor", List.class));
		TAG_METHOD_DICTIONARY.put("A2", new MethodTypeMapping("addSecondaryAuthor", List.class));
		TAG_METHOD_DICTIONARY.put("A3", new MethodTypeMapping("addTertiaryAuthor", List.class));
		TAG_METHOD_DICTIONARY.put("A4", new MethodTypeMapping("addSubsidiaryAuthor", List.class));
		TAG_METHOD_DICTIONARY.put("AB", new MethodTypeMapping("setAbstr", String.class));
		TAG_METHOD_DICTIONARY.put("AA", new MethodTypeMapping("setAuthorAddress", String.class));
		TAG_METHOD_DICTIONARY.put("AN", new MethodTypeMapping("setAccessionNumber", String.class));
		TAG_METHOD_DICTIONARY.put("AU", new MethodTypeMapping("addAuthor", List.class));
		TAG_METHOD_DICTIONARY.put("AV", new MethodTypeMapping("setArchivesLocation", String.class));
		TAG_METHOD_DICTIONARY.put("BT", new MethodTypeMapping("setBt", String.class));
		TAG_METHOD_DICTIONARY.put("C1", new MethodTypeMapping("setCustom1", String.class));
		TAG_METHOD_DICTIONARY.put("C2", new MethodTypeMapping("setCustom2", String.class));
		TAG_METHOD_DICTIONARY.put("C3", new MethodTypeMapping("setCustom3", String.class));
		TAG_METHOD_DICTIONARY.put("C4", new MethodTypeMapping("setCustom4", String.class));
		TAG_METHOD_DICTIONARY.put("C5", new MethodTypeMapping("setCustom5", String.class));
		TAG_METHOD_DICTIONARY.put("C6", new MethodTypeMapping("setCustom6", String.class));
		TAG_METHOD_DICTIONARY.put("C7", new MethodTypeMapping("setCustom7", String.class));
		TAG_METHOD_DICTIONARY.put("C8", new MethodTypeMapping("setCustom8", String.class));
		TAG_METHOD_DICTIONARY.put("CA", new MethodTypeMapping("setCaption", String.class));
		TAG_METHOD_DICTIONARY.put("CN", new MethodTypeMapping("setCallNumber", String.class));
		TAG_METHOD_DICTIONARY.put("CP", new MethodTypeMapping("setCp", String.class));
		TAG_METHOD_DICTIONARY.put("CT", new MethodTypeMapping("setUnpublishedReferenceTitle", String.class));
		TAG_METHOD_DICTIONARY.put("CY", new MethodTypeMapping("setPlacePublished", String.class));
		TAG_METHOD_DICTIONARY.put("DA", new MethodTypeMapping("setDate", String.class));
		TAG_METHOD_DICTIONARY.put("DB", new MethodTypeMapping("setDatabaseName", String.class));
		TAG_METHOD_DICTIONARY.put("DO", new MethodTypeMapping("setDoi", String.class));
		TAG_METHOD_DICTIONARY.put("DP", new MethodTypeMapping("setDatabaseProvider", String.class));
		TAG_METHOD_DICTIONARY.put("ED", new MethodTypeMapping("setEditor", String.class));
		TAG_METHOD_DICTIONARY.put("EP", new MethodTypeMapping("setEndPage", Integer.class));
		TAG_METHOD_DICTIONARY.put("ED", new MethodTypeMapping("setEdition", String.class));
		TAG_METHOD_DICTIONARY.put("ID", new MethodTypeMapping("setReferenceId", String.class));
		TAG_METHOD_DICTIONARY.put("IS", new MethodTypeMapping("setIssueNumber", Integer.class));
		TAG_METHOD_DICTIONARY.put("J1", new MethodTypeMapping("setPeriodicalNameUserAbbrevation", String.class));
		TAG_METHOD_DICTIONARY.put("J2", new MethodTypeMapping("setAlternativeTitle", String.class));
		TAG_METHOD_DICTIONARY.put("JA", new MethodTypeMapping("setPeriodicalNameStandardAbbrevation", String.class));
		TAG_METHOD_DICTIONARY.put("JF", new MethodTypeMapping("setPeriodicalNameFullFormatJF", String.class));
		TAG_METHOD_DICTIONARY.put("JO", new MethodTypeMapping("setPeriodicalNameFullFormatJO", String.class));
		TAG_METHOD_DICTIONARY.put("KW", new MethodTypeMapping("addKeyword", List.class));
		TAG_METHOD_DICTIONARY.put("L1", new MethodTypeMapping("addPdfLink", List.class));
		TAG_METHOD_DICTIONARY.put("L2", new MethodTypeMapping("addFullTextLink", List.class));
		TAG_METHOD_DICTIONARY.put("L3", new MethodTypeMapping("addRelatedRecord", List.class));
		TAG_METHOD_DICTIONARY.put("L4", new MethodTypeMapping("addImages", List.class));
		TAG_METHOD_DICTIONARY.put("LA", new MethodTypeMapping("setLanguage", String.class));
		TAG_METHOD_DICTIONARY.put("LB", new MethodTypeMapping("setLabel", String.class));
		TAG_METHOD_DICTIONARY.put("LK", new MethodTypeMapping("setWebsiteLink", String.class));
		TAG_METHOD_DICTIONARY.put("M1", new MethodTypeMapping("setNumber", String.class));
		TAG_METHOD_DICTIONARY.put("M2", new MethodTypeMapping("setMiscellaneous2", String.class));
		TAG_METHOD_DICTIONARY.put("M3", new MethodTypeMapping("setTypeOfWork", String.class));
		TAG_METHOD_DICTIONARY.put("N1", new MethodTypeMapping("setNotes", String.class));
		TAG_METHOD_DICTIONARY.put("N2", new MethodTypeMapping("setAbstr2", String.class));
		TAG_METHOD_DICTIONARY.put("OP", new MethodTypeMapping("setOriginalPublication", String.class));
		TAG_METHOD_DICTIONARY.put("PB", new MethodTypeMapping("setPublisher", String.class));
		TAG_METHOD_DICTIONARY.put("PP", new MethodTypeMapping("setPublishingPlace", String.class));
		TAG_METHOD_DICTIONARY.put("PY", new MethodTypeMapping("setPublicationYear", String.class));
		TAG_METHOD_DICTIONARY.put("R1", new MethodTypeMapping("setReviewedItem", String.class));
		TAG_METHOD_DICTIONARY.put("RN", new MethodTypeMapping("setResearchNotes", String.class));
		TAG_METHOD_DICTIONARY.put("RP", new MethodTypeMapping("setReprintEdition", String.class));
		TAG_METHOD_DICTIONARY.put("SE", new MethodTypeMapping("setSection", String.class));
		TAG_METHOD_DICTIONARY.put("SN", new MethodTypeMapping("setIsbnIssn", String.class));
		TAG_METHOD_DICTIONARY.put("SP", new MethodTypeMapping("setStartPage", Integer.class));
		TAG_METHOD_DICTIONARY.put("ST", new MethodTypeMapping("setShortTitle", String.class));
		TAG_METHOD_DICTIONARY.put("T1", new MethodTypeMapping("setPrimaryTitle", String.class));
		TAG_METHOD_DICTIONARY.put("T2", new MethodTypeMapping("setSecondaryTitle", String.class));
		TAG_METHOD_DICTIONARY.put("T3", new MethodTypeMapping("setTertiaryTitle", String.class));
		TAG_METHOD_DICTIONARY.put("TA", new MethodTypeMapping("setTranslatedAuthor", String.class));
		TAG_METHOD_DICTIONARY.put("TI", new MethodTypeMapping("setTitle", String.class));
		TAG_METHOD_DICTIONARY.put("U1", new MethodTypeMapping("setUserDefinable1", String.class));
		TAG_METHOD_DICTIONARY.put("U2", new MethodTypeMapping("setUserDefinable2", String.class));
		TAG_METHOD_DICTIONARY.put("U3", new MethodTypeMapping("setUserDefinable3", String.class));
		TAG_METHOD_DICTIONARY.put("U4", new MethodTypeMapping("setUserDefinable4", String.class));
		TAG_METHOD_DICTIONARY.put("U5", new MethodTypeMapping("setUserDefinable5", String.class));
		TAG_METHOD_DICTIONARY.put("UR", new MethodTypeMapping("setUrl", String.class));
		TAG_METHOD_DICTIONARY.put("VL", new MethodTypeMapping("setVolumeNumber", String.class));
		TAG_METHOD_DICTIONARY.put("VO", new MethodTypeMapping("setPublisherStandardNumber", String.class));
		TAG_METHOD_DICTIONARY.put("Y1", new MethodTypeMapping("setPrimaryDate", String.class));
		TAG_METHOD_DICTIONARY.put("Y2", new MethodTypeMapping("setAccessDate", String.class));
	}

	/*
	 * 
	 */
	private final static LinkedHashMap<MethodTypeMapping, String> METHOD_TAG_DICTIONARY = new LinkedHashMap<MethodTypeMapping, String>();

	static {
		for (Map.Entry<String, MethodTypeMapping> entry : TAG_METHOD_DICTIONARY.entrySet()) {
			String s = entry.getValue().getMethod();
			s = "get".concat(s.substring(3));
			if (entry.getValue().getClazz().equals(List.class)) {
				s = s.concat("s");
			}
			METHOD_TAG_DICTIONARY.put(new MethodTypeMapping(s, entry.getValue().getClazz()), entry.getKey());
		}
	}

	private final static HashMap<String, Method> RECORD_AVAILABLE_METHODS = new HashMap<String, Method>();

	static {
		Method[] methods = Record.class.getMethods();
		if (methods != null) {
			for (Method method : methods) {
				RECORD_AVAILABLE_METHODS.put(method.getName(), method);
			}
		}
	}

	/**
	 * 
	 * @param obj
	 * @param line
	 * @param code
	 * @param methodName
	 * @param _clazz
	 * @throws JRisException
	 */
	private static void executeSetReflection(Record obj, String line, String code, String methodName, Class<?> _clazz) throws JRisException {
		Method method;
		Class<?> clazz = _clazz;
		try {
			Object arg = line.substring(5).trim();

			if (clazz.equals(List.class)) {
				clazz = String.class;
			} else if (clazz.equals(Type.class)) {
				try {
					arg = Type.valueOf((String) arg);
				} catch (Exception e) {
					throw new JRisException(e);
				}
			} else if (clazz.equals(Integer.class)) {
				Matcher matchcer = NUMBER_PATTERN.matcher((String) arg);
				if (matchcer.matches()) {
					arg = Integer.valueOf((String) arg);
				} else {
					return;
				}
			}

			method = obj.getClass().getMethod(methodName, clazz);

			if (code.equals(L1)) {
				String[] splitted = ((String) arg).split(";");
				for (String s : splitted) {
					method.invoke(obj, s);
				}
			} else {
				method.invoke(obj, arg);
			}

		} catch (SecurityException e) {
			throw new JRisException(e);
		} catch (NoSuchMethodException e) {
			throw new JRisException(e);
		} catch (IllegalAccessException e) {
			throw new JRisException(e);
		} catch (IllegalArgumentException e) {
			throw new JRisException(e);
		} catch (InvocationTargetException e) {
			throw new JRisException(e);
		}
	}

	/**
	 * 
	 * @param record
	 * @param methodName
	 * @param clazz
	 * @return
	 * @throws JRisException
	 */
	private static Object executeGetReflection(Record record, String methodName, Class<?> clazz) throws JRisException {
		Object result = null;

		Method method = RECORD_AVAILABLE_METHODS.get(methodName);
		if (method != null) {
			try {
				result = method.invoke(record);
			} catch (SecurityException e) {
				throw new JRisException(e);
			} catch (IllegalAccessException e) {
				throw new JRisException(e);
			} catch (IllegalArgumentException e) {
				throw new JRisException(e);
			} catch (InvocationTargetException e) {
				throw new JRisException(e);
			}
		}

		return result;
	}

	/**
	 * 
	 * @param line
	 * @param lastParsed
	 * @param record
	 * @return
	 * @throws JRisException
	 */
	private static String lineParser(String line, String lastParsed, Record record) throws JRisException {
		String code = lastParsed;
		MethodTypeMapping mappedField = null;
		if (!line.isEmpty() && line.length() > 6) {
			code = line.substring(0, 2);
			mappedField = TAG_METHOD_DICTIONARY.get(code);
			if (mappedField != null) {
				executeSetReflection(record, line, code, mappedField.getMethod(), mappedField.getClazz());
			} else if (lastParsed.equals(AB)) {
				code = AB;
				mappedField = TAG_METHOD_DICTIONARY.get(code);
				executeSetReflection(record, line, code, mappedField.getMethod(), mappedField.getClazz());
			} else {
				throw new JRisException("Field with code " + code + " not allowed");
			}
		}
		return code;
	}

	/**
	 * Parse the content of the {@link Reader} as RIS format and builds the associated {@code List<Record> list}.
	 * @param reader the reader to parse.
	 * @return the related {@link List} of {@link Record} parsed from the Reader.
	 * @throws IOException if an IO error occurred.
	 * @throws JRisException if an applicative error occurred.
	 */
	public static List<Record> parse(Reader reader) throws IOException, JRisException {
		List<Record> result = null;
		BufferedReader br = null;
		if (reader != null) {
			try {
				br = new BufferedReader(reader);

				result = new LinkedList<Record>();
				Record temp = new Record();
				String line = null;
				String lastCode = null;

				while ((line = br.readLine()) != null) {
					if (line.startsWith(ER)) {
						result.add(temp);
						temp = new Record();
					} else {
						lastCode = lineParser(line.trim(), lastCode, temp);
					}
				}

			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						// NOTHING TO DO
					}
				}
			}
		} else {
			throw new JRisException("Reader null not allowed.");
		}
		return result;
	}

	/**
	 * See {@link #parse(Reader)}.
	 * @see {@link #parse(Reader)}.
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws JRisException
	 */
	public static List<Record> parse(File file) throws IOException, JRisException {
		List<Record> result = null;
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			result = parse(fr);
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					// NOTHING TO DO
				}
			}
		}
		return result;
	}

	/**
	  * See {@link #parse(Reader)}.
     * @see {@link #parse(Reader)}.
	 * @param filePath the file path of the file to parse.
	 * @return
	 * @throws IOException
	 * @throws JRisException
	 */
	public static List<Record> parse(String filePath) throws IOException, JRisException {
	    List<Record> result = null;
	    FileInputStream in = null;
	    try {
	       in = new FileInputStream(filePath);
	       result = parse(in);
	    } finally {
	        if (in != null) {
	            try {
	                in.close();
	            } catch (IOException e) {
	                //NOTHING TO DO.
	            }
	        }
	    }
		return result;
	}

	/**
	 * See {@link #parse(Reader)}.
     * @see {@link #parse(Reader)}.
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws JRisException
	 */
	public static List<Record> parse(InputStream in) throws IOException, JRisException {
		List<Record> result = null;
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(in);
			result = parse(reader);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// NOTHING TO DO
				}
			}
		}
		return result;
	}

	/**
	 * Write to into the {@link Writer} the text format of the {@code records} provided;
	 * 
	 * @param records
	 * @param writer
	 * @return true only if the action is successfully executed, false otherwise.
	 * @throws JRisException
	 *             if an applicative exception occurs.
	 * @throws IOException
	 *             if an IO exception occurs.
	 */
	public static boolean build(List<Record> records, Writer writer) throws JRisException, IOException {
		boolean success = false;
		if (records != null && !records.isEmpty()) {
			if (writer != null) {
				StringBuilder builder = new StringBuilder();
				Object currentVal = null;
				for (Record record : records) {
					for (Map.Entry<MethodTypeMapping, String> entry : METHOD_TAG_DICTIONARY.entrySet()) {
						currentVal = executeGetReflection(record, entry.getKey().getMethod(), entry.getKey().getClazz());
						if (currentVal != null) {
							if (currentVal instanceof List) {
								for (Object obj : (List<?>) currentVal) {
									builder.append(entry.getValue());
									builder.append(TAG_SEPARATOR);
									builder.append(obj.toString());
									builder.append(LINE_SEPARATOR);
								}
							} else {
								builder.append(entry.getValue());
								builder.append(TAG_SEPARATOR);
								builder.append(currentVal.toString());
								builder.append(LINE_SEPARATOR);
							}
						}
					}
					builder.append(ER);
					builder.append(TAG_SEPARATOR);
					builder.append(LINE_SEPARATOR);
					builder.append(LINE_SEPARATOR);
				}

				writer.write(builder.toString());
				success = true;
			} else {
				throw new JRisException("Writer not allowed.");
			}
		} else {
			throw new JRisException("Record list null or empty not allowed.");
		}
		return success;
	}

	/**
	 * See {@link #build(List, Writer)}.
	 * 
	 * @see {@link #build(List, Writer)}.
	 * @param records
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws JRisException
	 */
	public static boolean build(List<Record> records, File file) throws IOException, JRisException {
		boolean success = false;
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			success = build(records, writer);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// NOTHING TO DO.
				}
			}
		}
		return success;
	}

	/**
	 * 
	 * See {@link #build(List, Writer)}.
	 * 
	 * @see {@link #build(List, Writer)}.
	 * @param records
	 * @param out
	 * @return
	 * @throws IOException
	 * @throws JRisException
	 */
	public static boolean build(List<Record> records, OutputStream out) throws IOException, JRisException {
		boolean success = false;
		OutputStreamWriter writer = null;

		try {
			writer = new OutputStreamWriter(out);
			success = build(records, writer);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// NOTHING TO DO.
				}
			}
		}
		return success;
	}

	/**
	 * See {@link #build(List, Writer)}.
	 * 
	 * @see {@link #build(List, Writer)}.
	 * @param records
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws JRisException
	 */
	public static boolean build(List<Record> records, String filePath) throws IOException, JRisException {
		boolean success = false;
		FileOutputStream writer = null;

		try {
			writer = new FileOutputStream(filePath);
			success = build(records, writer);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// NOTHING TO DO.
				}
			}
		}
		return success;
	}

}