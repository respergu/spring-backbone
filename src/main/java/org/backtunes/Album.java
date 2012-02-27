package org.backtunes;

import java.util.ArrayList;
import java.util.List;

public class Album {
	
	private String title;
	private String artist;
	private List<Track> tracks = new ArrayList<Track>();
	
	public Album() {};
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public List<Track> getTracks() {
		return tracks;
	}
	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}

}
