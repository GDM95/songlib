/*
 *	[CS213] Assignment 1
 *	by Greg Melillo and Eric Kim
*/

package songlib.app;

public class Song {
	
	String title;
	String artist;
	String album;
	String year;
	
	public Song(String title, String artist, String album, String year){
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public boolean isDuplicate(Song entrySong) {
		if(entrySong != null && this.title.equalsIgnoreCase(entrySong.title) && this.artist.equalsIgnoreCase(entrySong.artist)) {
			return true;
		}
		return false;
	}
	
	public String toStringFull() {
		return title + "," + artist + "," + album + "," + year + "\n";
	}
	
	// return the String that is displayed in the song list 
	public String toString() {
		return "Name: " + title + "\t\tArtist: " + artist;
	}
	
	public String getLowerCase() {
		return this.title.toLowerCase() + this.artist.toLowerCase();
	}

}