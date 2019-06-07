package com.fedex.services.agile.cards.report;

import com.fedex.services.agile.cards.model.TaskModel;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

import java.awt.Color;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

public class V1CardCreator extends CardReport {
	private static final StyleBuilder lineStyle = styleBuilders.style(styleBuilders.pen1Point()).bold().setFontSize(7);
	private static final Integer headerWidth = cardWidth * 6 / 10;

	private StyleBuilder boldBgColorStyle(Color color) {
		if (color == Color.BLACK) {
			return DynamicReports.stl.style(lineStyle).setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP)
			                         .setPadding(2).setBackgroundColor(color).setForegroundColor(Color.WHITE);
		}
		return DynamicReports.stl.style(lineStyle).setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP)
		                         .setPadding(2).setBackgroundColor(color);
	}

	private StyleBuilder norm15Style() {
		return DynamicReports.stl.style(styleBuilders.style()).setFontSize(15)
		                         .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.TOP)
		                         .setPadding(2).setForegroundColor(Color.BLACK);
	}

	private ComponentBuilder<?, ?> buildLine1(TaskModel taskModel) {
		return cmp.horizontalList(cmp.text(getString(taskModel.getEpic())).setStyle(bold7Style).setFixedWidth(headerWidth),
		                          cmp.horizontalList(cmp.text("Release:\n" + getString(taskModel.getRelease())).setStyle(bold7Style),
			                          cmp.verticalList(cmp.text("Sprint:").setStyle(bold7Style),
				                          cmp.text(getString(taskModel.getSprint())).setStyle(noline8style)).setStyle(lineStyle)).setStyle(lineStyle))
			.setStyle(lineStyle);
	}

	private ComponentBuilder<?, ?> buildLine2(TaskModel taskModel) {
		return cmp.horizontalList(cmp.text("Feature:\n" + " " + getString(taskModel.getFeature()))
		                             .setFixedWidth(headerWidth).setStyle(boldBgColorStyle(taskModel.getColor())),
		                          cmp.verticalList(cmp.text("Assigned To:").setStyle(bold7Style),
			                          cmp.text(getString(taskModel.getAssignedTo())).setStyle(noline8style))
		                             .setStyle(bold7Style)).setStyle(lineStyle);
	}

	private ComponentBuilder<?, ?> buildLine3(TaskModel taskModel) {
		return cmp.horizontalList(cmp.text("User Story: " + getString(taskModel.getUserStoryTitle())).setStyle(bold7Style)
		                             .setStyle(lineStyle).setFixedWidth(headerWidth),
		                          cmp.horizontalList(cmp.verticalList(cmp.text("Id:").setStyle(bold7Style),
			                          cmp.text(getString(taskModel.getUserStoryId())).setStyle(bold10Style)).setStyle(lineStyle),
		                                             cmp.verticalList(cmp.text("Points:").setStyle(bold7Style), cmp.text(taskModel.getStoryPoints())
		                                                                                                           .setStyle(norm15Style()))
		                                                .setStyle(lineStyle))).setStyle(lineStyle);
	}

	protected ComponentBuilder<?, ?> buildCardHeader(TaskModel taskModel) {
		Integer headerHeight = cardHeight / 3;
		return cmp.verticalList(buildLine1(taskModel), buildLine2(taskModel), buildLine3(taskModel)).setFixedHeight(headerHeight);
	}

	private ComponentBuilder<?, ?> buildDescription(TaskModel taskModel) {
		return cmp.verticalList(cmp.text("Description:\n" + getString(taskModel.getDescription())).setStyle(Templates.printStyle).setFixedWidth(cardWidth))
		          .setStyle(lineStyle);
	}

	@Override
	protected ComponentBuilder<?, ?> buildCard(TaskModel taskModel) {
		if (taskModel == null) {
			StyleBuilder style = styleBuilders.style(styleBuilders.pen1Point()).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
			return cmp.text("Blank Card").setStyle(style);
		}
		return cmp.verticalList(cmp.verticalGap(10).setStyle(Templates.printStyle), cmp.verticalList(buildCardHeader(taskModel),
			buildDescription(taskModel)).setFixedWidth(cardWidth).setFixedHeight(cardHeight).setStyle(lineStyle));
	}
}
