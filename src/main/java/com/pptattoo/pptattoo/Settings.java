package com.pptattoo.pptattoo;

public class Settings {

    public final static String[] fonts = { };
    public final static String[] styles = {"css/style.css"};
    public final static String[] themes = {"css/dark.css", "css/light.css", "css/blu.css"};

    public enum theme { DARK, LIGHT , BLU}
    public final static int DEFAULT_WINDOW_HEIGHT = 900;
    public final static int DEFAULT_WINDOW_WIDTH = 1100;
    public final static int MIN_WINDOW_HEIGHT = 700;
    public final static int MIN_WINDOW_WIDTH_LOGIN = 500;
    public final static int MIN_WINDOW_WIDTH_APP = 1000;
    public final static int WIDTH_BREAKPOINT = 1100;


    public enum roles {
        PROPRIETARIO,
        ARTISTA,
        COLLABORATORE,
        APPRENDISTA,
    }
    public final static String INIT_TITLE = "Tattoo-Piercing P&P";


    public final static int CELL_DB_DIPENDENTI = 5;
    public static final int CELL_DB_SERVIZI = 3;
    public static final int CELL_DB_CLIENTI = 5;
    public static int Cell_DB_APPUNTAMENTI = 7;

}
