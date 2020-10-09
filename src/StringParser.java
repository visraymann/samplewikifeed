import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

	public static void main(String args[]) throws IOException {
		
		Scanner scanner = null;
		ArrayList<String> lines = new ArrayList<String>();
		File file = new File("C:\\codebase\\samplewikifeed\\samp2.csv");
		HashMap<String, HashMap<VerseKey, String>> chapters = new HashMap<String, HashMap<VerseKey, String>>();

		try (FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(isr)) {
			String str;
			while ((str = reader.readLine()) != null) {
				lines.add(str.replace(",$$$", ""));
			}

		} catch (IOException e) {
			System.out.println("Exception" + e);
		}

		Iterator<String> it = lines.iterator();
		
		while (it.hasNext()) {
			scanner = new Scanner(it.next());
			scanner.useDelimiter(",##,");
			extractVersesByChapter(scanner, chapters);
		}

		Set<String> keys = chapters.keySet();
		Iterator<String> itKeys = keys.iterator();
		while (itKeys.hasNext()) {
			String previousVerseNo = "";
			String itKeysStr = (String) itKeys.next();
			
			printVerses(previousVerseNo, itKeysStr, 
					parseVersesAndSort(chapters, itKeysStr));
		}
	}

	/**
	 * @param scanner
	 * @param chapters
	 */
	private static void extractVersesByChapter(Scanner scanner, HashMap<String, HashMap<VerseKey, String>> chapters) {
		String chapterName;
		String verseLine;
		chapterName = scanner.next();
		if (!chapters.containsKey(chapterName)) {
			chapters.put(chapterName, new HashMap<VerseKey, String>());
		}
		verseLine = scanner.next();
		VerseKey key = new VerseKey(scanner.next(), scanner.next());
		chapters.get(chapterName).put(key, verseLine);
	}

	private static Set<Map.Entry<VerseKey, String>> parseVersesAndSort(
			HashMap<String, HashMap<VerseKey, String>> chapters, String itKeysStr) {
		HashMap<VerseKey, String> map = chapters.get(itKeysStr);
		ValueComparator bvc = new ValueComparator(map);
		TreeMap<VerseKey, String> sorted = new TreeMap<>(bvc);
		sorted.putAll(map);
		Set<Map.Entry<VerseKey, String>> entries = sorted.entrySet();
		return entries;
	}

	private static void printVerses(String previousVerseNo, String itKeysStr, Set<Map.Entry<VerseKey, String>> entries)
			throws IOException {
		PrintWriter pw = new PrintWriter(System.out);
		BufferedWriter bw = new BufferedWriter(pw);
		System.out.println("chapter -----> " + itKeysStr);
		for (Map.Entry<VerseKey, String> entry : entries) {
			
			if (entry.getValue().contains("||")) {
				bw.write(entry.getValue() + entry.getKey().getVerseNo()+"."+ entry.getKey() + "||");
				bw.newLine();bw.newLine(); //write("\n\n");
			} else if (!previousVerseNo.equals(entry.getKey().getVerseNo())) {
				bw.newLine();bw.newLine();
				bw.write(entry.getValue() + entry.getKey().getVerseNo()+"."+entry.getKey() + "|");
				bw.newLine();
			} else {
				bw.write(entry.getValue() + entry.getKey().getVerseNo()+"."+ entry.getKey() + "|");
				bw.newLine();
			}
			previousVerseNo = entry.getKey().getVerseNo();
		}
	}

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

class VerseKey { 

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
