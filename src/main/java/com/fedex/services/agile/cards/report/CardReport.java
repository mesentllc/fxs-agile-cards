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
	protected static final JasperReportBuilder report = DynamicReports.report();
	protected static final StyleBuilders styleBuilders = DynamicReports.stl;
	protected static final int cardHeight = (PageType.LETTER.getWidth() - 60) / 2;
	protected static final int cardWidth = (PageType.LETTER.getHeight() - 60) / 2;
	protected static final StyleBuilder bold7Style = DynamicReports.stl.style(Templates.boldStyle).setFontSize(7)
			.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP).setPadding(2)
			.setForegroundColor(Color.BLACK);
	protected static final StyleBuilder noline8style = DynamicReports.stl.style(Templates.boldStyle).setFontSize(8)
			.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP).setPadding(2)
			.setForegroundColor(Color.BLACK);
	protected static final StyleBuilder line8style = DynamicReports.stl.style(styleBuilders.pen1Point()).setFontSize(8)
			.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP).setPadding(2)
			.setForegroundColor(Color.BLACK);
	protected static final StyleBuilder bold10Style = DynamicReports.stl.style(Templates.boldStyle).setFontSize(10)
			.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP)
			.setForegroundColor(Color.BLACK);

	protected static final Color COLOR_ORANGE = new Color(205, 110, 0);
	protected static final Color COLOR_PURPLE = new Color(102, 0, 153);
	protected static final Color COLOR_GOLD = new Color(255, 208, 0);
	protected static final Color COLOR_BROWN = new Color(110, 70, 0);

	protected abstract ComponentBuilder<?, ?> buildCard(TaskModel taskModel);

	protected ComponentBuilder<?, ?> buildCardHeader(TaskModel taskModel) {
		StyleBuilder lineStyle = styleBuilders.style(styleBuilders.pen1Point()).bold().setFontSize(10);
		Integer headerHeight = cardHeight / 4;
		Integer headerWidth = cardWidth * 9 / 10;
		if (taskModel.getTask() != null) {
			StyleBuilder style = getTextColor(taskModel.getTask());
			return cmp.horizontalList(cmp.horizontalGap(5),
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
			return cmp.horizontalList(cmp.horizontalGap(5),
					cmp.verticalList(cmp.text("Feature: " + getString(taskModel.getFeature()) +
					                          "\nID: " + getString(taskModel.getUserStoryId()) +
					                          "\nUser Story: " + getString(taskModel.getUserStoryTitle()))
					                    .setStyle(bold10Style)).setFixedWidth(headerWidth),
					cmp.verticalList(cmp.text("Points\n" + getString(taskModel.getStoryPoints())).setStyle(lineStyle)
							.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)))
					.setFixedHeight(headerHeight);
		}
	}

	private StyleBuilder getTextColor(String task) {
		if (task == null) {
			return DynamicReports.stl.style(Templates.boldStyle)
					.setFontSize(10).setForegroundColor(Color.BLACK);
		}
		String string = task.toUpperCase();
		if (string.toUpperCase().startsWith("CODE REVIEW")) {
			return DynamicReports.stl.style(Templates.boldStyle)
					.setFontSize(10).setForegroundColor(COLOR_GOLD);
		}
		if (string.toUpperCase().startsWith("COMPLIANCE")) {
			return DynamicReports.stl.style(Templates.boldStyle)
					.setFontSize(10).setForegroundColor(COLOR_GOLD);
		}
		if (string.toUpperCase().startsWith("CODE")) {
			return DynamicReports.stl.style(Templates.boldStyle)
					.setFontSize(10).setForegroundColor(COLOR_ORANGE);
		}
		if (string.toUpperCase().startsWith("DEPLOY")) {
			return DynamicReports.stl.style(Templates.boldStyle)
					.setFontSize(10).setForegroundColor(Color.BLUE);
		}
		if (string.toUpperCase().startsWith("TEST")) {
			return DynamicReports.stl.style(Templates.boldStyle)
					.setFontSize(10).setForegroundColor(Color.GREEN);
		}
		if (string.toUpperCase().startsWith("DEFECT")) {
			return DynamicReports.stl.style(Templates.boldStyle)
					.setFontSize(10).setForegroundColor(Color.RED);
		}
		if (string.toUpperCase().startsWith("RESEARCH")) {
			return DynamicReports.stl.style(Templates.boldStyle)
					.setFontSize(10).setForegroundColor(COLOR_PURPLE);
		}
		if (string.toUpperCase().startsWith("SPIKE")) {
			return DynamicReports.stl.style(Templates.boldStyle)
					.setFontSize(10).setForegroundColor(COLOR_PURPLE);
		}
		if (string.toUpperCase().startsWith("DEMO")) {
			return DynamicReports.stl.style(Templates.boldStyle)
					.setFontSize(10).setForegroundColor(COLOR_BROWN);
		}
		return DynamicReports.stl.style(Templates.boldStyle)
				.setFontSize(10).setForegroundColor(Color.BLACK);
	}

	protected ComponentBuilder<?, ?> buildDecription(String itg, String description) {
		Integer sectionHeight = cardHeight * 3 / 4;
		StyleBuilder style = styleBuilders.style(styleBuilders.pen1Point());
		return cmp.verticalList(cmp.verticalGap(5),
				cmp.text("ITG: " + getString(itg) + "\n" + getString(description)).setStyle(Templates.printStyle))
				.setStyle(style).setFixedHeight(sectionHeight);
	}

	protected String getString(String string) {
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
				while (cards.size() < 4) {
					cards.add(buildCard(null));
				}
				multiPageList.add(cmp.verticalGap(10),
						cmp.horizontalList(cmp.horizontalGap(5), cards.get(0), cmp.horizontalGap(20), cards.get(1)),
						cmp.verticalGap(20),
						cmp.horizontalList(cmp.horizontalGap(5), cards.get(2), cmp.horizontalGap(20), cards.get(3)));
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
			while (cards.size() < 4) {
				cards.add(buildCard(null));
			}
			multiPageList.add(cmp.verticalGap(10),
					cmp.horizontalList(cmp.horizontalGap(5), cards.get(0), cmp.horizontalGap(20), cards.get(1)),
					cmp.verticalGap(20),
					cmp.horizontalList(cmp.horizontalGap(5), cards.get(2), cmp.horizontalGap(20), cards.get(3)));
		}
		report.setPageFormat(PageType.LETTER, PageOrientation.LANDSCAPE).summary(multiPageList);
		return report;
	}
}
