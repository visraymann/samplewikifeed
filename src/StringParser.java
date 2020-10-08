import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class StringParser {

	public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {

		// readFromExcel();

		File file = new File("C:\\Users\\visra\\Desktop\\samp2.csv");
		HashMap<String, HashMap<VerseKey, String>> chapters = new HashMap<String, HashMap<VerseKey, String>>();
		String chapterName = "";
		String verseLine = "";
		ArrayList<String> lines = new ArrayList<String>();

		try (FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(isr)) {
			String str;
			while ((str = reader.readLine()) != null) {
				//System.out.println(str);
				lines.add(str.replace(",$$$", ""));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Lines: " + lines);
		/*Scanner scanner = new Scanner(file);
		scanner.useDelimiter("$$$");
		while (scanner.hasNext()) {
			lines.add(scanner.nextLine().replace("$$$", ""));
		}
		scanner.close();*/
		Iterator<String> it = lines.iterator();
		Scanner scanner2 = null;
		
		while (it.hasNext()) {
			scanner2 = new Scanner(it.next());
			scanner2.useDelimiter(",##,");
			chapterName = scanner2.next();
			if (!chapters.containsKey(chapterName)) {
				chapters.put(chapterName, new HashMap<VerseKey, String>());
			}
			verseLine = scanner2.next();
			VerseKey key = new VerseKey(scanner2.next(), scanner2.next());
			chapters.get(chapterName).put(key, verseLine);
		}
		Set<String> keys = chapters.keySet();
		Iterator<String> itKeys = keys.iterator();
		while (itKeys.hasNext()) {
			String itKeysStr = (String) itKeys.next();
			HashMap<VerseKey, String> map = chapters.get(itKeysStr);
			ValueComparator bvc = new ValueComparator(map);
			TreeMap<VerseKey, String> sorted = new TreeMap<>(bvc);
			sorted.putAll(map);
			Set<Map.Entry<VerseKey, String>> entries = sorted.entrySet();
			String previousVerseNo = "";
			System.out.println("chapter -----> " + itKeysStr);
			
			PrintStream printStream = new PrintStream(System.out, true, "UTF-8");
	        
	        
			for (Map.Entry<VerseKey, String> entry : entries) {
				if (entry.getValue().contains("||")) {
					printStream.println(entry.getValue() + entry.getKey() + "||");
					printStream.println("\n\n");
				} else if (!previousVerseNo.equals(entry.getKey().getVerseNo())) {
					printStream.println("'''Verse : " + entry.getKey().getVerseNo() + "'''\n\n");
					printStream.println(entry.getValue() + entry.getKey() + "|");
					printStream.println("\n");
				} else {
					printStream.println(entry.getValue() + entry.getKey() + "|");
					printStream.println("\n");
				}
				previousVerseNo = entry.getKey().getVerseNo();
			}
		}

		// System.out.println(chapters.keySet());
	}

	/*
	 * public static void readFromExcel() { try { File file = new
	 * File("C:\\\\Users\\\\visra\\\\Desktop\\\\samp2.xlsx"); // creating a new file
	 * instance FileInputStream fis = new FileInputStream(file); // obtaining bytes
	 * from the file // creating Workbook instance that refers to .xlsx file
	 * XSSFWorkbook wb = new XSSFWorkbook(fis); XSSFSheet sheet = wb.getSheetAt(0);
	 * // creating a Sheet object to retrieve object
	 * Iterator<org.apache.poi.ss.usermodel.Row> itr = sheet.iterator(); //
	 * iterating over excel file
	 * 
	 * while (itr.hasNext()) { org.apache.poi.ss.usermodel.Row row = itr.next();
	 * Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each
	 * column String rowinString = ""; while (cellIterator.hasNext()) { Cell cell =
	 * cellIterator.next(); System.out.println(cell.getCellTypeEnum().toString());
	 * switch (cell.getCellType()) { case Cell.CELL_TYPE_STRING: // field that
	 * represents string cell type //System.out.print(cell.getStringCellValue() +
	 * "\t\t\t"); rowinString = rowinString + cell.getStringCellValue(); break; case
	 * Cell.CELL_TYPE_NUMERIC: // field that represents number cell type
	 * //System.out.print(cell.getNumericCellValue() + "\t\t\t"); rowinString =
	 * rowinString + cell.getNumericCellValue(); break; default: } }
	 * System.out.println("Row Value: " + rowinString); } } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */
}

class ValueComparator implements Comparator<VerseKey> {

	Map<VerseKey, String> base;

	public ValueComparator(Map<VerseKey, String> base) {
		this.base = base;
	}

	@Override
	public int compare(VerseKey o1, VerseKey o2) {
		if (o1.verseNo == o2.verseNo && o1.verseLineNo > o2.verseLineNo) {
			return 1;
		} else if (o1.verseNo == o2.verseNo && o1.verseLineNo < o2.verseLineNo) {
			return -1;
		} else if (o1.verseNo > o2.verseNo) {
			return 1;
		} else if (o1.verseNo < o2.verseNo) {
			return -1;
		} else
			return 0;
	}

}

class VerseKey { // implements Comparable<VerseKey>{ //implements Comparator<VerseKey> {

	int verseNo;
	int verseLineNo;

	public VerseKey(String verseNo, String verseLineNo) {
		this.verseNo = Integer.parseInt(verseNo);
		this.verseLineNo = Integer.parseInt(verseLineNo);
	}

	@Override
	public String toString() {
		// return "Key: "+ this.verseNo + " : " + this.verseLineNo;
		return "" + this.verseLineNo;
	}

	public String getVerseNo() {
		// return "Key: "+ this.verseNo + " : " + this.verseLineNo;
		return "" + this.verseNo;
	}
}
