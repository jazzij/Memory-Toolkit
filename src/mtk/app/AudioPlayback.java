/**
* AudioPlayback.java
*Eventually add this to presentation engine.
* Given a directory/url, this class will download all the clips located
* in the directory, and handle playback. Simply select which clip to play (or allow random selection)
**/
package mtk.app;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.lang.Thread;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.InterruptedException;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;
import javazoom.jl.player.advanced.PlaybackEvent;

public class AudioPlayback  extends PlaybackListener {	

	public String sourceDirName;	
	private ArrayList<File>stories;
	private AdvancedPlayer audioPlayer;
	private Random randnum;
	public boolean IS_PLAYING = false;
	
	
	/**
	* Constructor
	*/
	public AudioPlayback(String sourceFile) throws FileNotFoundException{
		super();
		this.sourceDirName = sourceFile;
		this.stories = new ArrayList<File>();
		int numFiles = loadAudioFiles();
		System.out.println("Ready to play "+numFiles);
		
		randnum = new Random();
	}


	/**
	* Get audio files from given directory. Prepare files for playback
	* Returns number of files loaded from the directory
	* returns int 
	*/	
	public int loadAudioFiles() throws FileNotFoundException{
		//copy files from source
		File sourceDir = new File(sourceDirName);
		
		if(!sourceDir.isDirectory()){
			throw new FileNotFoundException("source dir:"+sourceDir+"not found.");
		}

		//get refs to all files in source directory and add to stories list
		String[] types = {"mp3", "wav"};
		Collection<File> allFiles = FileUtils.listFiles( sourceDir, types, false);
		stories.addAll(allFiles);
		
		return stories.size();
	}
	
	public void playMP3(){	
		
		int next = randnum.nextInt( stories.size());
		playMP3(stories.get(next));
	}
	/**
	 * playMP3
	 * Given a File, the program will attempt to play it.
	 * A listener will report when the file starts playing (will set IS_PLAYING flag) and
	 * when it completes (will reset IS_PLAYING flag)
	 * @param file
	 */
	private void playMP3(File file){
		
		try{
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);

		audioPlayer = new AdvancedPlayer(bis);
		audioPlayer.setPlayBackListener(this);
//		temp.setPlaybackListener(
//			new PlaybackListener(){
//				@Override
//				public void playbackFinished(PlaybackEvent event){	
//					System.out.println("woah! Done");
//				}
//			});
		
		}catch(Exception e){
		System.err.println("Problem playing file: "+file.getAbsolutePath()+"/n"+ e.getMessage());
		}
		
		//new thread allow program to continue while file plays
		//try{
		Thread playThread = new Thread(){
			public void run(){
				try{
					audioPlayer.play();}
				catch(Exception e){ 
					System.err.println(e.getMessage());}
			}
		};
		playThread.start();
		//playThread.join(); // make the program wait until recording is done;
		/*}catch(InterruptedException ie){
			//do something
			System.err.println(ie.getMessage());
		}*/
		System.out.println("Complete.");
	}
	
	public void stopMP3(){
		System.out.println("Closing...");
		IS_PLAYING = false;
		audioPlayer.close();
	}
	
 	
 	@Override
 	public void playbackFinished(PlaybackEvent event){
 		System.out.println("it's done");
 		audioPlayer.close();
 		IS_PLAYING = false;
 	}
 	@Override
 	public void playbackStarted(PlaybackEvent event){	
 		System.out.println("it's playing");
 		IS_PLAYING = true;
 	}

 	
 	
 	
	public static void main (String[] args) throws IOException, InterruptedException {
	  AudioPlayback player = new AudioPlayback("/home/pi");
	 player.playMP3();
	 // player.playMP3(new File("/home/pi/workspace/Memory Toolkit/stories/example.mp3"));
	  
//	  Thread.sleep(8000);
//	  System.out.println("Waking up");
//	  player.stopMP3();
	  

}
}

