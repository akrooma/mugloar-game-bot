package com.example.mugloar;

import com.example.mugloar.domain.game.bot.GameBot;
import com.example.mugloar.domain.game.state.GameState;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class MugloarApplication implements CommandLineRunner {

    private final GameBot gameBot;

    public static void main(String[] args) {
        SpringApplication.run(MugloarApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("STARTING TO PLAY THE GAME");
        GameState finalGameState = gameBot.playGame();

        System.out.println("FINAL GAME STATE:");
        System.out.println(finalGameState.getGameEndMessage());
        System.out.println(finalGameState);

        System.exit(0);
    }
}
