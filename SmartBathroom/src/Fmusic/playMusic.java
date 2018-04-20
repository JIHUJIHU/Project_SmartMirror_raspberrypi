package Fmusic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class playMusic { 
	public playMusic(){
				try {
							FileInputStream fileInputStream=new FileInputStream("resources/music/2.mp3"); //YOUR MP3
							
							Player Player=new Player(fileInputStream);
							Player.play(); //재생
							System.out.println("Song is Playing");
				}catch(FileNotFoundException fe){
							fe.printStackTrace();
				}catch(JavaLayerException je) {
							je.printStackTrace();
				}
	}
}
