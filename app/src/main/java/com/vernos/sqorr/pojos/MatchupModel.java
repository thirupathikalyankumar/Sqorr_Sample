package com.vernos.sqorr.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MatchupModel {
    @SerializedName("cardId")
    @Expose
    private String cardId;
    @SerializedName("cardTitle")
    @Expose
    private String cardTitle;
    @SerializedName("isPurchased")
    @Expose
    private Boolean isPurchased;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("currencyTypeIsTokens")
    @Expose
    private Boolean currencyTypeIsTokens;
    @SerializedName("matchupsPlayed")
    @Expose
    private int matchupsPlayed;
    @SerializedName("matchupsWon")
    @Expose
    private int matchupsWon;
    @SerializedName("settlementDate")
    @Expose
    private String settlementDate;
    @SerializedName("payout")
    @Expose
    private int payout;
    @SerializedName("matchups")
    @Expose
    private List<Matchup> matchups = null;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public Boolean getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(Boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Boolean getCurrencyTypeIsTokens() {
        return currencyTypeIsTokens;
    }

    public void setCurrencyTypeIsTokens(Boolean currencyTypeIsTokens) {
        this.currencyTypeIsTokens = currencyTypeIsTokens;
    }

    public int getMatchupsPlayed() {
        return matchupsPlayed;
    }

    public void setMatchupsPlayed(int matchupsPlayed) {
        this.matchupsPlayed = matchupsPlayed;
    }

    public int getMatchupsWon() {
        return matchupsWon;
    }

    public void setMatchupsWon(int matchupsWon) {
        this.matchupsWon = matchupsWon;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public int getPayout() {
        return payout;
    }

    public void setPayout(int payout) {
        this.payout = payout;
    }

    public List<Matchup> getMatchups() {
        return matchups;
    }

    public void setMatchups(List<Matchup> matchups) {
        this.matchups = matchups;
    }

}

