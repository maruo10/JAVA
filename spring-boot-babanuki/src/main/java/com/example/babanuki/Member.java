package com.example.babanuki;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer id;

  private String  name;       // 名前
  private Integer winCount;   // 勝利数
  private Integer loseCount;  // 敗北数
  private Integer sort;       // 並び順
  private Integer gameId;     // ゲームID
  private String  cards;      // 持ち札

  // id
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  // name
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  // winCount
  public Integer getWinCount() {
    return winCount;
  }
  public void setWinCount(Integer winCount) {
    this.winCount = winCount;
  }

  // loseCount
  public Integer getLoseCount() {
    return loseCount;
  }
  public void setLoseCount(Integer loseCount) {
    this.loseCount = loseCount;
  }

  // sort
  public Integer getSort() {
    return sort;
  }
  public void setSort(Integer sort) {
    this.sort = sort;
  }

  // gameId
  public Integer getGameId() {
    return gameId;
  }
  public void setGameId(Integer gameId) {
    this.gameId = gameId;
  }

  // cards
  public String getCards() {
    return cards;
  }
  public void setCards(String cards) {
    this.cards = cards;
  }


}