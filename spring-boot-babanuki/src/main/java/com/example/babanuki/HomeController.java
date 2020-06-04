package com.example.babanuki;

// import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@EnableAutoConfiguration
public class HomeController {

  @Autowired
  private GameRepository gamesRepository;

  @Autowired
  private MemberRepository memberRepository;

  Log log = LogFactory.getLog(HomeController.class);

  @GetMapping(path="/")
  String home(@RequestParam(value="name", required=false, defaultValue="masa") String name, Model model) {
    model.addAttribute("name", name);
    return "home";
  }

  @PostMapping(path="/start")
  String start(RedirectAttributes redirectAttributes, @RequestParam(value="count", required=false, defaultValue="3") Integer count) {

    // gamesテーブルにレコードを追加する
    Game g = new Game();
    g.setGameNum(count);
    g.setMemberNum(5);
    g.setNowGame(0);
    g.setNowMember(0);
    g = gamesRepository.save(g);

    String[] members = {"player1", "player2", "player3", "player4", "player5"};

    List<List<Card>> cards = Card.makeCards(members.length);

    for (int i = 0; i < members.length; i++) {
      Member m = new Member();
      m.setName(members[i]);
      m.setWinCount(0);
      m.setLoseCount(0);
      m.setSort(i);
      m.setGameId(g.getId());
      m.setCards(Card.list2string(cards.get(i)));
      m = memberRepository.save(m);
    }

    log.info("new game created! id=" + g.getId());

    // play画面に渡すパラメータ
    redirectAttributes.addFlashAttribute("id", g.getId());

    // play画面へリダイレクト
    return "redirect:/play";
  }

  /**
   * ゲーム中画面
   * @param gameId
   * @param model
   * @return
   */
  @GetMapping(path="/play")
  String play(@RequestParam(value="gameId", required=false, defaultValue="0") Integer gameId, Model model, RedirectAttributes redirectAttributes) {

    // パラメータとして渡ってこない場合は、start()からredirectでやってきたってこと。
    if (gameId <= 0) {
      try {
        // redirectでセットしたパラメータを受け取る。そして、Integerに変換。
        gameId = Integer.parseInt(model.getAttribute("id").toString());
      } catch(NumberFormatException e) {
        log.info("play() ERROR! id is not Integer! id=" + model.getAttribute("id"));
        return "redirect:/";
      }
    }

    // ------ DBからgameデータを取得 -------------

    // idでgameテーブルを検索する
    Optional<Game> ret = gamesRepository.findById(gameId);
    if (!ret.isPresent()) {
      log.info("play() ERROR! id is not found! id=" + gameId);
      return "redirect:/";
    }

    // 検索結果を取得
    Game g = ret.get();
    log.info("play() id=" + g.getId() + ", game num=" + g.getGameNum());

    // テンプレートで使用するパラメータをセット
    model.addAttribute("game", g);

    // ------ DBからmemberデータを取得 -------------
    List<Member> members = memberRepository.findByGameId(gameId);
    model.addAttribute("members", members);

    Player[] players = {new Player1(), new Player2(), new Player3(), new Player4(), new Player5()};

    List<List<Card>> membersCard = new ArrayList<List<Card>>();
    for (int i = 0; i < members.size(); i++) {
      String temp = members.get(i).getCards();
      if (temp.isEmpty()) {
        // 結果画面へ遷移する
        redirectAttributes.addFlashAttribute("id", gameId);
        return "redirect:/result";
      }
      List<Card> c = Card.toList(temp);
      membersCard.add(c);

      players[i].setCard(c);
    }



    // ------ 前の人 -------------
    Integer now = g.getNowMember();
    Member nowMember = members.get(now);
    Integer before = now - 1;
    if (before < 0) {
      before = members.size() - 1;
    }
    Member beforeMember = members.get(before);

    // ------ 前の人の手札から１枚抜く -------------
    List<Card> beforeCards = membersCard.get(before);

    // 画面に表示するために複製を作っておく
    List<Card> beforeCardsBackup = Card.toList(Card.list2string(beforeCards));

    int drawIndex = 0;
    drawIndex = players[now].choisePullPosition(beforeCards.size());

    // 皆に知らせる
    for (int i = 0; i < members.size(); i++) {
      players[i].onChoisePullPosition(beforeCards.size(), drawIndex, now);
    }

    // 引かれたカードを抽出
    Card card = beforeCards.get(drawIndex - 1);

    // 引かれたカードをリストから削除
    beforeCards.remove(drawIndex - 1);

    // memberテーブルを更新するためにカードデータを更新しておく
    beforeMember.setCards(Card.list2string(beforeCards));

    // 引いたカードをどの位置に入れるか
    Integer setIndex = - 1;
    setIndex = players[now].choisePushPosition(card);

    // 皆に知らせる
    for (int i = 0; i < members.size(); i++) {
      players[i].onChoisePushPosition(beforeCards.size(), setIndex, now);
    }

    List<Card> nowCards = membersCard.get(now);

    // 指定された位置にカードを追加
    nowCards.add(setIndex, card);

    // 引いたカードがマッチしたらカードリストから削除する
    int nowCardSize = nowCards.size();
    List<Card>  newCards = Card.deletePars(nowCards);
    boolean isMatch = newCards.size() != nowCardSize;

    // 新しいカードリストとマッチしたかどうかを引いた人に伝える
    players[now].onChoised(card, newCards, isMatch);

    // memberテーブルを更新するためにカードデータを更新しておく
    nowMember.setCards(Card.list2string(newCards));

    // 次の人
    Integer next = now + 1;
    if (next >= members.size()) {
      next = 0;
    }
    g.setNowMember(next);

    // DBに保存
    gamesRepository.save(g);
    memberRepository.save(beforeMember);
    memberRepository.save(nowMember);

    // 引いた人、引かれた人のどちらかのカードがなくなった場合
    if (beforeCards.size() == 0 || newCards.size() == 0) {
      // 結果画面へ遷移する
      redirectAttributes.addFlashAttribute("id", gameId);
      return "redirect:/result";
    }

    // ------ 画面表示のための処理 -------------

    model.addAttribute("beforeMember", beforeMember);
    model.addAttribute("nowMember", nowMember);
    model.addAttribute("card", card);

    // 引かれたカードも取り消し線で表示したいので、バックアップに置き換える
    membersCard.set(before, beforeCardsBackup);
    model.addAttribute("membersCard", membersCard);


    model.addAttribute("now", now);



    // テンプレートファイル名を返す
    return "play";
  }



