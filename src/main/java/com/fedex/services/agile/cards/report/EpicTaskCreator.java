package com.fedex.services.agile.cards.report;

import java.awt.Color;

import com.fedex.services.agile.cards.model.TaskModel;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

public class EpicTaskCreator extends CardReport {
	private final Integer headerWidth = getCardWidth() * 6 / 10;

	public EpicTaskCreator(boolean oneCard) {
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
						cmp.text("Sprint:\n").setStyle(getLineStyle())).setStyle(getLineStyle()))
				.setStyle(getLineStyle());
	}

	private ComponentBuilder<?, ?> buildLine2(TaskModel taskModel) {
		return cmp.horizontalList(cmp.text("Feature:\n" + " " + getString(taskModel.getFeature()))
						.setFixedWidth(headerWidth).setStyle(boldBgColorStyle(taskModel.getColor())),
				cmp.verticalList(cmp.text("Assigned To:\n" + getString(taskModel.getAssignedTo())).setStyle(getBoldStyleSmall()))
						.setStyle(getBoldStyleSmall())).setStyle(getLineStyle());
	}

	private ComponentBuilder<?, ?> buildLine3(TaskModel taskModel) {
		return cmp.horizontalList(cmp.text("User Story: " + getString(taskModel.getUserStoryTitle())).setStyle(getBoldStyleSmall())
						.setStyle(getLineStyle()).setFixedWidth(headerWidth),
				cmp.horizontalList(cmp.text("Id:\n" + getString(taskModel.getUserStoryId())).setStyle(getBoldStyleSmall()),
						cmp.verticalList(cmp.text("Points:").setStyle(getBoldStyleSmall()), cmp.text(taskModel.getStoryPoints())
						                                                              .setStyle(getNormalStyleLarge()))
						   .setStyle(getLineStyle()))).setStyle(getLineStyle());
	}

/*
	private ComponentBuilder<?, ?> enumerateChecklist() {
		int width = cardWidth / 16;
		return cmp.verticalList(cmp.horizontalList(cmp.text("1.  Review Data Model").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("2.  Map Source Target").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("3.  Review Objects").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("4.  File Format").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("5.  Config Setup").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("6.  Coding").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("7.  Testing").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("8.  Paperwork").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("9.  Code Review").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("10. Deploy").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("11. Checkout").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)),
				cmp.horizontalList(cmp.text("12. Review Data Model").setStyle(getNoLineStyle()), cmp.text(" ").setStyle(line8style).setFixedWidth(width)));
	}
*/

	protected ComponentBuilder<?, ?> buildCardHeader(TaskModel taskModel) {
		Integer headerHeight = getCardHeight() / 3;
		return cmp.verticalList(buildLine1(taskModel), buildLine2(taskModel), buildLine3(taskModel)).setFixedHeight(headerHeight);
	}

/*
	private ComponentBuilder<?, ?> buildDecription(TaskModel taskModel) {
		return cmp.horizontalList(cmp.text("Description:\n" + getString(taskModel.getDescription())).setStyle(getPrintStyle()).setFixedWidth(headerWidth),
				enumerateChecklist().setStyle(getLineStyle())).setStyle(getLineStyle());
	}
*/

	private ComponentBuilder<?, ?> buildDecription(TaskModel taskModel) {
		return cmp.verticalList(cmp.text("Description:\n" + getString(taskModel.getDescription())).setStyle(getPrintStyle()).setFixedWidth(getCardWidth()))
		          .setStyle(getLineStyle());
	}

	@Override
	protected ComponentBuilder<?, ?> buildCard(TaskModel taskModel) {
		if (taskModel == null) {
			StyleBuilder style = styleBuilders.style(styleBuilders.pen1Point()).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
			return cmp.text("Blank Card").setStyle(style);
		}
		return cmp.verticalList(buildCardHeader(taskModel), buildDecription(taskModel)).setFixedWidth(getCardWidth()).setFixedHeight(getCardHeight()).setStyle(getLineStyle());
	}
}
