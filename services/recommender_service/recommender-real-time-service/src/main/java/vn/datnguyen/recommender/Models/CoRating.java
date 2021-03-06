package vn.datnguyen.recommender.Models;

public class CoRating {
    private String clientId, item1Id, item2Id;
    private int score, ratingItem1, ratingItem2; 
    
    public CoRating(String clientId, String item1Id, String item2Id, int score, int ratingItem1, int ratingItem2) {
        this.clientId = clientId;
        this.item1Id = item1Id;
        this.item2Id = item2Id;
        this.score = score;
        this.ratingItem1 = ratingItem1;
        this.ratingItem2 = ratingItem2;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getItem1Id() {
        return this.item1Id;
    }

    public String getItem2Id() {
        return this.item2Id;
    }

    public int getScore() {
        return this.score;
    }

    public int getRatingItem1() {
        return this.ratingItem1;
    }

    public int getRatingtem2() {
        return this.ratingItem2;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setItem1Id(String item1Id) {
        this.item1Id = item1Id;
    }

    public void setItem2Id(String item2Id) {
        this.item2Id = item2Id;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
