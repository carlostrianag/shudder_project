var MAX_Y, PLAYING;

MAX_Y = 225;

window.PARENT_FOLDER = null;

PLAYING = true;

window.SCROLL_POSITION_FOLDER_ID = {};

window.SCROLL_POS = 0;

window.DRAGGIN = false;

window.PRESSED = false;

window.TOTAL_SECONDS = 0;

$(document).ready(function() {
  document.oncontextmenu = function() {
    return false;
  };
  PlayerController.initPlayer();
  TCPController.init();
  $('#elapsed-time-text').text('0:00');
  $('#remaining-time-text').text('0:00');
  $('#slider').val(0);
  $('.library-tab').click(function(e) {
    $('.library-tab').addClass('active-tab');
    $('.playlist-tab').removeClass('active-tab');
    $('#library-list').addClass('active-content');
    $('#playlist-list').removeClass('active-content');
    $('.search-bar').addClass('active-content');
    $('.bottom-container').removeClass('no-search');
  });
  $('.playlist-tab').click(function(e) {
    $('.playlist-tab').addClass('active-tab');
    $('.library-tab').removeClass('active-tab');
    $('#library-list').removeClass('active-content');
    $('#playlist-list').addClass('active-content');
    $('.search-bar').addClass('active-content');
    $('.bottom-container').addClass('no-search');
  });
  $('#back-folder').click(function(e) {
    $('#library-list').empty();
    window.SCROLL_POS = window.SCROLL_POSITION_FOLDER_ID[window.PARENT_FOLDER] ? window.SCROLL_POSITION_FOLDER_ID[window.PARENT_FOLDER] : 0;
    FolderController.getFolderContentById(JSON.stringify({
      id: window.PARENT_FOLDER
    }));
    delete window.SCROLL_POSITION_FOLDER_ID[window.PARENT_FOLDER];
  });
  $('#artist-checkbox').click(function(e) {
    $(this).addClass('checkbox-selected');
    $('#title-checkbox').removeClass('checkbox-selected');
    FolderController.orderTracksByArtistName();
  });
  $('#title-checkbox').click(function(e) {
    $(this).addClass('checkbox-selected');
    $('#artist-checkbox').removeClass('checkbox-selected');
    FolderController.orderTracksBySongName();
  });
  $('input[name=query]').keyup(function(e) {
    if (e.keyCode === 27) {
      $(this).val("");
    } else {

    }
    FolderController.search($(this).val());
  });
  $('#back-btn').click(function(e) {
    PlayerController.back();
  });
  $('#stop-btn').click(function(e) {
    PlayerController.stop();
  });
  $('#play-btn').click(function(e) {});
  $('#next-btn').click(function(e) {
    PlayerController.next();
  });
  $('#play-btn').click(function(e) {
    PlayerController.resume();
  });
  $('#pause-btn').click(function(e) {
    PlayerController.pause();
  });
  $('#volume-slider').on("change", (function() {
    PlayerController.setVolumeFromValue($(this).val() / 100, true);
  }));
  $('#slider').on('mousedown', (function() {
    window.PRESSED = true;
  }));
  $('#slider').on('mousemove', (function() {
    if (window.PRESSED) {
      window.DRAGGIN = true;
    }
  }));
  $('#slider').on('mouseup', (function() {
    var percentage;
    percentage = $(this).val() / window.TOTAL_SECONDS;
    if (window.DRAGGIN) {
      PlayerController.seek(percentage);
    }
    window.DRAGGIN = false;
    window.PRESSED = false;
  }));
  $('.library-tab').click();
  $('#library-list').height($(window).height() - MAX_Y);
  $('#playlist-list').height($(window).height() - MAX_Y);
  $('.tab-content').height($(window).height() - MAX_Y);
  $('#folder-selector').click(function() {
    FolderController.openFile();
  });
  FolderController.getFolderContentById(JSON.stringify({
    id: -1
  }));
  window.location.href += "#error";
});

$(window).resize(function() {
  $('#library-list').height($(window).height() - MAX_Y);
  $('#playlist-list').height($(window).height() - MAX_Y);
  $('.tab-content').height($(window).height() - MAX_Y);
});
