package simpleGUI
 
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
import java.awt.Color

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
object CardCanvas extends scala.swing.Panel { 
  
	//Draws a deck of cards using translations from the parameters.
	def getInstance(width: Int, height: Int, d: StringDeck, topcard_flip:Boolean, bottomcard_flip:Boolean, isFaceUp:Boolean, xoffset: Int, yoffset: Int) : CardCanvas = {
     
     var panel = new CardCanvas
     panel.opaque_=(false)
      
	 panel.deck = d
	 panel.xoffset = xoffset
	 panel.yoffset = yoffset
	 panel.topcard_flip = topcard_flip
	 panel.bottomcard_flip = bottomcard_flip
	 panel.isFaceUp = isFaceUp
	 if (panel.deck != null) panel.cardImage = panel.createAllImages
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
	  if (deck != null) cardImage = createAllImages
	  else cardImage = Array[BufferedImage] ()
  }
 
  //Creates all the images for the deck of cards in this instance.
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
  
  //Creates a back image of the card.
  def createBack() : BufferedImage = {
    
    //http://subtlepatterns.com/diagmonds/
    var cardback_imgFile = new File("images/card_back.png")
    var image2 = ImageIO.read(cardback_imgFile).getSubimage(0, 0, cardWidth.toInt, cardHeight.toInt)
    var image = new BufferedImage(image2.getWidth, image2.getHeight, BufferedImage.TYPE_4BYTE_ABGR)

    var gr = image.createGraphics()
    gr.drawRenderedImage(image2, null)
    
    // Draw Border
    gr.setColor(Color.white)
    gr.drawRect(0,0,cardWidth.toInt-1,cardHeight.toInt-1)
    
    return image
  }
 
  //creates a face card image based on the string
  def createImage(card: String) : BufferedImage = {
    
    //http://subtlepatterns.com/fresh-snow/
    var cardface_imgFile = new File("images/card_face.png")
    var image2 = ImageIO.read(cardface_imgFile).getSubimage(0, 0, cardWidth.toInt, cardHeight.toInt)
    var image = new BufferedImage(image2.getWidth, image2.getHeight, BufferedImage.TYPE_4BYTE_ABGR)
    

    var gr = image.createGraphics()
    gr.drawRenderedImage(image2, null)
    
    // Draw Border
    gr.setColor(Color.black)
    gr.drawRect(0,0,cardWidth.toInt-1,cardHeight.toInt-1)
    
    font = new Font("Dialog",Font.PLAIN, 20)
    gr.setFont(font)
    
    val prefix = card.substring(0,1)  // first character
    val postfix = card.substring(1,2) // second character
    var suit=""
    var color = Color.black
    
    if (prefix.equals("S")) {
      
      suit = "\u2660"   // unicode for the "spade" character
    }
    else if (prefix.equals("H")) {
      
      suit = "\u2665"   // unicode for the "heart" character
      color = Color.red
    }
    else if (prefix.equals("C")) {
      
      suit = "\u2663"
    }
    else if (prefix.equals("D")) {
     
      suit = "\u2666"   // unicode for the "diamond" character
      color = Color.red
    }
     
    var point = postfix
    
    gr.setColor(color)
    gr.drawString(point,3,20)
    gr.drawString(suit,1,38)
    gr.setFont(new Font("Dialog", Font.BOLD, 100))
    gr.drawString(suit,30,70)
    gr.drawString(point,0,140)
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
	   }
	  if (yoffset != 0) { y += cardHeight*(yoffset/100) }
    }
    
  }
}