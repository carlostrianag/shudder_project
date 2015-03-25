QUEUE_SONGS = {}

OnContentFetched = (folderDTO)->
	$('#library-list').empty()
	$("<a class='list-group-item'><img src='assets/images/ic_unknown.png'>..</a>")
		.click((e) ->
			FolderController.getFolderContentById(JSON.stringify({id: folderDTO.parentFolder}))
			return)
		.appendTo('#library-list')	
	$.each(folderDTO.folders, (i, item) ->
		$("<a class='list-group-item'><img src='assets/images/ic_folder.png'>"+item.title.toUpperCase()+"</a>")
			.click((e) ->
				FolderController.getFolderContentById(JSON.stringify({id: item.id}))
				return)
			.appendTo('#library-list')
	)

	$.each(folderDTO.tracks, (i, item) ->

		if 	QUEUE_SONGS[item.id]
			divElement = $("<div id='check-song-"+item.id+"' class='check-box added-to-playlist'><img src='assets/images/ic_check.png'></div>")
			divElement.appendTo('#library-list')			
			listElement = $("<a id='song-"+item.id+"' class='list-group-item added-to-playlist'><img src='assets/images/ic_headphones.png'>"+item.title.toUpperCase()+" "+item.artist.toUpperCase()+"</a>")
		else
			divElement = $("<div id='check-song-"+item.id+"' class='check-box'><img src='assets/images/ic_check.png'></div>")
			divElement.appendTo('#library-list')
			listElement = $("<a id='song-"+item.id+"' class='list-group-item'><img src='assets/images/ic_headphones.png'>"+item.title.toUpperCase()+" "+item.artist.toUpperCase()+"</a>")
		listElement.click((e) ->
			PlayerController.playSong(JSON.stringify(item), false) if e.which is 1
			return)

		divElement.on("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd", ->
			if listElement.hasClass 'move-right'
				Debugger.display 'finish right'
				listElement.addClass('added-to-playlist')
				listElement.removeClass('move-right')
				$(this).addClass('added-to-playlist')
				$(this).removeClass('move-right')
			else
				$(this).removeClass('move-left')
				listElement.removeClass('move-left')
				$(this).removeClass('move-right')
				listElement.removeClass('move-right')	
				$(this).removeClass('added-to-playlist')
				listElement.removeClass('added-to-playlist')								
			return)

		listElement.bind('contextmenu', (e)->
			if !QUEUE_SONGS[item.id]
				$(this).addClass('move-right')
				divElement.addClass('move-right')
				QUEUE_SONGS[item.id] = item
				PlayerController.enqueueSong(JSON.stringify(item))
			else
				$(this).removeClass('added-to-playlist')
				divElement.removeClass('added-to-playlist')			
				$(this).addClass('move-left')
				divElement.addClass('move-left')
				QUEUE_SONGS[item.id] = null
				PlayerController.dequeueSong(JSON.stringify(item))

			return)
		listElement.appendTo('#library-list')
		return
	)

	return

drawSearchResults = (tracks) ->
	$('#library-list').empty()
	$.each(tracks, (i, item) ->

		if 	QUEUE_SONGS[item.id]
			divElement = $("<div class='check-box added-to-playlist'><img src='assets/images/ic_check.png'></div>")
			divElement.appendTo('#library-list')			
			listElement = $("<a class='list-group-item added-to-playlist'><img src='assets/images/ic_headphones.png'>"+item.title.toUpperCase()+" "+item.artist.toUpperCase()+"</a>")
		else
			divElement = $("<div class='check-box'><img src='assets/images/ic_check.png'></div>")
			divElement.appendTo('#library-list')
			listElement = $("<a class='list-group-item'><img src='assets/images/ic_headphones.png'>"+item.title.toUpperCase()+" "+item.artist.toUpperCase()+"</a>")
		listElement.click((e) ->
			PlayerController.playSong(JSON.stringify(item), false) if e.which is 1
			return)

		divElement.on("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd", ->
			if listElement.hasClass 'move-right'
				listElement.addClass('added-to-playlist')
				listElement.removeClass('move-right')
				$(this).addClass('added-to-playlist')
				$(this).removeClass('move-right')
			else
				$(this).removeClass('move-left')
				listElement.removeClass('move-left')
				$(this).removeClass('move-right')
				listElement.removeClass('move-right')	
				$(this).removeClass('added-to-playlist')
				listElement.removeClass('added-to-playlist')								
			return)

		listElement.bind('contextmenu', (e)->
			if !QUEUE_SONGS[item.id]
				$(this).addClass('move-right')
				divElement.addClass('move-right')
				QUEUE_SONGS[item.id] = item
				PlayerController.enqueueSong(JSON.stringify(item))
			else
				$(this).removeClass('added-to-playlist')
				divElement.removeClass('added-to-playlist')			
				$(this).addClass('move-left')
				divElement.addClass('move-left')
				QUEUE_SONGS[item.id] = null
				PlayerController.dequeueSong(JSON.stringify(item))

			return)
		listElement.appendTo('#library-list')
		return
	)	
	return

OnBuildFolderFinished = ->
	FolderController.getFolderContentById(JSON.stringify({id: -1}))
	return