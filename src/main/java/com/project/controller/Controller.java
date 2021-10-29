package com.project.controller;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class Controller {
	
	final static int WINPOINT = 100;

	private static final Logger log = LoggerFactory.getLogger(Controller.class);

	private Map<Integer, Integer> snake = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> ladder = new HashMap<Integer, Integer>();
	Vector<Integer> playerPosition = new Vector<Integer>();

	// get position by ID
	@GetMapping(path = "/player/{id}")
	public ResponseEntity<?> getPosition(@PathVariable Integer id) {
		int position = playerPosition.get(id);
		return new ResponseEntity("Player " + id + " is at " + position, HttpStatus.OK);
	}

	// get position of all players
	@GetMapping(path = "/player/all")
	public ResponseEntity<?> getPositionOfAll() {
		Iterator value = playerPosition.iterator();
		Vector<String> body = new Vector<String>();
		int i=0;
		while (value.hasNext()) {
			body.add("Player "+ i + " is at " + value.next().toString());
			i++;
		}
		return new ResponseEntity(body, HttpStatus.OK);
	}

	// add new player
	@PostMapping(path = "/player/new")
	public ResponseEntity<?> addPlayer(@RequestParam Integer position) {
		playerPosition.add(position);
		return new ResponseEntity("New player has been added", HttpStatus.CREATED);
	}

	// add new snakes
	@PostMapping(path = "/snake/new")
	public ResponseEntity<?> addSnake(@RequestParam Integer start, @RequestParam Integer end) {
		snake.put(start, end);
		return new ResponseEntity("Snakes : " + snake.entrySet(), HttpStatus.CREATED);
	}

	// add new ladders
	@PostMapping(path = "/ladder/new")
	public ResponseEntity<?> addLadder(@RequestParam Integer start, @RequestParam Integer end) {
		ladder.put(start, end);
		return new ResponseEntity("Ladders : " + ladder.entrySet(), HttpStatus.CREATED);
	}

	// roll the dice
	@GetMapping(path = "/play/{id}")
	public ResponseEntity<?> play(@PathVariable Integer id) {
		int dice = rollDice();
		log.info("Dice shows " + dice);
		int initialPosition = playerPosition.get(id);
		int finalPosition = initialPosition + dice;
		if(finalPosition>100) {
			finalPosition = initialPosition;
		}
		
		// check if player won
		if(isWin(finalPosition)) {
			return new ResponseEntity("Initial position - "+ initialPosition + "\nDice - " + dice + "\nCONGRATULATIONS ! You won", HttpStatus.OK);
		}
		
		// check if bitten by snake or got a ladder
		if(snake.containsKey(finalPosition)) {
			log.info("Got bitten by snake");
			finalPosition = snake.get(finalPosition);
			playerPosition.set(id, finalPosition);
			
			return new ResponseEntity("Initial position - "+ initialPosition + "\nDice - " + dice + "\nYou got swallowed by a snake to "+ finalPosition, HttpStatus.OK);
		}
		else if(ladder.containsKey(finalPosition)) {
			log.info("Climbed a ladder");
			finalPosition = ladder.get(finalPosition);
			playerPosition.set(id, finalPosition);
			
			return new ResponseEntity("Initial position - "+ initialPosition + "\nDice - " + dice + "\nYou got a ladder to " + finalPosition, HttpStatus.OK);
		}
		
		playerPosition.set(id, finalPosition);
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
