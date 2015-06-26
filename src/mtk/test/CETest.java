package mtk.test;

import mtk.app.*;
import java.io.IOException;


/**
 * @author shinda
 * 
 */
public class CETest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		StoryBall sb = new StoryBall();
		
		sb.start();


		  int c = 1;
		  do{
		   
			   sb.playbackStory();
			   Thread.sleep(8000);
			   
		  }while(c++ < 5);

		sb.stopPlayback();
		System.out.print("All stop");
	}

}
