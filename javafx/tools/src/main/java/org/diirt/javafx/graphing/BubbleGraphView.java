/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diirt.javafx.graphing;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import static org.diirt.datasource.formula.ExpressionLanguage.formula;
import static org.diirt.datasource.formula.ExpressionLanguage.formulaArg;
import org.diirt.datasource.graphene.BubbleGraph2DExpression;
import static org.diirt.datasource.graphene.ExpressionLanguage.bubbleGraphOf;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.graphene.BubbleGraph2DRendererUpdate;

/**
 *
 * @author mjchao
 */
public class BubbleGraphView extends BaseGraphView< BubbleGraph2DRendererUpdate > {

    private StringProperty xColumn = new SimpleStringProperty( null );
    private StringProperty yColumn = new SimpleStringProperty( null );
    private StringProperty sizeColumn = new SimpleStringProperty( null );
    private StringProperty colorColumn = new SimpleStringProperty( null );
    private BooleanProperty highlightFocusValue = new SimpleBooleanProperty( false );
    
    @Override
    public Graph2DExpression<BubbleGraph2DRendererUpdate> createExpression(String dataFormula) {
	BubbleGraph2DExpression plot = bubbleGraphOf(formula(dataFormula),
	    formulaArg(xColumn.getValue()),
	    formulaArg(yColumn.getValue()),
	    formulaArg(sizeColumn.getValue()),
	    formulaArg(colorColumn.getValue())
	);
	return plot;
    }
    
    public BubbleGraphView() {
	
	this.highlightFocusValue.addListener( new ChangeListener< Boolean >() {

	    @Override
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		graph.update( graph.newUpdate().highlightFocusValue( newValue ) );
	    }
	});
	
	this.xColumn.addListener( new ChangeListener< String >() {

	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		BubbleGraphView.super.reconnect();
	    }
	});
	
	this.yColumn.addListener( new ChangeListener< String >() {

	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		BubbleGraphView.super.reconnect();
	    }
	    
	});
	
	this.sizeColumn.addListener( new ChangeListener< String >() {

	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		BubbleGraphView.super.reconnect();
	    }
	    
	});
	
	this.colorColumn.addListener( new ChangeListener< String >() {

	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		BubbleGraphView.super.reconnect();
	    }
	});
    }
    
    @Override
    protected void onMouseMove(MouseEvent e) {
        graph.update(graph.newUpdate().focusPixel( (int)e.getX(), (int)e.getY()));
    }
    
    public BooleanProperty highlightFocusValueProperty() {
	return this.highlightFocusValue;
    }
    
    public boolean isHighlightFocusValue() {
        return highlightFocusValue.getValue();
    }

    public void setHighlightFocusValue(boolean highlightFocusValue) {
        this.highlightFocusValue.setValue( highlightFocusValue );
    }
    
    public StringProperty xColumnProperty() {
	return this.xColumn;
    }
    
    public String getXColumn() {
        return xColumn.getValue();
    }

    public void setXColumn(String xColumn) {
        this.xColumn.setValue( xColumn );
    }
    
    public StringProperty yColumnProperty() {
	return this.yColumn;
    }
    
    public String getYColumn() {
        return yColumn.getValue();
    }

    public void setYColumn(String yColumn) {
        this.yColumn.setValue( yColumn );
    }
    
    public StringProperty sizeColumnProperty() {
	return this.sizeColumn;
    }
    
    public String getSizeColumn() {
        return sizeColumn.getValue();
    }

    public void setSizeColumn(String sizeColumn) {
        this.sizeColumn.setValue( sizeColumn );
    }
    
    public StringProperty colorColumnProperty() {
	return this.colorColumn;
    }
    
    public String getColorColumn() {
        return colorColumn.getValue();
    }

    public void setColorColumn(String colorColumn) {
        this.colorColumn.setValue( colorColumn );
    }
}
