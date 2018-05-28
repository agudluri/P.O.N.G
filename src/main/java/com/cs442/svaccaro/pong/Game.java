package com.cs442.svaccaro.pong;
public class Game
{
    String gamename;
    int dbID;
    String genre;
    String releaseyr;
    String esrbRating;
    String descript;
    double lowPrice;
    double highPrice;
    String imageURL;
    Double averageRating;
    Double popularity;
    String category;

    public Game()
    {
        gamename="Unknown";
        dbID=-1;
        genre="general";
        releaseyr="00/00/00";
        esrbRating="NA";
        descript="NA";
        lowPrice=-1;
        highPrice=-1;
    }

    public Game(String newGamename,int id, String newGenre, String newDescript,String newRYr, String newERSB,double newLow,double newHigh)
    {
        gamename=newGamename;
        dbID=id;
        genre=newGenre;
        releaseyr=newRYr;
        esrbRating=newERSB;
        descript=newDescript;
        lowPrice=newLow;
        highPrice=newHigh;

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    //Gets
    public String getGamename()
    {
        return gamename;
    }
    public int getDbID()
    {
        return dbID;
    }
    public String getGenre()
    {
        return genre;
    }
    public String getReleaseyr()
    {
        return releaseyr;
    }
    public String getEsrbRating()
    {
        return esrbRating;
    }
    public String getDescript()
    {
        return descript;
    }
    public double getLowPrice()
    {
        return lowPrice;
    }
    public double getHighPrice()
    {
        return highPrice;
    }

    //SETS
    public void setGamename(String newGame)
    {
        gamename=newGame;
    }
    public void setDbID(int newID)
    {
        dbID=newID;
    }
    public void setGenre(String newGen)
    {
        genre=newGen;
    }
    public void setReleaseyr(String yr)
    {
        releaseyr=yr;
    }
    public void setEsrbRating(int dbVal)
    {
        if(dbVal==-1)
        {
            esrbRating="NA";
        }
        else
        {
            String [] esrbTranslation = {"RP","EC","E","E10+","T","M","AO"};
            esrbRating=esrbTranslation[dbVal-1];
        }
    }
    public void setDescript(String newDescript)
    {
        descript=newDescript;
    }
    public void setLowPrice(double low)
    {
        lowPrice=low;
    }
    public void setHighPrice(double high)
    {
        highPrice=high;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(int category) {
        String[] categoryStr = {"Main Game","DLC Add On","Expansion","Bundle","Standalone"};
        this.category = categoryStr[category];
    }

    public String toString()
    {
        return gamename+";"+dbID+";"+releaseyr+";"+esrbRating+";"+descript+";"+lowPrice+";"+highPrice;
    }
}
