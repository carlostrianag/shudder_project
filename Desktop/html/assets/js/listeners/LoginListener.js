var OnFailedLogin, OnSuccessfulLogin;

OnSuccessfulLogin = function() {
  loadPage('home_page.html');
};

OnFailedLogin = function() {
  showErrorDialog('Invalid email/password combination, please try again.', true);
};