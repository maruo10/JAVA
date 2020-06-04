package com.example.babanuki;
import java.util.*;

public interface Player {
    public void setCard(List<Card> cards);

    public Integer choisePullPosition(Integer cardCount);

    public Integer choisePushPosition(Card card);

    public void onChoised(Card card, List<Card> newCards, boolean isMatch);

    public void onChoisePullPosition(Integer cardCount, Integer pullPosition, Integer puller);

    public void onChoisePushPosition(Integer cardCount, Integer pushPosition, Integer puller);
}