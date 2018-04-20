package speechService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import commData.CommData;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import livingInformation.WeatherSB;
import livingInformation.WeatherData;
import speech.tts.TextToSpeech;

public class simpleSpeech {
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Member Variable
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private final static String TAG 			= "simpleSpeech";


	private TextToSpeech textToSpeech 			= new TextToSpeech();															// Necessary
	private Logger 				logger 						= Logger.getLogger(getClass().getName());					// Logger
	
	// Variables
	private String 				result;

	// Threads
	public Thread					speechThread			= null;
	public Thread					resourcesThread		= null;

	// LiveRecognizer
	private LiveSpeechRecognizer recognizer;
	private FileInputStream fileInputStream; //=new FileInputStream("resources/music/2.mp3"); //YOUR MP3
	
	public Player 			 player = null; //=new Player(fileInputStream);

	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- Constructor Method
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public simpleSpeech() {

		CommData.log(TAG, "Loading..\n");
		
		try {
						this.fileInputStream=new FileInputStream("resources/music/2.mp3");
						this.player = new Player(fileInputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Configuration
		Configuration configuration = new Configuration();

		// Load model from the jar
		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");

		// if you want to use LanguageModelPath disable the 3 lines after which
		// are setting a custom grammar->

		// configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin")

		// Grammar
		configuration.setGrammarPath("resources//grammars");
		configuration.setGrammarName("grammar");
		configuration.setUseGrammar(true);

		try {
					recognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException ex) {
					CommData.log(TAG, "IOException : "+ex.getMessage());
		}

		// Start recognition process pruning previously cached data.
		recognizer.startRecognition(true);
		
		CommData.log(TAG, "init OK! Loading OK !!!");
		CommData.gSTATE = CommData.STATE_LOAD_OK;
		
		// Start the Thread
		//startSpeechThread();
		//startResourcesThread();
	}

	public void startThread(){
			startSpeechThread();
			startResourcesThread();
	}
	
	public void stopThread(){
		if (speechThread != null && speechThread.isAlive())
		{ 
			this.speechThread.interrupt(); 
			this.speechThread = null; 
		}
		if (resourcesThread != null && resourcesThread.isAlive()) 
		{
			this.resourcesThread.interrupt(); 
			this.resourcesThread = null; 
		}
    }
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- 음성 분석  Thread 시작
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void startSpeechThread() {

		// alive?
		if (speechThread != null && speechThread.isAlive())
		{
			System.out.println("already exists");
			return;
		}

		// initialise
		speechThread = new Thread(new Runnable() {
			
			public void run() {
				logger.log(Level.INFO, "You can start to speak...\n");
				textToSpeech.speak("You can start to speak", 1.5f, false, true);
				try {
					while (true) {
						/*
						 * This method will return when the end of speech is
						 * reached. Note that the end pointer will determine the end
						 * of speech.
						 */
						SpeechResult speechResult = recognizer.getResult();
						if (speechResult != null) {

							result = speechResult.getHypothesis();
							CommData.gVOICE_RESULT = result;
							
							if(result.equals("<unk>")) {
								result = "Please try again";
							}
			
//							CommData.log(TAG, "You said: [" + result + "]\n");
//							CommData.log(TAG, "You said: " + result + "\n");
							System.out.println("YOu said: [" + result + "]\n");

							makeDecision(result);

						} else
							logger.log(Level.INFO, "I can't understand what you said.\n");

					}
				} catch (Exception ex) {
					logger.log(Level.WARNING, null, ex);
				}

				logger.log(Level.INFO, "SpeechThread has exited...");
			}
		});
			

		// Start
		speechThread.start();

	}

	/**
	 * Starting a Thread that checks if the resources needed to the
	 * SpeechRecognition library are available
	 */
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- 마이크로부터 음성 인식  Thread 시작  
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	public void startResourcesThread() {

		if (resourcesThread != null && resourcesThread.isAlive())
			  return;

		resourcesThread = new Thread(new Runnable() {
			public void run() {
				try {

					// Detect if the microphone is available
					while (true) {
						if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
							  CommData.log(TAG, "Microphone is available.\n");
						} else {
							//CommData.log(TAG, "Microphone is not available.\n");
						}

						// Sleep some period
						Thread.sleep(350);
					}

				} catch (InterruptedException ex) {
					CommData.log(TAG, "InterruptException : "+ ex.getMessage());
					resourcesThread.interrupt();
				}
			}
		});
			
		// Start
		resourcesThread.start();
	}

	/**
	 * Takes a decision based on the given result
	 */
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//- 음성 분석  결과에 따른 동작 제어 
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	public void makeDecision(String speech) {

		if (speech.contains("current weather")){
			WeatherData wd = new WeatherData();
			WeatherSB current = wd.getCurrentWeather();
			textToSpeech.speak("current temperature is "+current.temp_C+" degrees and status is " + current.status + ". Have a good day." , 1.5f, false, true);
			return;
		}
		else if (speech.contains("play music")) {
			textToSpeech.speak("play the music", 1.5f, false, true);
			/*
			try {
				//this.player.play();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}*/
			return;
		}
		else if (speech.contains("stop music")) {
			textToSpeech.speak("stop the music", 1.5f, false, true);
			
			//if(this.player == null) 
				//this.player.close();
			/*
			try {
				this.player.
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}*/
			return;
		}
	}
	

	/**
	 * Java Main Application Method
	 * 
	 * @param args
//	 */
}