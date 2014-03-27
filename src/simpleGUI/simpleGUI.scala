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

  def top = new MainFrame { // tp is a required method
    title = "BlackJack"
    var gameFont = new Font("Bodoni MT Black", Font.BOLD, 30)
    var messageFont = new Font("Bodoni MT Black", Font.BOLD, 15)
   /* // declare Components here
    val label = new Label {
      text = "I'm a big label!."
      font = new Font("Ariel", java.awt.Font.ITALIC, 24)
    }
    val button = new Button {
      text = "Throw!"
      foreground = Color.blue
      background = Color.red
      borderPainted = true
      enabled = true
      tooltip = "Click to throw a dart"
    }
    val toggle = new ToggleButton { text = "Toggle" }
    val checkBox = new CheckBox { text = "Check me" }
    val textField = new TextField {
      columns = 10
      text = "Click on the target!"	
    }
    val textArea = new TextArea {
      text = "initial text\nline two"
      background = Color.green
    }
    val canvas = new Canvas {
      preferredSize = new Dimension(100, 100)
    }
     
    val gridPanel = new GridPanel(1, 2) {
      contents += checkBox
      contents += label
      contents += textArea
    }

    // choose a top-level Panel and put components in it
    // Components may include other Panels*/
    
      
     val canvas = new Canvas {
      preferredSize = new Dimension(100, 100)
    }
    
    var border2 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), 
	BorderFactory.createEtchedBorder(EtchedBorder.LOWERED))				
    
	var gameDeck = new StringDeck
    //gameDeck.shuffle
    var playerDeck = new StringDeck(gameDeck.deal(2))
    var dealerDeck = new StringDeck(gameDeck.deal(2))
    var playerCanvas =  CardCanvas.getInstance(1000,200, playerDeck, false, false, true, 20,0)
    
    
    var player1_space = new FlowPanel {

      this.opaque = false
      contents += playerCanvas
      
    }
    
    
	val button_hit = new Button {
		text = "Hit!"
		font_=(gameFont)
		//foreground = Color.blue
		//background = Color.red
		//this.preferredSize_=(new Dimension(80,30))
		borderPainted = true
		enabled = true
		tooltip = "Click to get another card from the dealer."
	}
    
    var dealer_space = new FlowPanel {

      this.opaque = false
      this.border = border2
       
      contents += CardCanvas.getInstance(400, 200, gameDeck, false, false, false, 5, 2)
      contents += CardCanvas.getInstance(600,200, dealerDeck, true, false, false, 20,0)
      
     
        
      contents += button_hit
    }
    
     val messageArea = new TextArea {
      text = "Hello! Let's start a game\n"
      background = Color.gray
      font_=(messageFont)
    }
    
    
      
    var panelFelt = new BorderPanel {
      preferredSize = new Dimension(1500, 800)
      
      layout(dealer_space) = North
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

    // specify which Components produce events of interest
    listenTo(button_hit)
    /*listenTo(toggle)
    listenTo(canvas.mouse.clicks)*/

    // react to events
    reactions += {
      case ButtonClicked(component) if component == button_hit =>
      	
        val prescore = playerDeck.score
	    playerDeck.dealt(gameDeck.deal(1))
	  	playerCanvas.update(playerDeck)
	  	playerCanvas.repaint
	  	
	  	//check for blackJack
	  	val yourscore = playerDeck.score
	  	if (yourscore == 21) {
	  	  messageArea.append("You win\n")
	  	  messageArea.append("(" + prescore + ", " + yourscore + ")")
	  	
	  	} else if (yourscore > 21) { 
	  	  messageArea.append("You lose\n")
	  	  messageArea.append("(" + prescore + ", " + yourscore + ")")
	  	}
	  	else {
	  	  messageArea.append("You can Hit or Pass to the dealer\n")
	  	  messageArea.append("(" + prescore + ", " + yourscore + ")")
	  	}
      	
      /*case ButtonClicked(component) if component == toggle =>
        toggle.text = if (toggle.selected) "On" else "Off"
      case MouseClicked(_, point, _, _, _) =>
        canvas.throwDart(new Dart(point.x, point.y, Color.black))
        textField.text = (s"You clicked in the Canvas at x=${point.x}, y=${point.y}.") */
    }
  }
}