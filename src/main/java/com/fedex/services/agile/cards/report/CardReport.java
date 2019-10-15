package com.fedex.services.agile.cards.report;

import com.fedex.services.agile.cards.comparitor.DescriptionLengthComparator;
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
import java.util.Collections;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

public abstract class CardReport {
	private static final int cardHeight_1 = PageType.LETTER.getWidth() - 80;
	private static final int cardWidth_1 = PageType.LETTER.getHeight() - 30;
	private static final int cardHeight_4 = (PageType.LETTER.getWidth() - 80) / 2;
	private static final int cardWidth_4 = (PageType.LETTER.getHeight() - 60) / 2;
	static final StyleBuilders styleBuilders = DynamicReports.stl;
	private static final JasperReportBuilder report = DynamicReports.report();
	protected abstract ComponentBuilder<?, ?> buildCard(TaskModel taskModel);
	final boolean oneCard;

	CardReport(boolean oneCard) {
		this.oneCard = oneCard;
	}

	int getCardHeight() {
		if (oneCard) {
			return cardHeight_1;
		}
		return cardHeight_4;
	}

	int getCardWidth() {
		if (oneCard) {
			return cardWidth_1;
		}
		return cardWidth_4;
	}

	StyleBuilder getPrintStyle() {
		if (oneCard) {
			return styleBuilders.style().setPadding(2).setFontSize(16);
		}
		return styleBuilders.style().setPadding(2).setFontSize(7);
	}

	StyleBuilder getLineStyle() {
		if (oneCard) {
			return styleBuilders.style(styleBuilders.pen1Point()).bold().setFontSize(16);
		}
		return styleBuilders.style(styleBuilders.pen1Point()).bold().setFontSize(7);
	}

	StyleBuilder getBoldStyleSmall() {
		if (oneCard) {
			return DynamicReports.stl.style(Templates.boldStyle).setFontSize(16)
				.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP)
				.setPadding(2).setForegroundColor(Color.BLACK);
		}
		return DynamicReports.stl.style(Templates.boldStyle).setFontSize(7)
			.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP)
			.setPadding(2).setForegroundColor(Color.BLACK);
	}

	StyleBuilder getBoldStyleLarge() {
		if (oneCard) {
			return DynamicReports.stl.style(Templates.boldStyle).setFontSize(20)
				.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP)
				.setForegroundColor(Color.BLACK);
		}
		return DynamicReports.stl.style(Templates.boldStyle).setFontSize(10)
			.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP)
			.setForegroundColor(Color.BLACK);
	}

	StyleBuilder getNoLineStyle() {
		if (oneCard) {
			return DynamicReports.stl.style(Templates.boldStyle).setFontSize(16)
				.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP).setPadding(2)
				.setForegroundColor(Color.BLACK);
		}
		return DynamicReports.stl.style(Templates.boldStyle).setFontSize(8)
			.setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP).setPadding(2)
			.setForegroundColor(Color.BLACK);
	}

	StyleBuilder getNormalStyleSmall() {
		if (oneCard) {
			return DynamicReports.stl.style(styleBuilders.style()).setFontSize(30)
				.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.TOP)
				.setPadding(2).setForegroundColor(Color.BLACK);
		}
		return DynamicReports.stl.style(styleBuilders.style()).setFontSize(15)
			.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.TOP)
			.setPadding(2).setForegroundColor(Color.BLACK);
	}

	StyleBuilder getNormalStyleLarge() {
		if (oneCard) {
			return DynamicReports.stl.style(styleBuilders.style()).setFontSize(40)
				.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.TOP)
				.setPadding(2).setForegroundColor(Color.BLACK);
		}
		return DynamicReports.stl.style(styleBuilders.style()).setFontSize(20)
			.setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.TOP)
			.setPadding(2).setForegroundColor(Color.BLACK);
	}

	protected ComponentBuilder<?, ?> buildCardHeader(TaskModel taskModel) {
		StyleBuilder lineStyle = styleBuilders.style(styleBuilders.pen1Point()).bold().setFontSize(10);
		Integer headerHeight = getCardHeight() / 4;
		Integer headerWidth = getCardWidth() * 9 / 10;
		if (taskModel.getTask() != null) {
			StyleBuilder style = DynamicReports.stl.style(Templates.boldStyle).setFontSize(10).setForegroundColor(Color.BLACK);
			return cmp.horizontalList(cmp.horizontalGap(15),
					cmp.verticalList(
							cmp.verticalList(cmp.text("Feature: " + getString(taskModel.getFeature()) +
							                          "\nID: " + getString(taskModel.getTaskId()) + "\nTask: " +
							                          getString(taskModel.getTask())).setStyle(style)),
							cmp.text("User Story: " + getString(taskModel.getUserStoryId()) + " - " +
									getString(taskModel.getUserStoryTitle())).setStyle(getPrintStyle()))
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
					                    .setStyle(getBoldStyleLarge())).setFixedWidth(headerWidth),
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
		tasks.sort(new DescriptionLengthComparator());
		if (oneCard) {
			return buildSingles(tasks);
		}
		return buildQuads(tasks);
//		for (TaskModel task : tasks) {
//			if (oneCard) {
//				preparePageOf1(task, multiPageList);
//			}
//			else {
//				largeCardSeen = preparePageOf4(task, cards, multiPageList, largeCardSeen);
//			}
//		}
//		if (!cards.isEmpty()) {
//			fabricate(multiPageList, cards);
//		}
//		report.setPageFormat(PageType.LETTER, PageOrientation.LANDSCAPE).summary(multiPageList);
//		return report;
	}

	private JasperReportBuilder buildSingles(List<TaskModel> tasks) {
		MultiPageListBuilder multiPageList = cmp.multiPageList();

		for (TaskModel task : tasks) {
			ComponentBuilder<?, ?> card = buildCard(task);
			fabricate(multiPageList, card);
		}
		report.setPageFormat(PageType.LETTER, PageOrientation.LANDSCAPE).summary(multiPageList);
		return report;
	}

	private JasperReportBuilder buildQuads(List<TaskModel> tasks) {
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

	private void preparePageOf1(TaskModel task, MultiPageListBuilder multiPageList) {
		ComponentBuilder<?, ?> card = buildCard(task);
		fabricate(multiPageList, card);
	}

	private boolean preparePageOf4(TaskModel task, List<ComponentBuilder<?, ?>> cards,
	                               MultiPageListBuilder multiPageList, boolean largeCardSeen) {
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
		return largeCardSeen;
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

	private void fabricate(MultiPageListBuilder multiPageList, ComponentBuilder<?, ?> card) {
		multiPageList.add(cmp.verticalGap(10), cmp.horizontalList(cmp.horizontalGap(5), card));
	}
}
