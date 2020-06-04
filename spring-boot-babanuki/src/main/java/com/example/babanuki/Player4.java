package com.example.babanuki;

import java.util.*;

public class Player4 implements Player {

  private List<Card> cards;

  public void setCard(List<Card> cards) {
    this.cards = cards;
  }

  /**
   * 引くカードを選ぶ
   *
   * @param Integer cardCount 引く相手の持っているカード枚数
   * @return Integer 1〜cardCountの間の数字を返す
   */
  public Integer choisePullPosition(Integer cardCount) {
    // player4は常に中間のカードを引く
    return cardCount / 2;
  }

  /**
   * 引いたカードを入れる位置を決める
   *
   * @param String card 引いたカード
   */
  public Integer choisePushPosition(Card card) {
    // player4は引いたカードを先頭に追加(ただし、ジョーカーの時だけ末尾に入れる)
    if (card.isJocker()) {
      return this.cards.size();
    }
    return 0;
  }

  /**
   * 自分の引いたカードがやってきた
   *
   * @param String  card      引いたカード
   * @param String  newCards  新しいカード達
   * @param boolean isMatch   引いたカードが手元のカードとマッチしていればtrue
   * @return なし
   */
  public void onChoised(Card card, List<Card> newCards, boolean isMatch) {
    this.cards = newCards;
  }

  /**
   * 引くカードが選ばれたイベント
   *
   * @param Integer cardCount     引かれるカードリストの数
   * @param Integer pullPosition  引いたカードの位置
   * @param Integer puller        引いた人(メンバーリストのindex)
   */
  public void onChoisePullPosition(Integer cardCount, Integer pullPosition, Integer puller) {

  }

  /**
   * 引いたカードを入れたイベント
   *
   * @param Integer cardCount     引いたカードを入れるカードリストの数
   * @param Integer pushPosition  引いたカードを入れた位置
   * @param Integer puller        引いた人(メンバーリストのindex)
   */
  public void onChoisePushPosition(Integer cardCount, Integer pushPosition, Integer puller) {

  }

}