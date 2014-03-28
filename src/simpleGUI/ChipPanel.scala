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
import java.awt.geom.Ellipse2D

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
object ChipCanvas extends scala.swing.Panel { 
	
	val TYPE_STACKED_VERTICAL_RIGHT = 0
	val TYPE_STACKED_VERTICAL_RIGHT_INLINEX = 1
  
	def getInstance(w: Int, h: Int, d: StringChips, alignmentType: Int) : ChipCanvas = {
	
	  var panel = new ChipCanvas
	  if (alignmentType == TYPE_STACKED_VERTICAL_RIGHT) {
	     
	     panel.opaque_=(false)
		 panel.chips = d
		 panel.height = h
		 panel.width = w
	     panel.xstart = 0
	     panel.ystart = 0
		 panel.chipImage = panel.createImage
		 panel.preferredSize_=(new java.awt.Dimension(w,h))
		 
	  } else if (alignmentType == TYPE_STACKED_VERTICAL_RIGHT_INLINEX) {
	     panel.opaque_=(false)
		 panel.chips = d
		 panel.height = h
		 panel.width = w
	     panel.xstart = -70
	     panel.ystart = 0
		 panel.chipImage = panel.createImage
		 panel.preferredSize_=(new java.awt.Dimension(w,h))
	}
	return panel
  }
}
 
class ChipCanvas extends scala.swing.Panel {
  
	var translate = true
	var transl = 1
	if (translate) transl = -1
	var width = 400
	var height = 300
	var chipDiameter = 70.0
	var xoffset = 6.0
	var yoffset = -13.0
	var ystart = width/2
	var xstart = 0.0	
	var x_row1:Double = xstart + (3*transl) //180
	var x_row2:Double = xstart + (40*transl) //120
	var x_row3:Double = xstart + (10*transl) /.0
	var y_row1:Double = ystart + (3*transl)
	var y_row2:Double = ystart + (40*transl)
	var y_row3:Double = ystart + (10*transl)
	var chips: StringChips = new StringChips
  
	private var chipImage:BufferedImage = null
	  
	def update(d: StringChips) {
		chips = d
		chipImage = createImage
	}
	 
	def createImage() : BufferedImage = {
	    var x = 0.0
	    var y = 0.0
	
		var image = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR)
		var gr = image.createGraphics()
		var colors = Array(Color.red, Color.getHSBColor(0.3f, .8f, .3f), Color.black)
	
		
	    for (i <- 0 to 2)
	    {   
 
	    	var value = chips.number(i)
	    	var numberofchips = chips.deck(i)
	    	font = new Font("Bodoni MT Black", Font.BOLD, 30)
		    gr.setFont(font)
		    
		    var color = Color.black
		    var strvalue = ""
		    
		    x_row1 = xstart
		    x_row2 = xstart
		    x_row3 = xstart
		    y_row1 = ystart
		    y_row2 = ystart
		    y_row3 = ystart
	    
		    if (i==0) {
		    	color = Color.blue
		    	strvalue = "  5"
		    	y_row3 += chipDiameter*(3+yoffset/100)
		    	x_row3 += chipDiameter*(4+xoffset/100)
		    	y = y_row3
		    	x = x_row3
		    }
		    else if (i==1) {
		    	color = Color.black
		    	strvalue = " 25"
		    	y_row2 += chipDiameter*(2+yoffset/100)
		    	x_row2 += chipDiameter*(1+xoffset/100)
		    	y = y_row2
		    	x = x_row2
		    }
		    else if (i==2) {
		    	color = Color.white
		    	strvalue = "100"
		    	y_row1 += chipDiameter*(3+(yoffset/100))
		    	x_row1 += chipDiameter*(2.5+xoffset/100)
		    	y = y_row1
		    	x = x_row1
		    }
		   
	    	for (w <- 1 to numberofchips) {
    		
			    var circleBorder = new Ellipse2D.Double(x, y, 100, 100)
			    var outerCircle = new Ellipse2D.Double(x, y, 103, 103)
			    var innerCircle = new Ellipse2D.Double(x+22, y+22, 60, 60)
			    var innerBorder = new Ellipse2D.Double(x+22, y+22, 61, 61)
	
			    
			    gr.setColor(Color.black)
			    gr.draw(circleBorder)
			    gr.setColor(Color.black)
			    gr.draw(outerCircle)
			    gr.setColor(Color.white)
			    gr.draw(innerCircle)
			    
			    gr.setColor(Color.lightGray)
			    gr.fill(circleBorder)
			    gr.setColor(color)
			    gr.fill(outerCircle)
			    
			    var a = x+52 //centerx
			    var b = y+51 //centery
			    for (l <- 0 to 360 )  {
			    	var xp = a + 50.5*Math.cos(l*Math.PI/180)
			    	var yp = b + 48*math.sin(l*Math.PI/180)
				    gr.setPaint(colors(i))
				    gr.drawLine(a.toInt, b.toInt, xp.toInt, yp.toInt)
			    }
			    
			    gr.setColor(Color.white)
			    gr.fill(innerCircle)
			    gr.setColor(Color.black)
			    gr.draw(innerBorder)
			    gr.drawString(strvalue,x.toInt+28,y.toInt+64)
			    
			    x += chipDiameter*(xoffset/100)
			    y += chipDiameter*(yoffset/100)
	    	}
	    	
	    }
    return image   
  }	
 
  // override the paint method
  override def paintComponent(gr: Graphics2D) {
    super.paintComponent(gr)  // paint background

    gr.drawImage(chipImage, null, 0, 0)
    
  }
}