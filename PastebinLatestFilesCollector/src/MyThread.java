import java.io.IOException;

public class MyThread extends Thread{
	int counter;
	String name;
	String text;
	
	public MyThread(int counter, String name) {
		this.counter = counter;
		this.name = name;
	}
	
	public void run() {
		try {
			text = MainParallel.getUrlSource("https://www.pastebin.com/raw/"+name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
