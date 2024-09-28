package kr.chide1.duel.duel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PlayerStatus {
    private int point;
    private int winCount;
    private int loseCount;
}
