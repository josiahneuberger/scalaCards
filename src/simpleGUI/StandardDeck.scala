package simpleGUI

import scala.util.Random
import scala.util.matching.Regex

/********************************88
 * @author Josiah Neuberger
 * 
 * 	The CardPanel class is based on the very basic design by Ivan Macteki and can be found
 *  at http://java.macteki.com/2011/03/how-to-display-deck-of-playing-card-in.html. 
 *  (Licensed under: MIT License): http://opensource.org/licenses/MIT
 *  
 *  I have heavily modified and upgraded the framework to support my needs here.
 *  
 *  I referenced the following code for an introduction to Scala GUIs;
 *  	http://www.cis.upenn.edu/~matuszek/Concise%20Guides/Concise%20Scala%20GUI.html
 *   
 *  I also used Subtle Patterns for the face/back of the cards:
 *  	http://subtlepatterns.com/
 */
class StandardDeck extends Deck[(String, String, Int)] with StandardCards {
	this.deck = newDeck  
}

class StringDeck extends Deck[String] with StringCards with StringBlackjack {
  
	this.deck = newDeck
	
	def this(yourDeck: List[String]) {
	  this()
	  deck = yourDeck
	}
	
	def score(): Int = {
	  return score(this.deck)
	}
  
}

class StringChips extends Deck[Int] with PokerChips {
	this.deck = newDeck
	
	def add(c: Array[Int]) = {
		
		var l:Array[Int] = new Array[Int](3)
		for(i <- 0 to 2) {
			l(i) = deck(i) + c(i)
		}
		deck = List.fromArray(l)
	}
	
	def subtract(c: Array[Int]) = {
		
		var l:Array[Int] = new Array[Int](3)
		for(i <- 0 to 2) {
			l(i) = deck(i) - c(i)
		}
		deck = List.fromArray(l)
	}
	
	def value(): Int = {
		return value(this.deck.toArray)
	}
	
	def betvalue(): Int = {
	  return value(this.betAmount)
	}
	
	def red(): Int = { return deck(0) }
	def green(): Int = { return deck(1) }
	def black(): Int = { return deck(2) }
	
	def redbet(): Int = { return betAmount(0) }
	def greenbet(): Int = { return betAmount(1) }
	def blackbet(): Int = { return betAmount(2) }
}


abstract class Deck[L] {
	var deck:List[L] = List()
	
	
	def shuffle() = {
		
		deck = Random.shuffle(deck)
	}
	
	def dealt(cards: List[L]) {
	 	deck = deck :::  cards
	}
	
	def deal() : L = {
	  var top = deck.head
	  deck = deck.tail
	  
	  return top
	}
	
	def deal(numberOfCards:Int) : List[L] = {
	  var cards = deck.takeRight(numberOfCards)
	  deck = deck.dropRight(numberOfCards)
	  
	  return cards
	}
	
	def size(): Int = {
	  	return deck.size
	}
	
	def iterator: Iterator[L] = {
	    deck.iterator
	}
	
	def getList: List[L] = {
	  return deck
	}
	
	def setDeck(yourDeck: List[L]) = { deck = yourDeck }

}

trait StandardCards {
	
	def newDeck() : List[(String, String, Int)] = {
		return List( ("S", "A", 1), ("S", "2", 2), ("S", "3", 3), ("S", "4", 4), ("S", "5", 5), ("S", "6", 6),
							("S", "7", 7), ("S", "8", 8), ("S", "9", 9), ("S", "T", (10)), ("S", "J", (10)), ("S", "Q", (10)), ("S", "K", (10)),
						("H", "A", 1), ("H", "2", 2), ("H", "3", 3), ("H", "4", 4), ("H", "5", 5), ("H", "6", 6),
							("H", "7", 7), ("H", "8", 8), ("H", "9", 9), ("H", "T", (10)), ("H", "J", (10)), ("H", "Q", (10)), ("H", "K", (10)),
						("C", "A", 1), ("C", "2", 2), ("C", "3", 3), ("C", "4", 4), ("C", "5", 5), ("C", "6", 6),
							("C", "7", 7), ("C", "8", 8), ("C", "9", 9), ("C", "T", (10)), ("C", "J", (10)), ("C", "Q", (10)), ("C", "K", (10)),
						("D", "A", 1), ("D", "2", 2), ("D", "3", 3), ("D", "4", 4), ("D", "5", 5), ("D", "6", 6),
							("D", "7", 7), ("D", "8", 8), ("D", "9", 9), ("D", "T", (10)), ("D", "J", (10)), ("D", "Q", (10)), ("D", "K", (10))
					)
	}
}

