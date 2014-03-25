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


object SimpleGUI extends SimpleSwingApplication {

  def top = new MainFrame { // tp is a required method
    title = "A Sample Scala Swing GUI"

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
    
      // create graphics panel and add it to the frame
    val dealerCanvas1 = CardCanvas.getInstance(200,200,Array("S2", "HT"))
    val dealerCanvas2 = CardCanvas.getInstance(200,200,Array("SA", "CK"))
    val FlowPanel2 = new FlowPanel {

      contents += CardCanvas.getInstance(200,200,Array("H8", "D3"))
      contents += CardCanvas.getInstance(200,200,Array("CQ", "CK"))
    }

    // choose a top-level Panel and put components in it
    // Components may include other Panels*/
    
     val canvas = new Canvas {
      preferredSize = new Dimension(100, 100)
    }
    
    val button = new Button {
      text = "Throw!"
      foreground = Color.blue
      background = Color.red
      borderPainted = true
      enabled = true
      tooltip = "Click to throw a dart"
    }
      
    val panelFelt = new BorderPanel {
      preferredSize = new Dimension(1500, 800)
      
      layout(canvas) = South
      layout(button) = North
      
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
    /*listenTo(button)
    listenTo(toggle)
    listenTo(canvas.mouse.clicks)

    // react to events
    reactions += {
      case ButtonClicked(component) if component == button =>
        val x = Random.nextInt(100)
        val y = Random.nextInt(100)
        val c = new Color(Random.nextInt(Int.MaxValue))
        canvas.throwDart(new Dart(x, y, c))
        textField.text = s"Dart thrown at $x, $y"
      case ButtonClicked(component) if component == toggle =>
        toggle.text = if (toggle.selected) "On" else "Off"
      case MouseClicked(_, point, _, _, _) =>
        canvas.throwDart(new Dart(point.x, point.y, Color.black))
        textField.text = (s"You clicked in the Canvas at x=${point.x}, y=${point.y}.") 
    }*/
  }
}