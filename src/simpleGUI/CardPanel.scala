package simpleGUI

/******************************************************************************
* File : CardPanel.java (version 2)
* Author : http://java.macteki.com/
* Description :
*   Display a shuffled deck of playing card.
* Tested with : JDK 1.6 under Windows XP and MAC osx 10.6.6
******************************************************************************/
 
import java.awt.image.BufferedImage
import java.awt.Font
import javax.swing.JPanel
import java.awt.Graphics2D

object CardCanvas extends Canvas { 
  
   def getInstance(width: Int, height: Int, d: Array[String]) : CardCanvas = {
     
     
	 var panel = new CardCanvas
	 panel.deck = d
	 panel.cardImage = panel.createAllImage
	 panel.background_=(new scala.swing.Color(255,255,128))
	 panel.preferredSize_=(new java.awt.Dimension(200,200))
	 return panel
  }
}
 
class CardCanvas extends Canvas {
  
  //private var cardImage = createImage
  private var deck: Array[String] = Array()
  
  /*val deck = Array(
    "SA","S2","S3","S4","S5","S6","S7","S8","S9","ST","SJ","SQ","SK", // spade
    "HA","H2","H3","H4","H5","H6","H7","H8","H9","HT","HJ","HQ","HK", // heart
    "CA","C2","C3","C4","C5","C6","C7","C8","C9","CT","CJ","CQ","CK", // club
    "DA","D2","D3","D4","D5","D6","D7","D8","D9","DT","DJ","DQ","DK"  // diamond
  )*/
  
  private var cardImage = Array[BufferedImage] ()
 
  def createAllImage() : Array[BufferedImage] = {
   
    var imageArray = new Array[BufferedImage] (deck.size)
    
    for (i <- 0 until deck.size ) {
      
      imageArray(i) = createImage(deck(i))
    }
    return imageArray;
  }
 
  def createImage(card: String) : BufferedImage = {
    
    // create a card image.
    val cardWidth = 60
    val cardHeight = 80
    var image = new BufferedImage(cardWidth, cardHeight, BufferedImage.TYPE_INT_ARGB)
 
    // get a graphics object of the image for drawing.
    var gr = image.getGraphics()
 
    // draw a white playing card
    gr.setColor(java.awt.Color.WHITE)
    gr.fillRect(0,0,cardWidth,cardHeight)
    // with black border
    gr.setColor(java.awt.Color.BLACK)
    gr.drawRect(0,0,cardWidth-1,cardHeight-1)
    
    // draw the "three of Spade"
    font = new Font("Dialog",Font.PLAIN, 28)
    gr.setFont(font)
    
    val prefix = card.substring(0,1)  // first character
    val postfix = card.substring(1,2) // second character
    var suit=""
    var color = java.awt.Color.BLACK
    
    if (prefix.equals("S")) {
      
      suit = "\u2660"   // unicode for the "spade" character
    }
    else if (prefix.equals("H")) {
      
      suit = "\u2665"   // unicode for the "heart" character
      color = java.awt.Color.RED
    }
    else if (prefix.equals("C")) {
      
      suit = "\u2663"
    }
    else if (prefix.equals("D")) {
     
      suit = "\u2666"   // unicode for the "diamond" character
      color = java.awt.Color.RED
    }
     
    var point = postfix
    var x = 5
    
    if (postfix.equals("T")) {
      
      x = 1
      point = "10"  // special handling for "ten"
    }
  
    gr.setColor(color);
    gr.drawString(suit+point,x,45);
 
    return image   
  }
 
  // override the paint method
  override def paintComponent(gr: Graphics2D) {
    
    super.paintComponent(gr)  // paint background

 
    var y=0
    var x=0
    for (i <- 0 until cardImage.size)
    {     
	  gr.drawImage(cardImage(i),null,x,y) // draw a card
	  x+=61
	  if ((i+1)%13==0) {
	    y+=81 
	    x=0
	  }
    }
  }
}