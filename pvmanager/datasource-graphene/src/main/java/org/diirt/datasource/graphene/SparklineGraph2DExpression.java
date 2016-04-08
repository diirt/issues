/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.graphene;

import org.diirt.graphene.SparklineGraph2DRendererUpdate;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.DesiredRateExpressionImpl;
import static org.diirt.datasource.graphene.ExpressionLanguage.functionOf;

/**
 *
 * @author carcassi
 */
public class SparklineGraph2DExpression extends DesiredRateExpressionImpl<Graph2DResult> implements Graph2DExpression<SparklineGraph2DRendererUpdate> {

    SparklineGraph2DExpression(DesiredRateExpression<?> tableData,
            DesiredRateExpression<?> xColumnName,
            DesiredRateExpression<?> yColumnName) {
        super(ExpressionLanguage.<Object>createList(tableData, xColumnName, yColumnName),
                new SparklineGraph2DFunction(functionOf(tableData),
                functionOf(xColumnName), functionOf(yColumnName)),
                "Sparkline Graph");
    }

    @Override
    public void update(SparklineGraph2DRendererUpdate update) {
        ((SparklineGraph2DFunction) getFunction()).getRendererUpdateQueue().writeValue(update);
    }

    @Override
    public SparklineGraph2DRendererUpdate newUpdate() {
        return new SparklineGraph2DRendererUpdate();
    }
}
