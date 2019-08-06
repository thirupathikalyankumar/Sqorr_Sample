package com.vernos.sqorr.pojos;

public class TvPojo {

    /*  {
        "url": "https://www.youtube.com/watch?v=Wws-p1AQuIc",
        "title": "NBA All-Star Swap",
        "description": "The Rockets and Thunder made a big time move yesterday when they agreed on a trade involving Russell Westbrook and Chris Paul.",
        "leagueId": "547e6e1e57489582581c7d8b",
        "durationInSeconds": 62,
        "isFeatured": false
    },*/


    private String url;
    private String title;
    private String description;
    private String leagueId;
    private String   durationInSeconds;
    private String isFeatured;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public void setDurationInSeconds(String durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public String getDurationInSeconds() {
        return durationInSeconds;
    }

    public String getIsFeatured() {
        return isFeatured;
    }
}
