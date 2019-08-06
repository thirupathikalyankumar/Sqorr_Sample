package com.vernos.sqorr.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Matchup {
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("playerA")
    @Expose
    private PlayerA playerA;
    @SerializedName("playerB")
    @Expose
    private PlayerB playerB;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @SerializedName("isUserPlayed")
    @Expose
    private Boolean isUserPlayed;
    @SerializedName("pickIndex")
    @Expose
    private int pickIndex;
    @SerializedName("winIndex")
    @Expose
    private int winIndex;
    @SerializedName("isFinished")
    @Expose
    private Boolean isFinished;
    @SerializedName("isCancelled")
    @Expose
    private Boolean isCancelled;

    public PlayerA getPlayerA() {
        return playerA;
    }

    public void setPlayerA(PlayerA playerA) {
        this.playerA = playerA;
    }

    public PlayerB getPlayerB() {
        return playerB;
    }

    public void setPlayerB(PlayerB playerB) {
        this.playerB = playerB;
    }

    public Boolean getIsUserPlayed() {
        return isUserPlayed;
    }

    public void setIsUserPlayed(Boolean isUserPlayed) {
        this.isUserPlayed = isUserPlayed;
    }

    public int getPickIndex() {
        return pickIndex;
    }

    public void setPickIndex(int pickIndex) {
        this.pickIndex = pickIndex;
    }

    public int getWinIndex() {
        return winIndex;
    }

    public void setWinIndex(int winIndex) {
        this.winIndex = winIndex;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

}