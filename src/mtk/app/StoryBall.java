/**
 * A simple app that takes sensor input from the Context Toolkit (IMU, face)
 * and plays an audio clip
 * 
 * 
 * @author Shinda Zheng, Jasmine Jones
 */
package mtk.app;

import java.util.Random;
//import java.util.Collection;
//import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
//
//import javazoom.jl.player.advanced.PlaybackListener;
//import javazoom.jl.player.advanced.PlaybackEvent;

import org.apache.commons.io.FileUtils;



public class StoryBall extends MemoryApp {

	private final String HOST_NAME = "localhost";
	private final String METHOD_FILE = "methods.json";
	
	private Random randnum;
	
	private final String DEST_DIR = "./stories/";
	private String SRC_DIR = "/media/D453-A719/griotproject/"; 	
	
	private AudioPlayback playback;

	
	public StoryBall() {
		super();
		randnum = new Random();
		
		try{
		playback = new AudioPlayback(DEST_DIR);
		}catch(FileNotFoundException fnf){
			System.err.println(fnf.getMessage());
		}
	}

	@Override
	public void start() {
		// Very important, must start with super.start()!
		super.start();
		//ctxEngine.startIMU();
		//ctxEngine.startCamera();
		
		//enter playback mode
		//thread sleeps and waits shake or camera notice

	}

	/**
	 * Delete files downloaded when application stops to prevent w
	 * Pi from wasting space
	 * @return -1 if stop is unsuccessful, 0 on success
	 */
	public int stop(){
		//delete downloaded files
		try{
		FileUtils.deleteDirectory(new File(DEST_DIR));
		}catch(Exception e){
			return -1;
		}
		
		return 0;
	}
	
	@Override
	protected String getHostName() {
		return HOST_NAME;
	}

	@Override
	protected String getMethodFile() {
		return METHOD_FILE;
	}

	@Override
	public void OnBoardIMUShakeDetected() {
		System.out.println("Event: Shaked");
		
		/* Play some audio clips */
		playbackStory();
	}
	


	/**
	 * Copies contents of source directory into destination directory. Creates destination dir
	 * if none exists.
	 * @return number of _stories downloaded
	 * @throws FileNotFoundException
	 */
	private long downloadStories() throws FileNotFoundException{
		
		//copy files from source to local
		try{
		File oldDir = new File(SRC_DIR);
		File newDir = new File(DEST_DIR);
		
		if(!oldDir.isDirectory()){
			throw new FileNotFoundException("source dir:"+SRC_DIR+"not found.");
		}
		
		FileUtils.copyDirectory(oldDir, newDir);
		return FileUtils.sizeOfDirectory(newDir);
		
		}catch (Exception e)
		{
			System.err.println("Error in copying dorectory. "+ e.getMessage() );
			return -1;
		}
		
		
	}
	
	/**
	 * Will download and playback an audio file from
	 * the designated directory. ID expected should be generated 
	 * based on whatever rules are there
	 * @param id
	 */
	public void playbackStory()
	{
		//A. if ALSO face detected, get stories for that face

		//B. if shaken && no face, just do random
		
		//C. if shaken and story already playing, stop it and start another with current rules
		if(playback.IS_PLAYING){
			playback.stopMP3();
		}
				
		//play another
		playback.playMP3();
	}
	public void stopPlayback(){
		playback.stopMP3();
	}
	
	/*
	class AudioListener extends PlaybackListener{
		@Override
		public void playbackFinished(PlaybackEvent evt){

		}
		@Override
		public void playbackStarted (PlaybackEvent evt){

		}
		}
		*/

}
