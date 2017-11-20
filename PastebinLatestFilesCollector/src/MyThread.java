import java.io.IOException;

/**
 * Thread used in the multi-threaded version of the program. Used to extract the text from the files.
 * @author A. Martínez
 * @version 1.0 21/11/2017
 */
public class MyThread extends Thread{
	private int counter;
	private String name;
	private String text;

	/**
	 * Sets the variables used to store
	 * @param counter
	 * @param name
	 */
	public MyThread(int counter, String name) {
		this.counter = counter;
		this.name = name;
	}

	/**
	 * Executes the thread. It extracts the text from the files.
	 */
	public void run() {
		try {
			text = MainParallel.getUrlSource("https://www.pastebin.com/raw/"+name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Returns the property 'counter' used to store the file's assigned position in the array.
	 * @return 'Counter' property
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Returns the property 'text' used to store the file's text.
	 * @return 'Text' property.
	 */
	public String getText() {
		return text;
	}
}
