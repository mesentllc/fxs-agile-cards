package com.fedex.services.agile.cards.report;

import com.fedex.services.agile.cards.model.TaskModel;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

public abstract class CardReport {
	static final StyleBuilders styleBuilders = DynamicReports.stl;
	static final int cardHeight = (PageType.LETTER.getWidth() - 80) / 2;
	static final int cardWidth = (PageType.LETTER.getHeight() - 60) / 2;
	static final StyleBuilder bold7Style = DynamicReports.stl.style(Templates.boldStyle).setFontSize(7)
		.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP).setPadding(2).setForegroundColor(Color.BLACK);
	static final StyleBuilder bold10Style = DynamicReports.stl.style(Templates.boldStyle).setFontSize(10)
		.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP).setForegroundColor(Color.BLACK);
	static final StyleBuilder noline8style = DynamicReports.stl.style(Templates.boldStyle).setFontSize(8)
			.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP).setPadding(2)
			.setForegroundColor(Color.BLACK);
	private static final JasperReportBuilder report = DynamicReports.report();
	protected abstract ComponentBuilder<?, ?> buildCard(TaskModel taskModel);

	protected ComponentBuilder<?, ?> buildCardHeader(TaskModel taskModel) {
		StyleBuilder lineStyle = styleBuilders.style(styleBuilders.pen1Point()).bold().setFontSize(10);
		Integer headerHeight = cardHeight / 4;
		Integer headerWidth = cardWidth * 9 / 10;
		if (taskModel.getTask() != null) {
			StyleBuilder style = DynamicReports.stl.style(Templates.boldStyle).setFontSize(10).setForegroundColor(Color.BLACK);
			return cmp.horizontalList(cmp.horizontalGap(15),
					cmp.verticalList(
							cmp.verticalList(cmp.text("Feature: " + getString(taskModel.getFeature()) +
							                          "\nID: " + getString(taskModel.getTaskId()) + "\nTask: " +
							                          getString(taskModel.getTask())).setStyle(style)),
							cmp.text("User Story: " + getString(taskModel.getUserStoryId()) + " - " +
									getString(taskModel.getUserStoryTitle())).setStyle(Templates.printStyle))
							.setFixedWidth(headerWidth),
					cmp.verticalList(cmp.text("Hours\n\n" + getString(taskModel.getHours())).setStyle(lineStyle)
							.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)))
					.setFixedHeight(headerHeight);
		}
		else {
			return cmp.horizontalList(cmp.horizontalGap(15),
					cmp.verticalList(cmp.text("Feature: " + getString(taskModel.getFeature()) +
					                          "\nID: " + getString(taskModel.getUserStoryId()) +
					                          "\nUser Story: " + getString(taskModel.getUserStoryTitle()))
					                    .setStyle(bold10Style)).setFixedWidth(headerWidth),
					cmp.verticalList(cmp.text("Points\n" + getString(taskModel.getStoryPoints())).setStyle(lineStyle)
							.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)))
					.setFixedHeight(headerHeight);
		}
	}

	String getString(String string) {
		if (string == null || string.trim().length() == 0) {
			return "";
		}
		return string;
	}

	public JasperReportBuilder buildCards(List<TaskModel> tasks) {
		MultiPageListBuilder multiPageList = cmp.multiPageList();
		List<ComponentBuilder<?, ?>> cards = new ArrayList<>(4);
		boolean largeCardSeen = false;

		for (TaskModel task : tasks) {
			if (task.getDescription().length() > 250) {
				largeCardSeen = true;
			}
			if (cards.size() == 4 || (cards.size() > 1 && largeCardSeen)) {
				fabricate(multiPageList, cards);
				cards = new ArrayList<>(4);
			}
			cards.add(buildCard(task));
			if (cards.size() == 2) {
				multiPageList.add(cmp.verticalGap(10),
				                  cmp.horizontalList(cmp.horizontalGap(5), cards.get(0), cmp.horizontalGap(20), cards.get(1)));
				cards = new ArrayList<>(4);
				largeCardSeen = false;
			}
		}
		if (!cards.isEmpty()) {
			fabricate(multiPageList, cards);
		}
		report.setPageFormat(PageType.LETTER, PageOrientation.LANDSCAPE).summary(multiPageList);
		return report;
	}

	private void fabricate(MultiPageListBuilder multiPageList, List<ComponentBuilder<?, ?>> cards) {
		while (cards.size() < 4) {
			cards.add(buildCard(null));
		}
		multiPageList.add(cmp.verticalGap(10),
			cmp.horizontalList(cmp.horizontalGap(5), cards.get(0), cmp.horizontalGap(20), cards.get(1)),
			cmp.verticalGap(20),
			cmp.horizontalList(cmp.horizontalGap(5), cards.get(2), cmp.horizontalGap(20), cards.get(3)));
	}
}
