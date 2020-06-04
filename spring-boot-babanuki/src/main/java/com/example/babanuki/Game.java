package com.example.babanuki;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Game {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer id;

  private Integer gameNum;   // ゲーム数
  private Integer memberNum; // メンバー数
  private Integer nowGame;   // 現在進行中のゲーム番号
  private Integer nowMember; // 現在対象のメンバー番号

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getGameNum() {
    return gameNum;
  }
  public void setGameNum(Integer num) {
    this.gameNum = num;
  }

  public Integer getMemberNum() {
    return memberNum;
  }
  public void setMemberNum(Integer num) {
    this.memberNum = num;
  }

  public Integer getNowGame() {
    return nowGame;
  }
  public void setNowGame(Integer nowGame) {
    this.nowGame = nowGame;
  }

  public Integer getNowMember() {
    return nowMember;
  }
  public void setNowMember(Integer nowMember) {
    this.nowMember = nowMember;
  }
}