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


object SimpleGUI extends SimpleSwingApplication {

  def top = new MainFrame { 
   title = "BlackJack"
   var gameFont = new Font("Bodoni MT Black", Font.BOLD, 30)
   var messageFont = new Font("Bodoni MT Black", Font.BOLD, 15)
    
   val button_hit = new Button {
		text = "Hit!"
		font_=(gameFont)
		borderPainted = true
		enabled = true
		tooltip = "Click to get another card from the dealer."
	}
	
	val button_pass = new Button {
		text = "Pass!"
		font_=(gameFont)
		borderPainted = true
		enabled = true
		tooltip = "Click to pass to Dealer."
	}
	
	val button_newgame = new Button {
		text = "Start New Game!"
		font_=(gameFont)
		borderPainted = true
		enabled = true
		visible = false
		tooltip = "Click to start a new game!"
	}
    
    var border2 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), 
	BorderFactory.createEtchedBorder(EtchedBorder.LOWERED))	

    
	var gameDeck = new StringDeck
    gameDeck.shuffle
    var playerDeck = new StringDeck(gameDeck.deal(2))
    var dealerDeck = new StringDeck(gameDeck.deal(2))
    var playerCanvas = CardCanvas.getInstance(600,200, playerDeck, false, false, true, 20,0)
    var dealerCanvas = CardCanvas.getInstance(400,300, dealerDeck, false, true, true, 20,0)
    var gameCanvas = CardCanvas.getInstance(600, 400, gameDeck, false, false, false, 20, 2)
    
    
    var player1_space = new FlowPanel {

      this.opaque = false
      contents += playerCanvas
      
      contents += button_hit
      contents += button_pass
      contents += button_newgame
      
    }
    
    var dealer_space = new BorderPanel {

      this.opaque = false
      this.border = border2
       
      layout(gameCanvas) = North
      layout(dealerCanvas) = South
    }
    
    val messageArea = new TextArea {
      text = "********************************************\nHello!\nLet's start the game already!!\n"
      background = Color.white
      font_=(messageFont)
    }
    
    
      
    var panelFelt = new BorderPanel {
      preferredSize = new Dimension(1500, 800)
      
      layout(dealer_space) = Center
      layout(player1_space) = South
      layout(messageArea) = West
     
      
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
    //listenTo(canvas.mouse.clicks)*/

    reactions += {
      case ButtonClicked(component) if component == button_newgame =>
       
       	gameDeck = new StringDeck
	    gameDeck.shuffle
	    
	    playerDeck = new StringDeck(gameDeck.deal(2))
	    dealerDeck = new StringDeck(gameDeck.deal(2))
       	
       	dealerCanvas.bottomcard_flip = true
	  	dealerCanvas.isFaceUp = false
	    playerCanvas.update(playerDeck)
	    dealerCanvas.update(dealerDeck) 
	    gameCanvas.update(gameDeck)
	    dealerCanvas.repaint
	    gameCanvas.repaint
	    
	    
	    button_pass.enabled_=(true)
	    button_pass.enabled_=(true)
       	button_hit.enabled_=(true)
	    button_newgame.visible_=(false)
	    messageArea.text_=("********************************************\nLet's start the next game already!!\n")
	    playerCanvas.repaint
	  
	    
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
	  	  
	  	  dealerCanvas.bottomcard_flip = false
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
	  	  
	  	  dealerCanvas.bottomcard_flip = false
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
			
     	} while (dealerscore<playerscore && dealerscore<21)
     	  
     	messageArea.append("Player final score: " + playerscore + "\nDealer final Score: " + dealerscore + "\n")
        dealerCanvas.bottomcard_flip = false
        dealerCanvas.isFaceUp = true
	  	dealerCanvas.update(dealerDeck)
	  	dealerCanvas.repaint

     /* case MouseClicked(_, point, _, _, _) =>
        canvas.throwDart(new Dart(point.x, point.y, Color.black))
        textField.text = (s"You clicked in the Canvas at x=${point.x}, y=${point.y}.") */
    }
  }
}