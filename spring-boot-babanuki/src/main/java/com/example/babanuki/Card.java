package com.example.babanuki;

import java.util.*;


public class Card {


  public static final String MARK_DIAMOND  = "D";
  public static final String MARK_HEART    = "H";
  public static final String MARK_SPADE    = "S";
  public static final String MARK_CLUB     = "C";
  public static final String MARK_JOCKER   = "J";

  private String mark;
  private String number;

  public Card(String mark, String number) {
    this.mark = mark;
    this.number = number;
  }
  public String toString() {
    return this.mark + this.number;
  }
  public boolean equals(Card c) {
    return this.toString().equals(c.toString());
  }
  public String getNumber() {
    return this.number;
  }
  public boolean isJocker() {
    return this.mark.equals(MARK_JOCKER);
  }

  public static List<List<Card>> makeCards(int memberNum) {

    List<Card> list = new ArrayList<Card>();
    String[] marks = { MARK_DIAMOND, MARK_HEART, MARK_SPADE, MARK_CLUB };

    // カード配列を作る
    for (int i = 0; i < 13; i++) {
      for (int j = 0; j < 4; j++) {
        list.add(new Card(marks[j], String.format("%02d", i + 1)));
      }
    }
    list.add(new Card(MARK_JOCKER, String.format("%02d", 0)));

    // シャッフルする
    Collections.shuffle(list);

    // メンバー分のカード配列を作る
    // String[] cards = new String[memberNum];
    List<List<Card>> membersCard = new ArrayList<List<Card>>(memberNum);
    for (int i = 0; i < memberNum; i++) {
      membersCard.add(new ArrayList<Card>());
    }

    // 各メンバーに追加する
    int m = 0;
    for (int i = 0; i < list.size(); i++) {
      membersCard.get(m).add(list.get(i));

      m++;
      if (m >= memberNum) {
        m = 0;
      }
    }

    // ペアを取り除く
    for (int i = 0; i < memberNum; i++) {
      List<Card> newCards = deletePars(membersCard.get(i));
      membersCard.set(i, newCards);
    }

    return membersCard;
  }

  public static List<Card> deletePars(List<Card> cards) {
    if (cards.isEmpty()) {
      return cards;
    }
    for (int i = 0; i < cards.size(); i++) {
      for (int j = i + 1; j < cards.size(); j++) {
        if (cards.get(i).getNumber().equals(cards.get(j).getNumber())) {
          cards.remove(j);
          cards.remove(i);
          cards = deletePars(cards);
          return cards;
        }
      }
    }

    return cards;
  }

  public static int getMaisu(String cards) {
    return cards.length() / 3;
  }

  public static String getCard(String cards, int index) {
    return cards.substring(index * 3, index * 3 + 3);
  }

  public static List<Card> toList(String cards) {
    List<Card> list = new ArrayList<Card>();
    for (int i = 0; i < getMaisu(cards); i++) {
      String temp = getCard(cards, i);
      Card card = new Card(temp.substring(0, 1), temp.substring(1, 3));
      list.add(card);
    }
    return list;
  }
  public static String list2string (List<Card> list) {
    String cards = "";
    for (int i = 0; i < list.size(); i++) {
      cards += list.get(i);
    }
    return cards;
  }

}
