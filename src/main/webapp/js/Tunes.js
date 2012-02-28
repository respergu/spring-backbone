(function($) {

	window.Album = Backbone.Model.extend({
		isFirstTrack : function(index) {
			return index == 0;
		},
		isLastTrack : function(index) {
			return index == this.get('tracks').length - 1;
		},
		trackUrlAtIndex : function(index) {
			if (this.get('tracks').length >= index) {
				return this.get('tracks')[index].url;
			} else {
				return null;
			}
		}
	});
	
	window.Albums = Backbone.Collection.extend({
		model: Album,
		url: 'todo/albums'
	});
	
	window.Playlist = Albums.extend({
		isFirstAlbum : function(index) {
			return index == 0;
		},
		isLastAlbum : function(index) {
			return index >= this.models.length - 1;
		}
	});
	
	window.Player = Backbone.Model.extend({
		defaults :{
			'currentAlbumIndex' : 0,
			'currentTrackIndex' : 0,
			'state' : 'stop'
		},
		initialize : function() {
			this.playlist = new Playlist();
		},
		play : function() {
			this.set({state : 'play'});
		},
		pause : function() {
			this.set({state : 'pause'});
		},
		isStopped : function() {
			return !this.isPlaying();
		},
		isPlaying : function() {
			return this.get('state') == 'play';
		},
		currentAlbum : function() {
			return this.playlist.at(this.get('currentAlbumIndex'));
		},
		currentTrackUrl : function() {
			var currentAlbum = this.currentAlbum();
			if (currentAlbum) {
				var currentTrackIndex = this.get('currentTrackIndex');
				return currentAlbum.trackUrlAtIndex(currentTrackIndex);
			} else {
				return null;
			}
		},
		nextTrack : function() {
			var currentAlbum = this.currentAlbum();
			var currentAlbumIndex = this.get('currentAlbumIndex');
			var currentTrackIndex = this.get('currentTrackIndex');
			if (currentAlbum.isLastTrack(currentTrackIndex)) {
				this.set({'currentTrackIndex' : 0});
				if (this.playlist.isLastAlbum(currentAlbumIndex)) {
					this.set({'currentAlbumIndex' : 0});
				} else {
					this.set({'currentAlbumIndex' : currentAlbumIndex + 1});
				}
			} else {
				this.set({'currentTrackIndex' : currentTrackIndex + 1});
			}
			this.logCurrentAlbumAndTrack();
		},
		prevTrack : function() {
			var currentAlbum = this.currentAlbum();
			var currentAlbumIndex = this.get('currentAlbumIndex');
			var currentTrackIndex = this.get('currentTrackIndex');
			if (currentAlbum.isFirstTrack(currentTrackIndex)) {
				if (this.playlist.isFirstAlbum(currentAlbumIndex)) {
					var prevAlbumIndex = this.playlist.models.length -1;
					var prevAlbum = this.playlist.at(prevAlbumIndex);
					this.set({'currentTrackIndex' : prevAlbum.get('tracks').length - 1});
					this.set({'currentAlbumIndex' : prevAlbumIndex});
				} else {
					var prevAlbumIndex = this.get('currentAlbumIndex') - 1;
					var prevAlbum = this.playlist.at(prevAlbumIndex); 
					this.set({'currentTrackIndex' : prevAlbum.get('tracks').length -1});
					this.set({'currentAlbumIndex' : prevAlbumIndex});
				}
			} else {
				this.set({'currentTrackIndex' : currentTrackIndex - 1});
			}
			this.logCurrentAlbumAndTrack();
		},
		reset : function() {
			this.set({'currentTrackIndex' : 1});
			this.set({'currentAlbumIndex' : 0});
		},
		logCurrentAlbumAndTrack : function() {
			console.log("Player" +
					this.get('currentAlbumIndex') + ':' +
					this.get('currentTrackIndex'), this);
		}
	});
	
	window.library = new Albums();
	window.player = new Player();
	
	window.AlbumView = Backbone.View.extend({
		tagName : 'li',
		className : 'album',
		initialize : function() {
			_.bindAll(this, 'render');
			this.model.bind('change', this.render);
			this.template = _.template($('#album-template').html());
		},
		render : function() {
			var renderedContent = this.template(this.model.toJSON());
			$(this.el).html(renderedContent);
			return this;
		}
	});
	
	window.LibraryAlbumView = AlbumView.extend({
		events: {
			'click .queue.add' : 'select'
		},
		select : function() {
			this.collection.trigger('select', this.model);
		}
		
	});
	
	window.PlaylistAlbumView = LibraryAlbumView.extend({
		events: {
			'click .queue.remove' : 'removeFromPlaylist'
		},
		initialize: function() {
			this.template = _.template($('#album-template').html());
			_.bindAll(this, 'render', 'remove', 'updateState', 'updateTrack');
			this.player = this.options.player;
			this.model.bind('remove', this.remove);
			this.player.bind('change:state', this.updateState);
			this.player.bind('change:currentTrackIndex', this.updateTrack);
		},
		removeFromPlaylist : function() {
			this.options.playlist.remove(this.model);
			this.player.reset();
		},
		updateState : function() {
			var isAlbumCurrent = (this.player.currentAlbum() === this.model);
            $(this.el).toggleClass('current', isAlbumCurrent);
		},
		updateTrack: function() {
			var isAlbumCurrent = (this.player.currentAlbum() === this.model);
			if (isAlbumCurrent) {
				var currentTrack = this.player.get('currentTrackIndex');
				$(this.el).addClass("current");
				$(this.el).find('li').removeClass("current");
				$(this.el).find('li:nth-child('+ currentTrack +')').addClass("current");
			}
			this.updateState();
		}
	});
	
	window.PlaylistView = Backbone.View.extend({
		tagName : 'section',
		className : 'playlist',
		events :{
			'click .control.next' : 'next',
			'click .control.prev' : 'prev',
			'click .control.play' : 'play',
			'click .control.pause' : 'pause'
		},
		initialize : function() {
			_.bindAll(this, 'render', 'renderAlbum', 'queueAlbum', 'updateState', 'updateTrack');
			this.template = _.template($('#playlist-template').html());
			this.collection.bind('reset', this.render);
			this.collection.bind('add', this.renderAlbum);
			this.player = this.options.player;
			this.player.bind('change:state', this.updateState);
			this.player.bind('change:currentTrackIndex', this.updateTrack);
			this.createAudio();
			this.library = this.options.library;
			this.library.bind('select', this.queueAlbum);
		},
		createAudio : function() {
			this.audio = new Audio();
		},
		updateTrack: function() {
			this.audio.src = this.player.currentTrackUrl();
			if (this.player.get('state') == 'play') {
				this.audio.play();
			} else {
				//this.audio.stop();
			}
		},
		updateState : function() {
			this.updateTrack();
			this.$("button.play").toggle(this.player.isStopped());
			this.$("button.pause").toggle(this.player.isPlaying());
		},
		render : function() {
			$(this.el).html(this.template(this.player.toJSON()));
			this.$('button.play').toggle(this.player.isStopped());
			this.$('button.pause').toggle(this.player.isPlaying());
			
			return this;
		},
		queueAlbum : function(album) {
			this.collection.add(album);
		},
		renderAlbum : function(album) {
			var view = new PlaylistAlbumView({
				model : album,
				player : this.player,
				playlist : this.collection
			});
			this.$('ul').append(view.render().el);
		},
		next : function()  {
			this.player.nextTrack();
		},
		prev : function() {
			this.player.prevTrack();
		},
		play : function() {
			this.player.play();
		},
		pause : function() {
			this.player.pause();
		}
	});
	
	window.LibraryView = Backbone.View.extend({
		tagName: 'section',
		className: 'library',
		initialize : function() {
			_.bindAll(this, 'render');
			this.template = _.template($('#library-template').html());
			this.collection.bind('reset', this.render);
		},
		render : function() {
			var $albums, collection = this.collection;
			$(this.el).html(this.template({}));
			$albums = this.$('.albums');
			collection.each(function(album){
				var view = new LibraryAlbumView({
					model : album,
					collection : collection
				});
				$albums.append(view.render().el);
			});
			return this;
		}
	});
	
	window.BackboneTunes = Backbone.Router.extend({
		routes : {
			'' : 'home',
			'blank' : 'blank'
		},
		initialize : function() {
			this.playlistView = new PlaylistView({
				collection : window.player.playlist,
				player : 	 window.player,
				library :	 window.library
			});
			this.libraryView = new LibraryView({
				collection : window.library
			});
		},
		home : function() {
			var $container = $('#container');
			$container.empty();
			$container.append(this.playlistView.render().el);
			$container.append(this.libraryView.render().el);
		},
		blank : function() {
			var $container = $('#container');
			$container.empty().text("BLANK");
		}
	});
	
	$(function() {
		window.App = new BackboneTunes();
		Backbone.history.start();
	});
	
	
})(jQuery);
