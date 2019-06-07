package com.fedex.services.agile.cards.report;

import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;

public class Templates {
	private static final StyleBuilder rootStyle;
	private static StyleBuilders stl = DynamicReports.stl;

	static final StyleBuilder boldStyle;
	static final StyleBuilder printStyle;

	static {
		rootStyle = stl.style().setPadding(2);
		boldStyle = stl.style(rootStyle).bold();
		printStyle = stl.style(rootStyle).setFontSize(7);
	}
}
