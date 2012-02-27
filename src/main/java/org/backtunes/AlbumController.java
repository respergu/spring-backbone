package org.backtunes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/albums")
public class AlbumController {
	
	   @RequestMapping(method = RequestMethod.GET, produces = "application/json") @ResponseBody
	    public List<Album> findAll() {
	    	List<Album> ret = new ArrayList<Album>();
	    	ret.add(getGhostAlbum());
	    	ret.add(getTomAlbum());
	        return ret;
	    }

	private Album getTomAlbum() {
		Album tomAlbum = getAlbum("Where the Earth Meets the Sky", "Tom Heasley");
		tomAlbum.getTracks().add(getTrack("Ground Zero", "/music/blue.mp3"));
		tomAlbum.getTracks().add(getTrack("Western Sky", "/music/jazz.mp3"));
		tomAlbum.getTracks().add(getTrack("Monterey Bay", "/music/minimalish.mp3"));
		tomAlbum.getTracks().add(getTrack("Where the Earth Meets the Sky", "/music/slower.mp3"));
		return tomAlbum;
	}

	private Album getGhostAlbum() {
		Album ghostAlbum = getAlbum("Bound - Zen Bound Ingame Music", "Ghost Monkey");
		ghostAlbum.getTracks().add(getTrack("Care", "/music/blue.mp3"));
		ghostAlbum.getTracks().add(getTrack("Rope and Wood", "/music/jazz.mp3"));
		ghostAlbum.getTracks().add(getTrack("Problem Solvent","/music/minimalish.mp3"));
		ghostAlbum.getTracks().add(getTrack("Unpaint My Skin", "/music/slower.mp3"));
		ghostAlbum.getTracks().add(getTrack("Nostalgia", "/music/blue.mp3"));
		ghostAlbum.getTracks().add(getTrack("Interludum", "/music/jazz.mp3"));
		ghostAlbum.getTracks().add(getTrack("Grind", "/music/minimalish.mp3"));
		ghostAlbum.getTracks().add(getTrack("Diagrams", "/music/slower.mp3"));
		ghostAlbum.getTracks().add(getTrack("Hare", "/music/blue.mp3"));
		ghostAlbum.getTracks().add(getTrack("Carefree", "/music/jazz.mp3"));
		ghostAlbum.getTracks().add(getTrack("Tunnel At The End Of Light", "/music/minimalish.mp3"));
		return ghostAlbum;
	}

	private Track getTrack(String title, String url) {
		Track ret = new Track();
		ret.setTitle(title);
		ret.setUrl(url);
		return ret;
	}

	private Album getAlbum(String title, String artist) {
		Album ret = new Album();
		ret.setTitle(title);
		ret.setArtist(artist);
		return ret;
	}

}
