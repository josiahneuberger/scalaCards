package simpleGUI

import scala.swing._
import scala.swing.BorderPanel.Position._
import event._
import java.awt.{ Color, Graphics2D }
import scala.util.Random
import java.awt.TexturePaint
import javax.imageio.ImageIO
import java.io.File
import java.awt.geom.Rectangle2D
import javax.swing.BorderFactory
import javax.swing.border.EtchedBorder
import java.awt.Font


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
object SimpleGUI extends SimpleSwingApplication {


  def top = new MainFrame { 
   title = "Josiah Neuberger's BlackJack"
   var gameFont = new Font("Bodoni MT Black", Font.BOLD, 30)
   var messageFont = new Font("Bodoni MT Black", Font.BOLD, 20)
    
   val button_hit = new Button {
		text = "Hit!"
		font_=(gameFont)
		borderPainted = true
		enabled = false
		
		tooltip = "Click to get another card from the dealer."
	}
	
	val button_pass = new Button {
		text = "Pass!"
		font_=(gameFont)
		borderPainted = true
		enabled = false
		tooltip = "Click to pass to Dealer."
	}
	
	val button_newgame = new Button {
		text = "New Game!"
		font_=(gameFont)
		borderPainted = true
		enabled = true
		visible = false
		tooltip = "Click to start a new game!"
	}
	
	val button_red = new Button {
		text = "Bet Red Chip!"
		font_=(gameFont)
		borderPainted = true
		enabled = true
		tooltip = "Click to pass to Dealer."
	}
	val button_green = new Button {
		text = "Bet Green Chip!"
		font_=(gameFont)
		borderPainted = true
		enabled = true
		tooltip = "Click to pass to Dealer."
	}
	val button_black = new Button {
		text = "Bet Black Chip!"
		font_=(gameFont)
		borderPainted = true
		enabled = true
		tooltip = "Click to pass to Dealer."
	}
	val button_betfinish = new Button {
		text = "Finished with bet!"
		font_=(gameFont)
		borderPainted = true
		enabled = true
		tooltip = "Click to let the deal start"
	}

    var border2 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), 
	BorderFactory.createEtchedBorder(EtchedBorder.LOWERED))	

	//Create/Draw initial chips, cards, and decks.
	var gameDeck = new StringDeck
    gameDeck.shuffle
    var playerDeck = new StringDeck(gameDeck.deal(2))
	var dealerDeck = new StringDeck(gameDeck.deal(2))
	var playerCanvas = CardCanvas.getInstance(400,300, playerDeck, false, false, true, 40,15)
	var dealerCanvas = CardCanvas.getInstance(400,300, dealerDeck, true, false, false, 40,15)
    var gameCanvas = CardCanvas.getInstance(600, 400, gameDeck, false, false, false, 10, 2)
    var playerchips = new StringChips()
    var playerChipCanvas = ChipCanvas.getInstance(600,300, playerchips, ChipCanvas.TYPE_STACKED_VERTICAL_RIGHT)
    var bettedChips = new StringChips()
    bettedChips.deck = List(0,0,0)
    var bettedChipsCanvas = ChipCanvas.getInstance(400, 300, bettedChips, ChipCanvas.TYPE_STACKED_VERTICAL_RIGHT_INLINEX)
    
    
    val messageArea = new TextArea {
      text = "************************************************\nHello!\nLet's start the game already!!\n"
      background = Color.white
      font_=(messageFont)
      columns = 20
      rows = 40
    }
    
    var buttons_layout = new GridPanel(4,1) {
    	this.opaque = false
      
    	contents += button_hit
      	contents += button_pass
      	contents += button_betfinish

        contents += button_newgame
    }
    
    var east_layout = new GridPanel(2,1) {
      this.opaque = false
      contents += messageArea
      contents += buttons_layout
    }
    
    val playerArea = new TextArea {
      text = "Bet: $0"
      background = Color.white
      font_=(messageFont)
    }
    
    val moneyArea = new TextArea {
      text = "Money: $" + playerchips.value
      background = Color.white
      font_=(messageFont)
    }
    
   var bets_layout = new GridPanel(6, 1) {
    	this.opaque = false
    	
    	contents += button_red
    	contents += button_green
    	contents += button_black
    	contents += playerArea
    	contents += moneyArea
    }
   
   var dealer_southlayout = new GridPanel(1,2) {
	   this.opaque = false
	   
	   contents += dealerCanvas
	   contents += bettedChipsCanvas
   }
   
   var player1_space = new GridPanel(1,3) {

      this.opaque = false
      contents += playerCanvas
      contents += playerChipCanvas
      contents += bets_layout
      
   }
    
    var dealer_space = new BorderPanel {

      this.opaque = false
      this.border = border2
      layout(gameCanvas) = North
      layout(dealer_southlayout) = South
    }
    
    var panelFelt = new BorderPanel {
      preferredSize = new Dimension(1500, 800)
      
      layout(dealer_space) = Center
      layout(player1_space) = South
      layout(east_layout) = East
      
      override def paintComponent(g: Graphics2D) = {
        
    	  // Start by erasing this Canvas
    	  g.clearRect(0, 0, size.width, size.height)
    	  
    	  //http://subtlepatterns.com/patterns/pool_table.zip
    	  var imgFile = new File("images/felt.png")
    	  var buff_img = ImageIO.read(imgFile)
    	  var r = new Rectangle(0, 0, buff_img.getWidth(), buff_img.getHeight())
    	  
    	  var tp = new TexturePaint(buff_img,r)
    	  g.setPaint(tp)
    	  
    	  
    	  var felt_background = new Rectangle2D.Double(0,0, size.width, size.height)
    	  g.fill(felt_background)
      }
      
      
    }
    
   
    
    contents = panelFelt
    
    
    menuBar = new MenuBar {
      contents += new Menu("File") {
        contents += new MenuItem(Action("Exit") {
          sys.exit(0)
        })
      }
    }

    listenTo(button_hit)
    listenTo(button_pass)
    listenTo(button_newgame)
    listenTo(button_betfinish)
    listenTo(button_red)
    listenTo(button_green)
    listenTo(button_black)
    

    reactions += {
      case ButtonClicked(component) if component == button_newgame =>
       
        if(gameDeck.size < 12) {
        	gameDeck = new StringDeck
        	gameDeck.shuffle
        }
       	
        playerDeck = new StringDeck(gameDeck.deal(2))
	    dealerDeck = new StringDeck(gameDeck.deal(2))
        
       	dealerCanvas.topcard_flip = true
	  	dealerCanvas.isFaceUp = false
	    playerCanvas.update(playerDeck)
	    dealerCanvas.update(dealerDeck)
	    gameCanvas.update(gameDeck)
	    dealerCanvas.repaint
	    playerCanvas.repaint
	    gameCanvas.repaint
	    
	    
	    button_pass.enabled_=(false)
       	button_hit.enabled_=(false)
       	if (playerchips.red > 0) button_red.enabled_=(true)
	    if (playerchips.green > 0) button_green.enabled_=(true)
       	if (playerchips.black > 0) button_black.enabled_=(true)
	    button_newgame.visible_=(false)
	    button_betfinish.visible_=(true)
	    bets_layout.visible_=(true)
	    playerArea.text_=("Bet $:0")
	    playerchips.betAmount = Array(0,0,0)
	    messageArea.text_=("********************************************\nLet's start the next game already!!\n")
	    moneyArea.text_=("Money: $" + playerchips.value)
	    playerCanvas.repaint
	    
	    bettedChips.deck = List(0,0,0)
	    bettedChipsCanvas.update(bettedChips)
	    bettedChipsCanvas.repaint
	    
	    
	  case ButtonClicked(component) if component == button_red =>
	    if (playerchips.red > 0) { 
	      playerchips.betAmount(0) += 1; playerArea.text_=("Bet: $" + playerchips.betvalue) 
	      playerchips.subtract(Array(1,0,0))
	      playerChipCanvas.update(playerchips)
	      playerChipCanvas.repaint()
	      
	      bettedChips.add(Array(1,0,0))
	      bettedChipsCanvas.update(bettedChips)
	      bettedChipsCanvas.repaint()
	    }
	    else { button_red.enabled_=(false) }
	  case ButtonClicked(component) if component == button_green =>
	    if (playerchips.green > 0) { 
	      playerchips.betAmount(1) += 1; playerArea.text_=("Bet: $" + playerchips.betvalue)
	      playerchips.subtract(Array(0,1,0))
	      playerChipCanvas.update(playerchips)
	      playerChipCanvas.repaint()
	      
	      bettedChips.add(Array(0,1,0))
	      bettedChipsCanvas.update(bettedChips)
	      bettedChipsCanvas.repaint()
	    }
	    else { button_green.enabled_=(false) }
	  case ButtonClicked(component) if component == button_black =>
	    if (playerchips.black > 0) { 
	      playerchips.subtract(Array(0,0,1))
	      playerChipCanvas.update(playerchips)
	      playerChipCanvas.repaint()
	      
	      playerchips.betAmount(2) += 1; playerArea.text_=("Bet: $" + playerchips.betvalue)
	      bettedChips.add(Array(0,0,1))
	      bettedChipsCanvas.update(bettedChips)
	      bettedChipsCanvas.repaint()
	    }
	    else { button_black.enabled_=(false) }
	    
	    
	  case ButtonClicked(component) if component == button_betfinish =>
	    
	    button_betfinish.visible_=(false)
	    button_hit.enabled_=(true)
	    button_pass.enabled_=(true)
	    bets_layout.visible_=(false)
	    
	    
      case ButtonClicked(component) if component == button_hit =>
      	
	    playerDeck.dealt(gameDeck.deal(1))
	  	playerCanvas.update(playerDeck)
	  	gameCanvas.update(gameDeck)
	  	playerCanvas.repaint
	  	gameCanvas.repaint
	  	
	  	//check for blackJack
	  	val yourscore = playerDeck.score
	  	val dealerscore = dealerDeck.score
	  	if (yourscore == 21) {
	  	  messageArea.append("You win with BlackJack!\n")
	  	  button_hit.enabled_=(false)
	  	  button_pass.enabled_=(false)
	  	  button_newgame.visible_=(true)
	  	  
	  	  playerchips.add(playerchips.betAmount) //add original amount back
	  	  playerchips.add(playerchips.betAmount) // add winnings
	  	  playerChipCanvas.update(playerchips)
	  	  playerChipCanvas.repaint()
	  	  dealerCanvas.topcard_flip = false
	  	  dealerCanvas.isFaceUp = true
	  	  dealerCanvas.update(dealerDeck)
	  	  dealerCanvas.repaint
	  	
	  	} else if (yourscore > 21) { 
	  	  messageArea.append("You busted. Sorry you lose\n")
	  	  messageArea.append("Your Score: " + yourscore + "\n")
	  	  messageArea.append("Dealer current Score: " + dealerscore + "\n")
	  	  button_hit.enabled_=(false)
	  	  button_pass.enabled_=(false)
	  	  button_newgame.visible_=(true)
	  	  
	  	  dealerCanvas.topcard_flip = false
	  	  dealerCanvas.isFaceUp = true
	  	  dealerCanvas.update(dealerDeck)
	  	  dealerCanvas.repaint
	  	  
	  	}
        
     case ButtonClicked(component) if component == button_pass =>
       
       	button_hit.enabled_=(false)
	  	button_pass.enabled_=(false)
	  	button_newgame.visible_=(true)
       
        var dealerscore = 0
        var playerscore = 0
        var errorCounter = 0
     	do {
			errorCounter += 1
     		//check for blackJack or a score higher than player
			playerscore = playerDeck.score
			dealerscore = dealerDeck.score
			
			if (dealerscore > 21) {
			    messageArea.append("The dealer busted. You win!\n")
			    playerchips.add(playerchips.betAmount) //add original amount back
			    playerchips.add(playerchips.betAmount) // add winnings
			    playerChipCanvas.update(playerchips)
			    playerChipCanvas.repaint()
			}
			else if (dealerscore == 21 || dealerscore>playerscore) {
			    messageArea.append("The dealer beat your score. Sorry you lose!\n")
			} else {
			    dealerDeck.dealt(gameDeck.deal(1))
			  	dealerCanvas.update(dealerDeck)
			  	gameCanvas.update(gameDeck)
			  	playerCanvas.repaint
			  	gameCanvas.repaint
			} 
			
     	} while (dealerscore<=playerscore && dealerscore<21)
     	  
     	messageArea.append("Player final score: " + playerscore + "\nDealer final Score: " + dealerscore + "\n")
        dealerCanvas.topcard_flip = false
        dealerCanvas.isFaceUp = true
	  	dealerCanvas.update(dealerDeck)
	  	dealerCanvas.repaint
        
    }
  }
}