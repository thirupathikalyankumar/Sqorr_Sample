package com.vernos.sqorr.utilities;


public class APIs {

    //    static final String BASE_URL = "http://40.70.208.170:8080/api/"; // IP -node
    static final String BASE_URL = "https://staging-backoffice.azurewebsites.net/api/"; //  IP - .net


    public static final String LOGIN = BASE_URL + "login";
    public static final String SIGN_UP_URL = BASE_URL + "account/create";
    public static final String LEAGUE = BASE_URL + "league";
    public static final String GET_CARDS = BASE_URL + "cards";
    public static final String CARD_DETAILS = BASE_URL + "card/";//"http://40.70.208.170:8080/api/card/5ce730856eb0612865b0f712"; /matchups
    public static final String MY_CARDS = BASE_URL + "mycards";
    public static final String ACCOUNT = BASE_URL + "auth/accounts/me";
    public static final String PROMOS = BASE_URL + "promos";
    public static final String WEB_LINKS = BASE_URL + "links";//https://staging-backoffice.azurewebsites.net/api/links
    public static final String SQORRTV_URL = BASE_URL + "sqorrtv";
    public static final String LOCATION_USER = BASE_URL + "validatelocation";

    //Password recovery;
    public static final String FORGOT_PASSWORD_URL=BASE_URL+"forgotPassword";

    //Change Password
    public static final String CHANGE_PWD_URL=BASE_URL+"account/updatepassword";

    //Update account data
    public static final String ACCOUNT_UPDATE_URL=BASE_URL+"account/update";

    public static final String SOCIAL_LOGIN_URL = BASE_URL + "sociallogin";

    public static final String  ADD_CARD_URL =BASE_URL + "account/addcard";

    public static final String CARDS_LIST_URL=BASE_URL+"account/cards";

    public static final String  EDIT_CARD_URL =BASE_URL + "account/editcard";

    public static final String  DELETE_CARD_URL = BASE_URL+ "account/deletecard";

   public  static  final String ADD_FUNDS_URL =BASE_URL + "addfunds";

   public static  final String PURCHASE_CARD=BASE_URL + "account/purchasecard";

   public static final String TRANSACTIONS_URL =BASE_URL+"account/transactions";

    public static final String GET_CARD_DATA =BASE_URL+"account/card";

}

