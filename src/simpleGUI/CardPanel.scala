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
import java.io.File
import javax.imageio.ImageIO
import java.awt.Rectangle
import java.awt.TexturePaint
import java.awt.geom.Rectangle2D
import javax.swing.ImageIcon
import scala.swing.BorderPanel
import java.awt.Dimension
import scala.swing.Panel

object CardCanvas extends scala.swing.Panel { 
  
   def getInstance(width: Int, height: Int, d: StringDeck, topcard_flip:Boolean, bottomcard_flip:Boolean, isFaceUp:Boolean, xoffset: Int, yoffset: Int) : CardCanvas = {
     
     var panel = new CardCanvas
     panel.opaque_=(false)
	 panel.deck = d
	 panel.xoffset = xoffset
	 panel.yoffset = yoffset
	 panel.topcard_flip = topcard_flip
	 panel.bottomcard_flip = bottomcard_flip
	 panel.isFaceUp = isFaceUp
	 panel.cardImage = panel.createAllImages
	 panel.preferredSize_=(new java.awt.Dimension(width,height))
	 return panel
  }
}
 
class CardCanvas extends scala.swing.Panel {
  
  var cardWidth = 100.0
  var cardHeight = 120.0
  var xoffset = 50.0
  var yoffset = 12.0
  var deck: StringDeck = new StringDeck
  var topcard_flip = true
  var bottomcard_flip = true
  var isFaceUp = true
  
  private var cardImage = Array[BufferedImage] ()
  private var backImage = createBack
  
  def update(d: StringDeck) {
	  deck = d
	  cardImage = createAllImages
  }
 
  def createAllImages() : Array[BufferedImage] = {
   
    var cardFaceArray = new Array[BufferedImage] (deck.size)
    
    if (bottomcard_flip && isFaceUp) { cardFaceArray(0) = backImage }
    if (topcard_flip && isFaceUp) { cardFaceArray(deck.size-1) = backImage }
    
    var i = -1
    for (card <- deck.getList) {
      i += 1
      if (i==0 && bottomcard_flip) {
        if (isFaceUp) { cardFaceArray(i) = backImage }
        else { cardFaceArray(i) = createImage(card) }
      } else if (i==deck.size-1 && topcard_flip) {
        if (isFaceUp) { cardFaceArray(i) = backImage }
        else { cardFaceArray(i) = createImage(card) }
      } else {
        if (isFaceUp) { cardFaceArray(i) = createImage(card) }
        else { cardFaceArray(i) = backImage }
      } 
    }
    return cardFaceArray;
  }
  
  def createBack() : BufferedImage = {
    
    //http://subtlepatterns.com/diagmonds/
    var cardback_imgFile = new File("images/card_back.png")
    var image = ImageIO.read(cardback_imgFile).getSubimage(0, 0, cardWidth.toInt, cardHeight.toInt)

    var gr = image.createGraphics()
    
    // Draw Border
    gr.setColor(java.awt.Color.RED)
    gr.drawRect(0,0,cardWidth.toInt-1,cardHeight.toInt-1)
    
    return image
  }
 
  def createImage(card: String) : BufferedImage = {
    
    //http://subtlepatterns.com/fresh-snow/
    var cardface_imgFile = new File("images/card_face.png")
    //http://subtlepatterns.com/swirl/
    var cardback_imgFile = new File("images/card_back.png")
    var image = ImageIO.read(cardface_imgFile).getSubimage(0, 0, cardWidth.toInt, cardHeight.toInt)

    var gr = image.createGraphics()
    
    // Draw Border
    gr.setColor(java.awt.Color.RED)
    gr.drawRect(0,0,cardWidth.toInt-1,cardHeight.toInt-1)
    
    // draw the "three of Spade"
    font = new Font("Dialog",Font.PLAIN, 20)
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
    
    /*if (postfix.equals("T")) {
      
      x = 1
      point = "10"  // special handling for "ten"
    }*/
  
    gr.setColor(color)
    gr.drawString(point,3,20)
    gr.drawString(suit,1,38)
    return image   
  }
 
  // override the paint method
  override def paintComponent(gr: Graphics2D) {
    super.paintComponent(gr)  // paint background
    
    var y=0.0
    var x=0.0
    
    
    for (i <- 0 until cardImage.size)
    {     
      gr.drawImage(cardImage(i),null,x.toInt,y.toInt)
      
	  if (xoffset != 0) { 
	    x += cardWidth*(xoffset/100)
	    val yy = 3
	   }
	  if (yoffset != 0) { y += cardHeight*(yoffset/100) }
    }
    
  }
}