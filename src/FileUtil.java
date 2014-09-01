import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class FileUtil {
	public static String read(File file) {
		BufferedReader reader;
		String textFile = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				textFile += line + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return textFile;
	}
	
	public static boolean write(File file, byte data[]) {
	  
		FileOutputStream out;
		try {
		  if(file.exists()) {
		    file.createNewFile();
		  }
			out = new FileOutputStream(file);
			out.write(data);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
