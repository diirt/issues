/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.graphene;

import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VImage;
import org.diirt.vtype.ValueUtil;
import java.awt.image.BufferedImage;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import org.diirt.graphene.*;
import org.diirt.datasource.QueueCollector;
import org.diirt.datasource.ReadFunction;
import static org.diirt.datasource.graphene.ArgumentExpressions.*;
import org.diirt.util.stats.Statistics;
import org.diirt.util.stats.StatisticsUtil;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;

/**
 *
 * @author carcassi
 */
class MultiAxisLineGraph2DFunction implements ReadFunction<Graph2DResult> {

    private ReadFunction<VType> tableData;
    private ReadFunctionArgument<List<String>> xColumnNames;
    private ReadFunctionArgument<List<String>> yColumnNames;

    private MultiAxisLineGraph2DRenderer renderer = new MultiAxisLineGraph2DRenderer(300, 200);

    private VImage previousImage;
    private final QueueCollector<MultiAxisLineGraph2DRendererUpdate> rendererUpdateQueue = new QueueCollector<>(100);

    MultiAxisLineGraph2DFunction(ReadFunction<?> tableData,
            ReadFunction<?> xColumnName,
            ReadFunction<?> yColumnName) {
        this.tableData = new CheckedReadFunction<VType>(tableData, "Data", VTable.class, VNumberArray.class);
        this.xColumnNames = stringArrayArgument(xColumnName, "X Columns");
        this.yColumnNames = stringArrayArgument(yColumnName, "Y Columns");
    }

    public QueueCollector<MultiAxisLineGraph2DRendererUpdate> getRendererUpdateQueue() {
        return rendererUpdateQueue;
    }

    @Override
    public Graph2DResult readValue() {
        VType vType = tableData.readValue();
        xColumnNames.readNext();
        yColumnNames.readNext();

        // Table and columns must be available
        if (vType == null || xColumnNames.isMissing() || yColumnNames.isMissing()) {
            return null;
        }

        // Prepare new dataset
        final List<Point2DDataset> dataset;
        if (vType instanceof VNumberArray) {
            dataset = Collections.singletonList(Point2DDatasets.lineData(((VNumberArray) vType).getData()));
        } else {
            dataset = DatasetConversions.point2DDatasetsFromVTable((VTable) vType, xColumnNames.getValue(), yColumnNames.getValue());
        }

        // Process all renderer updates
        List<MultiAxisLineGraph2DRendererUpdate> updates = rendererUpdateQueue.readValue();
        for (MultiAxisLineGraph2DRendererUpdate rendererUpdate : updates) {
            renderer.update(rendererUpdate);
        }

        // If no size is set, don't calculate anything
        if (renderer.getImageHeight() == 0 && renderer.getImageWidth() == 0)
            return null;

        BufferedImage image = new BufferedImage(renderer.getImageWidth(), renderer.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
        renderer.draw(image.createGraphics(), dataset);

        previousImage = ValueUtil.toVImage(image);
        Statistics xDataRange = StatisticsUtil.statisticsOf(new AbstractList<Statistics>() {

            @Override
            public Statistics get(int index) {
                return dataset.get(index).getXStatistics();
            }

            @Override
            public int size() {
                return dataset.size();
            }
        });

        Statistics yDataRange = StatisticsUtil.statisticsOf(new AbstractList<Statistics>() {

            @Override
            public Statistics get(int index) {
                return dataset.get(index).getYStatistics();
            }

            @Override
            public int size() {
                return dataset.size();
            }
        });

        return new Graph2DResult(vType, previousImage,
                new GraphDataRange(renderer.getXPlotRange(), xDataRange.getRange(), renderer.getXAggregatedRange()),
                new GraphDataRange(renderer.getYPlotRange(), yDataRange.getRange(), renderer.getYAggregatedRange()),
                -1);
    }

}
