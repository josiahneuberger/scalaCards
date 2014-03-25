package simpleGUI

import scala.swing.Panel
import java.awt.{ Graphics2D, Color }
import java.awt.Font
import java.awt.GradientPaint
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.awt.Rectangle
import java.awt.TexturePaint
import java.awt.RenderingHints
import javax.imageio.ImageIO
import java.io.File

class Canvas extends Panel {
  var centerColor = Color.yellow
  
  var darts = List[Dart]()
  
  this.opaque = false

  override def paintComponent(g: Graphics2D) {
    
    // Start by erasing this Canvas
    //g.clearRect(0, 0, size.width, size.height)
    
    var greentoblack = new GradientPaint(0,0, Color.getHSBColor(0.3f, .8f, .5f), size.width, size.height, Color.BLACK)
    var greentowhite = new GradientPaint(0,0, Color.getHSBColor(0.3f, .8f, .5f), size.width, size.height, Color.WHITE)
    var blacktogreen = new GradientPaint(0,0, Color.BLACK, size.width, size.height, Color.getHSBColor(0.3f, .8f, .5f))
    var whitetogreen = new GradientPaint(0,0, Color.WHITE, size.width, size.height, Color.getHSBColor(0.3f, .8f, .5f))
    var blacktowhite = new GradientPaint(0,0, Color.BLACK, size.width, size.height, Color.WHITE)
    var colors = Array(greentoblack, greentowhite)
    
    //http://subtlepatterns.com/patterns/pool_table.zip
    var icon = new javax.swing.ImageIcon("images/felt.png")
    var imgFile = new File("images/felt.png")
    var buff_img2 = ImageIO.read(imgFile)
    
    //g.setPaint(blacktogreen)
    //g.fillRect(0,0, size.width, size.height)
    //g.fill(new Rectangle2D.Double(0,0, size.width, size.height))
    
    //http://www.c-sharpcorner.com/UploadFile/433c33/working-with-texturepaint-in-java/
    var width = 30
    var height = 30
    var buff_img = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB)
    var g2 = buff_img.createGraphics()
    
    //http://java.macteki.com/2011/03/how-to-display-deck-of-playing-card-in.html
    for (i <- 0 to height) {
	    g2.setPaint(colors((i/2)%colors.length))
	    g2.drawLine(0, i, width-1, height-1)
	    g2.setPaint(colors((i/2)%colors.length))
	    g2.drawLine(i, 0, width-1, height-1)
	    g2.setPaint(colors((i/2)%colors.length))
	    g2.drawLine(width-1, height-1, 0, i)
	    g2.setPaint(colors((i/2)%colors.length))
	    g2.drawLine(width-1, height-1, i, 0)
	    //g2.setPaint(colors((i/4)%colors.length))
	    //g2.drawLine(width - i, height, width, height - i)
	    //g2.setPaint(colors((i/4)%colors.length))
	    //g2.drawLine(width - i, height, width, height - i)

    }

	var r = new Rectangle(0, 0, buff_img.getWidth(), buff_img.getHeight())
	
	//var tp = new TexturePaint(buff_img, r)
	var tp = new TexturePaint(buff_img2,r)
	
	//var mesg = "ABHISHEK "
	//var myFont = new Font("Bodoni MT Black", Font.BOLD, 72)
	
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
	
	g.setPaint(tp)
	//g.setFont(myFont)
	//g.drawString(mesg, 10, 100)
	
	//g.fill(new Rectangle2D.Double(0,0, size.width, size.height))
    
      /*// fill RoundRectangle2D.Double
        GradientPaint redtowhite = new GradientPaint(x,y,red,x+rectWidth, y,white);
        g2.setPaint(redtowhite);
        g2.fill(new RoundRectangle2D.Double(x, y, rectWidth, 
                                            rectHeight, 10, 10));*/
    
    // Draw background here
    /*g.setColor(Color.blue)
    g.fillOval(0, 0, 100, 100)
    g.setColor(Color.red)
    g.fillOval(20, 20, 60, 60)
    g.setColor(centerColor)
    g.fillOval(40, 40, 20, 20)*/
    
    g.setColor(Color.WHITE)
    //g.fillRoundRect(0, 0, 150, 250, 20, 20)
    g.setColor(Color.BLACK)
    //g.fillRoundRect(0, 0, 150, 250, 20, 20)
    
    // draw a white playing card
    //var cardWidth=60
    //var cardHeight=80;
    //g.setColor(java.awt.Color.WHITE);
    //g.fillRect(0,0,cardWidth,cardHeight);
    //g.setColor(java.awt.Color.BLACK);  // with black border
    //g.drawRect(0,0,cardWidth-1,cardHeight-1);
 
    //http://java.macteki.com/2011/03/how-to-display-deck-of-playing-card-in.html
    // draw the "three of Spade"
    
    var font=new Font("Dialog",Font.PLAIN, 28);
    g.setFont(font);
    var spade="\u2660";   // unicode for the "spade" character
    var heart="\u2764";
    var club="\u2663"
    var diamond="\u2666"
    //g.drawString(spade+"3",5,45);
    
    //http://www.fileformat.info/info/unicode/char/2764/index.htm
    g.setColor(Color.RED)
    g.drawString(heart+"2",10,45);
    
    g.setColor(Color.BLACK)
    g.drawString(club+10,15,60);
    
    g.setColor(Color.RED)
    g.drawString(diamond+"J",15,60);
    
    // Draw things that change on top of background
    for (dart <- darts) {
      g.setColor(dart.color)
      g.fillOval(dart.x, dart.y, 10, 10)
    }
  }

  /** Add a "dart" to list of things to display */
  def throwDart(dart: Dart) {
    darts = darts :+ dart
    // Tell Scala that the display should be repainted
    repaint()
  }
}