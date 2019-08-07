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
	private final Integer headerWidth = getCardWidth() * 6 / 10;

	public V1CardCreator(boolean oneCard) {
		super(oneCard);
	}

	private StyleBuilder boldBgColorStyle(Color color) {
		if (color == Color.BLACK) {
			return DynamicReports.stl.style(getLineStyle()).setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP)
			                         .setPadding(2).setBackgroundColor(color).setForegroundColor(Color.WHITE);
		}
		return DynamicReports.stl.style(getLineStyle()).setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP)
		                         .setPadding(2).setBackgroundColor(color);
	}

	private ComponentBuilder<?, ?> buildLine1(TaskModel taskModel) {
		return cmp.horizontalList(cmp.text(getString(taskModel.getEpic())).setStyle(getBoldStyleSmall()).setFixedWidth(headerWidth),
		                          cmp.horizontalList(cmp.text("Release:\n" + getString(taskModel.getRelease())).setStyle(getBoldStyleSmall()),
			                          cmp.verticalList(cmp.text("Sprint:").setStyle(getBoldStyleSmall()),
				                          cmp.text(getString(taskModel.getSprint())).setStyle(getNoLineStyle())).setStyle(getLineStyle())).setStyle(getLineStyle()))
			.setStyle(getLineStyle());
	}

	private ComponentBuilder<?, ?> buildLine2(TaskModel taskModel) {
		return cmp.horizontalList(cmp.text("Feature:\n" + " " + getString(taskModel.getFeature()))
		                             .setFixedWidth(headerWidth).setStyle(boldBgColorStyle(taskModel.getColor())),
		                          cmp.verticalList(cmp.text("Assigned To:").setStyle(getBoldStyleSmall()),
			                          cmp.text(getString(taskModel.getAssignedTo())).setStyle(getNoLineStyle()))
		                             .setStyle(getBoldStyleSmall())).setStyle(getLineStyle());
	}

	private ComponentBuilder<?, ?> buildLine3(TaskModel taskModel) {
		return cmp.horizontalList(cmp.text("User Story: " + getString(taskModel.getUserStoryTitle())).setStyle(getBoldStyleSmall())
		                             .setStyle(getLineStyle()).setFixedWidth(headerWidth),
		                          cmp.horizontalList(cmp.verticalList(cmp.text("Id:").setStyle(getBoldStyleSmall()),
			                          cmp.text(getString(taskModel.getUserStoryId())).setStyle(getBoldStyleLarge())).setStyle(getLineStyle()),
		                                             cmp.verticalList(cmp.text("Points:").setStyle(getBoldStyleSmall()), cmp.text(taskModel.getStoryPoints())
		                                                                                                           .setStyle(getNormalStyleSmall()))
		                                                .setStyle(getLineStyle()))).setStyle(getLineStyle());
	}

	protected ComponentBuilder<?, ?> buildCardHeader(TaskModel taskModel) {
		Integer headerHeight = getCardHeight() / 3;
		return cmp.verticalList(buildLine1(taskModel), buildLine2(taskModel), buildLine3(taskModel)).setFixedHeight(headerHeight);
	}

	private ComponentBuilder<?, ?> buildDescription(TaskModel taskModel) {
		return cmp.verticalList(cmp.text("Description:\n" + getString(taskModel.getDescription())).setStyle(getPrintStyle()).setFixedWidth(getCardWidth()))
		          .setStyle(getLineStyle());
	}

	@Override
	protected ComponentBuilder<?, ?> buildCard(TaskModel taskModel) {
		if (taskModel == null) {
			StyleBuilder style = styleBuilders.style(styleBuilders.pen1Point()).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
			return cmp.text("Blank Card").setStyle(style);
		}
		return cmp.verticalList(cmp.verticalGap(10).setStyle(getPrintStyle()), cmp.verticalList(buildCardHeader(taskModel),
			buildDescription(taskModel)).setFixedWidth(getCardWidth()).setFixedHeight(getCardHeight()).setStyle(getLineStyle()));
	}
}
