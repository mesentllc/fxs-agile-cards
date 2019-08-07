package com.fedex.services.agile.cards.report;

import com.fedex.services.agile.cards.model.TaskModel;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

public class FeatureTaskCreator extends CardReport {
	FeatureTaskCreator(boolean oneCard) {
		super(oneCard);
	}

	private ComponentBuilder<?, ?> buildSuccessCriteria(TaskModel taskModel) {
		StyleBuilder style = styleBuilders.style(styleBuilders.pen1Point());
		Integer sectionHeight = getCardHeight() * 2 / 4;
		return cmp.verticalList(cmp.verticalGap(5),
				cmp.text("Success Criteria: " + getString(taskModel.getAcceptanceCriteria()) + "\n\n"
						+ "ITG: " + getString(taskModel.getItg()) + "\n\n"
						+ "Priority: " + getString(taskModel.getPriority())).setStyle(getPrintStyle())).setStyle(style)
				.setFixedHeight(sectionHeight);
	}

	@Override
	protected ComponentBuilder<?, ?> buildCard(TaskModel taskModel) {
		if (taskModel == null) {
			StyleBuilder style = styleBuilders.style(styleBuilders.pen1Point()).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
			return cmp.text("Blank Card").setStyle(style);
		}
		StyleBuilder style = styleBuilders.style(styleBuilders.pen1Point());
		return cmp.verticalList(buildCardHeader(taskModel), buildDescription(taskModel.getItg(), taskModel.getDescription()), buildSuccessCriteria(taskModel))
				.setFixedHeight(getCardHeight()).setFixedWidth(getCardWidth()).setStyle(style);
	}

	@Override
	protected ComponentBuilder<?, ?> buildCardHeader(TaskModel taskModel) {
		if (taskModel == null) {
			taskModel = new TaskModel();
		}
		StyleBuilder lineStyle = styleBuilders.style(styleBuilders.pen1Point()).bold().setFontSize(10);
		Integer headerHeight = getCardHeight() / 4;
		Integer headerWidth = getCardWidth() * 9 / 10;
		return cmp.horizontalList(cmp.verticalList(cmp.text("ID: " + getString(taskModel.getUserStoryId()) + "\nFeature: " +
				getString(taskModel.getUserStoryTitle())).setStyle(getBoldStyleLarge()))
				.setFixedWidth(headerWidth), cmp.verticalList(cmp.text("Points\n" + getString(taskModel.getStoryPoints())).setStyle(lineStyle)
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)))
				.setFixedHeight(headerHeight);
	}

	private ComponentBuilder<?, ?> buildDescription(String itg, String description) {
		Integer sectionHeight = getCardHeight() / 4;
		StyleBuilder style = styleBuilders.style(styleBuilders.pen1Point());
		return cmp.verticalList(cmp.verticalGap(5),
				cmp.text("Description: " + getString(description)).setStyle(getPrintStyle()))
				.setStyle(style).setFixedHeight(sectionHeight);
	}
}
