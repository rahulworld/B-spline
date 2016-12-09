package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class UIPane
	extends JRootPane
{
	private static final long serialVersionUID = 1L;
	
	public JButton resetButton;
	public PaintPanel canvas;
	public JCheckBox drawControlPolygon;
	public JCheckBox drawCurvePoints;
	public JCheckBox renderAdaptive;
	public JCheckBox adaptiveBernstein;
	public JCheckBox addPoints;
	public JCheckBox insertPoints; 
	public JCheckBox movePoints;
	public JSlider slider;
	

	public UIPane() {
	    
		this.drawControlPolygon = new JCheckBox("Draw Control Polygon", true);
		this.drawControlPolygon.setFocusable(false);
		this.drawControlPolygon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIPane.this.stateChanged();
			}
		});
		
        this.drawCurvePoints = new JCheckBox("Draw Curve Points", true);
        this.drawCurvePoints.setFocusable(false);
        this.drawCurvePoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.drawCurvePoints(UIPane.this.drawCurvePoints.isSelected());
            }
        });	
        
        this.renderAdaptive = new JCheckBox("Adaptive Render", false);
        this.renderAdaptive.setFocusable(false);
        this.renderAdaptive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIPane.this.setRenderType();
            }
        });
        
        this.adaptiveBernstein = new JCheckBox("De Casteljau/Bernstein", false);
        this.adaptiveBernstein.setFocusable(false);
        this.adaptiveBernstein.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIPane.this.setRenderType();
            }
        });
        
        slider = new JSlider(JSlider.HORIZONTAL, 1, 100, PaintPanel.resolution);
        slider.setFocusable(false);
        slider.setBorder(BorderFactory.createTitledBorder("Resolution"));
        slider.setName("Resolution");
        slider.setMinorTickSpacing(5);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.setResolution(slider.getValue());
            }
        });
        
        this.movePoints = new JCheckBox("Move", true);
        this.movePoints.setFocusable(false);
        this.movePoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIPane.this.setMovePointsMode();
            }
        });
        this.insertPoints  = new JCheckBox("Insert", false);
        this.insertPoints.setFocusable(false);
        this.insertPoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIPane.this.setInsertPointsMode();
            }
        });
        this.addPoints  = new JCheckBox("Add", false);
        this.addPoints.setFocusable(false);
        this.addPoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIPane.this.setAddPointsMode();
            }
        });       
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(this.movePoints);
        bg.add(this.addPoints);
        //bg.add(this.insertPoints);
        
		
		JPanel rbuttonPanel = new JPanel();
		rbuttonPanel.setLayout(new GridLayout(3, 1));
	    rbuttonPanel.add(this.movePoints);
	    rbuttonPanel.add(this.addPoints);
		//rbuttonPanel.add(this.insertPoints);
		
		rbuttonPanel.setBorder(BorderFactory.createTitledBorder("Control Points Mode"));
		this.resetButton = new JButton("Reset");
		this.resetButton.setFocusable(false);
		this.resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIPane.this.clear();
			}
		});		
		
		this.canvas = new PaintPanel();
		this.canvas.setBackground(Color.WHITE);
		this.canvas.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.canvas.setPreferredSize(new Dimension(PaintPanel.PREF_W, PaintPanel.PREF_H));		
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(7, 2));
		p.add(this.slider);
		p.add(this.drawControlPolygon);
		p.add(this.drawCurvePoints);
		//p.add(this.renderAdaptive);
		//p.add(this.adaptiveBernstein);
		p.add(rbuttonPanel);
		p.add(this.resetButton);
		
		this.setLayout(new FlowLayout());
	    this.add(this.canvas, BorderLayout.EAST);
		this.add(p, BorderLayout.WEST);

		this.registerKeyListeners();
	}
	
	public void stateChanged() {
		this.canvas.setDrawControlPolygon(this.drawControlPolygon.isSelected());
	}

	public void setRenderType()
	{
	    //this.canvas.plotUsingBernsteinPolynomial = this.adaptiveBernstein.isSelected();
	    
	    this.canvas.setAdaptiveRender(this.renderAdaptive.isSelected());
	    
	    /*
	    if(!this.renderAdaptive.isSelected())
	    {
	        this.adaptiveBernstein.setSelected(false);
	    }
	    */
	}
	
	public void setInsertPointsMode() {
		if(this.insertPoints.isSelected()) {
			this.canvas.setMode(PaintPanel.MODE.INSERT);
		}
	}
	
	public void setMovePointsMode() {
		if(this.movePoints.isSelected()) {
		    this.canvas.setMode(PaintPanel.MODE.MOVE);
		}
	}
	
	   public void setAddPointsMode() {
	        if(this.addPoints.isSelected()) {
	            this.canvas.setMode(PaintPanel.MODE.ADD);
	        }
	    }
	
	public void clear() {
		this.canvas.reset();
	}

   void registerKeyListeners()
    {
       this.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);                 
            }
        },
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);
       
       this.registerKeyboardAction(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               drawControlPolygon.doClick();                  
           }
       },
       KeyStroke.getKeyStroke(KeyEvent.VK_C, 0),
       JComponent.WHEN_IN_FOCUSED_WINDOW);
       
       this.registerKeyboardAction(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               drawCurvePoints.doClick();                   
           }
       },
       KeyStroke.getKeyStroke(KeyEvent.VK_P, 0),
       JComponent.WHEN_IN_FOCUSED_WINDOW);
       
       this.registerKeyboardAction(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               renderAdaptive.doClick();                   
           }
       },
       KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),
       JComponent.WHEN_IN_FOCUSED_WINDOW);
       
       this.registerKeyboardAction(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               slider.setValue(slider.getValue()+1);                   
           }
       },
       KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.SHIFT_MASK),
       JComponent.WHEN_IN_FOCUSED_WINDOW);
       
       this.registerKeyboardAction(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               slider.setValue(slider.getValue()-1);                  
           }
       },
       KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.SHIFT_MASK),
       JComponent.WHEN_IN_FOCUSED_WINDOW);
           
    }
   
}
