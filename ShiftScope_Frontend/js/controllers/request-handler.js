function RequestHandler(_request) {
	var userId = parseInt(request.userId);
	var from = request.from;
	var type = parseInt(request.type);
	var response = parseInt(request.response);
	var content = request.content;


	this.drawPlaylist = function(){
			$('#library').empty();
			for (var i = 0; i < content.library.length; i++) {
				str = "";
				if (content.library[i].isFolder) {
					str = '<a href="#" class="list-group-item folder" data-path="'+content.library[i].absolutePath+'" data-key="'+content.library[i].id+'"><i class="fa fa-folder-o fa-1x"></i> '+content.library[i].title+' <span class="pull-right text-muted small"><em></em> </span> </a>';
				} else {
					str = '<a href="#" class="list-group-item song" data-path="'+content.library[i].absolutePath+'" data-key="'+content.library[i].id+'"><i class="fa fa-music fa-1x"></i> '+content.library[i].title+' <span class="pull-right text-muted small"><em>'+content.library[i].artist+'</em> </span> </a>';
				}
				
				$('#library').append(str);
			}

			$('.song').click(function(e) {
				e.preventDefault();
				request = new Request(124, "MOBILE", 8, 200, {id: parseInt($(this).data("key")), absolutePath: $(this).data("path").toString()});
				s.send(JSON.stringify(request));
				
			});

			$('.folder').click(function(e) {
				e.preventDefault();
				request = new Request(124, "MOBILE", 1, 200, {parentFolder: $(this).data("path").toString()});
				s.send(JSON.stringify(request));
				$('#back-folder').data("current", $(this).data("path").toString());


			});

			if (content.isPlaying) {
				$('#current-song').text(content.currentSong + " - " + content.currentArtist);
			}
		};

	this.handle = function() {
		switch(type) {

			case FETCH:
				this.drawPlaylist();
				break;

			case BACK_FOLDER:
				this.drawPlaylist();
				$('#back-folder').data("current", content.currentFolder);


				break;
			case NO_PAIR_FOUND:
			case CONNECTION_LOST:
				s.close();
				break;

			case CURRENT_PLAYING:
				$('#current-song').text(content.currentSong + " - " + content.currentArtist);
				break;
		};



	};
}