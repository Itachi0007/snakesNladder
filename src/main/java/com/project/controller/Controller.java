package com.project.controller;

import java.util.*;

import com.project.models.Board;
import com.project.models.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class Controller {
	
	final static int WINPOINT = 100;

	private static final Logger log = LoggerFactory.getLogger(Controller.class);

	private Board board = new Board();
	Vector<Player> players = new Vector<Player>();

	// get position by ID
	@GetMapping(path = "/player/{id}")
	public ResponseEntity<?> getPosition(@PathVariable Integer id) {
		Player player = players.get(id);
		int position = player.getPosition();
		return new ResponseEntity("Player " + id + " is at " + position, HttpStatus.OK);
	}

	// get all players
	@GetMapping(path = "/player/all")
	public ResponseEntity<?> getPositionOfAll() {
		return new ResponseEntity(players, HttpStatus.OK);
	}

	// add new player
	@PostMapping(path = "/player/new")
	public ResponseEntity<?> addPlayer(@RequestBody Player player) {
		players.add(player);
		return new ResponseEntity("New player has been added", HttpStatus.CREATED);
	}

	// add new snakes
	@PostMapping(path = "/snake/new")
	public ResponseEntity<?> addSnake(@RequestParam Integer start, @RequestParam Integer end) {
		board.addSnake(start, end);
		return new ResponseEntity(board.viewSnakes(), HttpStatus.CREATED);
	}

	// add new ladders
	@PostMapping(path = "/ladder/new")
	public ResponseEntity<?> addLadder(@RequestParam Integer start, @RequestParam Integer end) {
		board.addLadder(start, end);
		return new ResponseEntity(board.viewLadders(), HttpStatus.CREATED);
	}

	// roll the dice
	@GetMapping(path = "/play/{id}")
	public ResponseEntity<?> play(@PathVariable Integer id) {
		int dice = rollDice();
		log.info("Dice shows " + dice);
		int i=0;
		Player player = null;
		while(i < players.size()) {
			if(players.get(i).getId() == id) {
				log.info("chala");
				player = players.get(i);
				break;
			}
			i++;
		}
		int initialPosition = player.getPosition();
		int finalPosition = initialPosition + dice;
		if(finalPosition > 100) {
			finalPosition = initialPosition;
		}
		
		// check if player won
		if(isWin(finalPosition)) {
			return new ResponseEntity("Initial position - "+ initialPosition + "\nDice - " + dice + "\nCONGRATULATIONS ! You won", HttpStatus.OK);
		}
		
		// check if bitten by snake or got a ladder
		if(board.checkSnake(finalPosition) != 0) {
			log.info("Got bitten by snake");
			finalPosition = board.checkSnake(finalPosition);
			player.setPosition(finalPosition);
			players.set(i, player);
			
			return new ResponseEntity("Initial position - "+ initialPosition + "\nDice - " + dice + "\nYou got swallowed by a snake to "+ finalPosition, HttpStatus.OK);
		}
		else if(board.checkLadder(finalPosition) != 0) {
			log.info("Climbed a ladder");
			finalPosition = board.checkLadder(finalPosition);
			player.setPosition(finalPosition);
			players.set(i, player);
			
			return new ResponseEntity("Initial position - "+ initialPosition + "\nDice - " + dice + "\nYou got a ladder to " + finalPosition, HttpStatus.OK);
		}

		player.setPosition(finalPosition);
		players.set(i, player);
		return new ResponseEntity("Initial position - "+ initialPosition + "\nDice - " + dice + "\nYour new position is " + finalPosition, HttpStatus.OK);

	}

	// helper function to roll dice
	public static int rollDice() {
		int n = 0;
		Random r = new Random();
		n = r.nextInt(7);
		return (n == 0 ? 1 : n);
	}
	// helper function to check if he won
	public boolean isWin(int position)
	{
	    return WINPOINT == position;
	}
}