  /**
   * 結果の画面
   * @param gameId
   * @param model
   * @return
   */
  @GetMapping(path="/result")
  String result(@RequestParam(value="gameId", required=false, defaultValue="0") Integer gameId,
                Model model, RedirectAttributes redirectAttributes) {

    // パラメータとして渡ってこない場合は、play()からredirectでやってきたってこと。
    if (gameId <= 0) {
      try {
        // redirectでセットしたパラメータを受け取る。そして、Integerに変換。
        gameId = Integer.parseInt(model.getAttribute("id").toString());
      } catch(NumberFormatException e) {
        log.info("result() ERROR! id is not Integer! id=" + model.getAttribute("id"));
        return "redirect:/";
      }
    }

    // ------ DBからgameデータを取得 -------------

    // idでgameテーブルを検索する
    Optional<Game> ret = gamesRepository.findById(gameId);
    if (!ret.isPresent()) {
      log.info("result() ERROR! id is not found! id=" + gameId);
      return "redirect:/";
    }

    // 検索結果を取得
    Game g = ret.get();
    log.info("result() id=" + g.getId() + ", game num=" + g.getGameNum());

    // テンプレートで使用するパラメータをセット
    model.addAttribute("game", g);

    // ------ DBからmemberデータを取得 -------------
    List<Member> members = memberRepository.findByGameId(gameId);
    model.addAttribute("members", members);

    Integer winner = -1;
    List<List<Card>> membersCard = new ArrayList<List<Card>>();
    for (int i = 0; i < members.size(); i++) {
      String temp = members.get(i).getCards();
      if (temp.isEmpty()) {
        winner = i;
      }
      membersCard.add(Card.toList(temp));
    }
    model.addAttribute("membersCard", membersCard);

    if (winner == -1) {
      // play画面へ遷移する
      redirectAttributes.addFlashAttribute("id", gameId);
      return "redirect:/play";
    }

    model.addAttribute("winner", winner);

    // テンプレートファイル名を返す
    return "result";
  }
}