trait StringCards {
	
	def newDeck() : List[String]  = {
		return List("SA", "S2", "SA","S2","S3","S4","S5","S6","S7","S8","S9","ST","SJ","SQ","SK", // spade
					"HA","H2","H3","H4","H5","H6","H7","H8","H9","HT","HJ","HQ","HK", // heart
					"CA","C2","C3","C4","C5","C6","C7","C8","C9","CT","CJ","CQ","CK", // club
					"DA","D2","D3","D4","D5","D6","D7","D8","D9","DT","DJ","DQ","DK")  // diamond
	}
}

trait StringBlackjack {
	
	def aceScore(s: Int, ac: Int): Int = { if ( ac==0 || ((ac*11+s)>21) ) return ac else return 11 }
	def score(deckList: List[String]): Int = {
	  val Facecard = "[SHCD]([JQK])".r
	  val Tencard = "[SHCD](T)".r
	  val Numbercard = "[SHCD]([2-9])".r
	  val Acecard = "[SHCD]([A])".r
	 
	  //Reference: http://thecodegeneral.wordpress.com/2012/03/25/switch-statements-on-steroids-scala-pattern-matching/
	  def matchCard(card: String) :Int = card match {
	      case Acecard(_) => 11
	      case Facecard(_) => 10
	      case Tencard(_) => 10
	      case Numbercard(value) => value.toInt
	      case _ => System.err.println("ERROR: bad deck we couldn't match this card:" + card); -999;
	  }
	  
	  var deckScore = 0
	  var aces = 0
	  
	  for (card <- deckList) {
		  var cardValue = matchCard(card)
		  if (cardValue == 11) { aces+=1; cardValue=0 }
		  
		  deckScore += cardValue
	  }
	  return deckScore + aceScore(deckScore, aces)
	}	
}

trait PokerChips {

	val value_black = 100
	val value_red = 5
	val value_green = 25
	val symbol = List("R","G","B")
	val number = Array(5,25,100)
	var betAmount = Array(0,0,0)
	
	val Blackchip = """(\d{0,3})B(\d{0,3})""".r
	val Redchip = """(\d{0,3})R(\d{0,3})""".r
	val Greenchip = """(\d{0,3})G(\d{0,3})""".r
	
	def matchChip(card: String) :(Int, Int) = card match {
	      case Blackchip(number,_) => (number.toInt, value_black)
	      case Redchip(number,_) => (number.toInt, value_red)
	      case Greenchip(number,_) => (number.toInt, value_green)
	      case _ => System.err.println("ERROR: bad deck we couldn't match this card:" + card); (0,0);
	}
	
	def value(chipList: Array[Int]): Int = {
	  var stashValue = 0
	  
	  var i= -1
	  for (cn <- chipList) {
		  i+=1
		  stashValue += number(i)*cn
	  }
	  return stashValue
	}
	
	def translateToBigChips(chipList: Array[Int]): Array[Int] = {
		var stashValue = value(chipList)


		var blackchips = stashValue/number(2)
		stashValue -= blackchips*number(2)
		
		var greenchips = stashValue/number(1)
		stashValue -= greenchips*number(1)
		
		var redchips = stashValue/number(0)
		stashValue -= redchips*number(0)
		
		assert(stashValue == 0)
		return Array(redchips, greenchips, blackchips)
	}
	
	def newDeck(): List[Int] = {
	  return List(8,5,1);
	}
}

  
