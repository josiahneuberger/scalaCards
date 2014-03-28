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

object ChipCanvas extends scala.swing.Panel { 
  
   def getInstance(w: Int, h: Int, d: StringChips, xoffset: Int, yoffset: Int) : ChipCanvas = {
     
     var panel = new ChipCanvas
     panel.opaque_=(false)
	 panel.chips = d
	 panel.xoffset = xoffset
	 panel.yoffset = yoffset
	 panel.height = h
	 panel.width = w
	 panel.chipImage = panel.createImage
	 panel.preferredSize_=(new java.awt.Dimension(w,h))
	 return panel
  }
}
 
class ChipCanvas extends scala.swing.Panel {
  
  var width = 100
  var height = 120
  var xoffset = 50.0
  var yoffset = 12.0
  var chips: StringChips = new StringChips
  
  private var chipImage:BufferedImage = null
  
  def update(d: StringChips) {
	  chips = d
	  chipImage = createImage
  }
 
  def createImage() : BufferedImage = {
	var y=0.0
	var x = 0.0
	var x_row1=0.0
	var x_row2=0.0
	var x_row3=0.0
	var chipDiameter = 70.0
	var image = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR)
	var gr = image.createGraphics()

	println(chips.deck.toString)
    for (i <- 0 to 2)
    {   

    	var value = chips.number(i)
    	var numberofchips = chips.deck(i)
    	font = new Font("Bodoni MT Black", Font.BOLD, 30)
	    gr.setFont(font)
	    
	    var color = Color.black
	    var strvalue = ""
	    x_row1 = 0.0
	    x_row2 = 40.0
	    x_row3 = 90.0
	    
	    if (i==0) {
	    	color = Color.red
	    	strvalue = "  5"
	    	y = 0
	    	x_row3 += chipDiameter*(xoffset/100)
	    	x = x_row3
	    }
	    else if (i==1) {
	    	color = Color.getHSBColor(0.3f, .8f, .3f)
	    	strvalue = " 25"
	    	y = chipDiameter
	    	x_row2 += chipDiameter*(xoffset/100)
	    	x = x_row2
	    }
	    else if (i==2) {
	    	color = Color.black
	    	strvalue = "100"
	    	y = chipDiameter*2
	    	x_row1 += chipDiameter*(xoffset/100)
	    	x = x_row1
	    }
	   
    	for (i <- 1 to numberofchips) {
		    
		    var circleBorder = new Ellipse2D.Double(x, y, 100, 100)
		    var outerCircle = new Ellipse2D.Double(x, y, 103, 103)
		    var innerCircle = new Ellipse2D.Double(x+22, y+22, 60, 60)

		    
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
		    gr.setColor(Color.white)
		    gr.fill(innerCircle)
		    gr.setColor(Color.black)
		    gr.drawString(strvalue,x.toInt+28,y.toInt+64)   
		    
		    x += chipDiameter*(xoffset/100)
		    

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