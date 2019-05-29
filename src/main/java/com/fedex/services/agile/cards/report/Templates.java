package com.fedex.services.agile.cards.report;

/**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 * <p>
 * Copyright (C) 2010 - 2015 Ricardo Mariaca http://www.dynamicreports.org
 * <p>
 * This file is part of DynamicReports.
 * <p>
 * DynamicReports is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * <p>
 * DynamicReports is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilders;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.awt.Color;
import java.util.Locale;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class Templates {
	private static final StyleBuilder rootStyle;
	private static final StyleBuilder italicStyle;
	private static final StyleBuilder boldCenteredStyle;
	private static final StyleBuilder bold12CenteredStyle;
	private static final StyleBuilder bold18CenteredStyle;
	private static final StyleBuilder bold22CenteredStyle;
	private static final StyleBuilder columnStyle;
	private static final StyleBuilder columnTitleStyle;
	private static final StyleBuilder groupStyle;
	private static final StyleBuilder subtotalStyle;
	private static final ReportTemplateBuilder reportTemplate;
	private static final CurrencyType currencyType;
	private static final ComponentBuilder<?, ?> dynamicReportsComponent;
	private static final ComponentBuilder<?, ?> footerComponent;
	private static StyleBuilders stl = DynamicReports.stl;
	private static ComponentBuilders cmp = DynamicReports.cmp;

	protected static final StyleBuilder boldStyle;
	protected static final StyleBuilder printStyle;

	static {
		rootStyle = stl.style().setPadding(2);
		boldStyle = stl.style(rootStyle).bold();
		italicStyle = stl.style(rootStyle).italic();
		printStyle = stl.style(rootStyle).setFontSize(7);
		boldCenteredStyle = stl.style(boldStyle).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
		bold12CenteredStyle = stl.style(boldCenteredStyle).setFontSize(12);
		bold18CenteredStyle = stl.style(boldCenteredStyle).setFontSize(18);
		bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);
		columnStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
		columnTitleStyle = stl.style(columnStyle).setBorder(stl.pen1Point())
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
				.setBackgroundColor(Color.LIGHT_GRAY).bold();
		groupStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
		subtotalStyle = stl.style(boldStyle).setTopBorder(stl.pen1Point());
		StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
		StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle).setBackgroundColor(new Color(170, 170, 170));
		StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle).setBackgroundColor(new Color(140, 140, 140));
		StyleBuilder crosstabCellStyle = stl.style(columnStyle).setBorder(stl.pen1Point());
		TableOfContentsCustomizerBuilder tableOfContentsCustomizer = DynamicReports.tableOfContentsCustomizer()
				.setHeadingStyle(0, stl.style(rootStyle).bold());
		reportTemplate = DynamicReports.template().setLocale(Locale.ENGLISH).setColumnStyle(columnStyle)
				.setColumnTitleStyle(columnTitleStyle).setGroupStyle(groupStyle)
				.setGroupTitleStyle(groupStyle).setSubtotalStyle(subtotalStyle)
				.highlightDetailEvenRows().crosstabHighlightEvenRows()
				.setCrosstabGroupStyle(crosstabGroupStyle)
				.setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
				.setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
				.setCrosstabCellStyle(crosstabCellStyle)
				.setTableOfContentsCustomizer(tableOfContentsCustomizer);
		currencyType = new CurrencyType();
		HyperLinkBuilder link = DynamicReports.hyperLink("http://www.dynamicreports.org");
		dynamicReportsComponent = cmp.horizontalList(cmp.image(Templates.class.getResource("images/dynamicreports.png")).setFixedDimension(60, 60), cmp.verticalList(
				cmp.text("DynamicReports").setStyle(bold22CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
				cmp.text("http://www.dynamicreports.org").setStyle(italicStyle).setHyperLink(link))).setFixedWidth(300);
		footerComponent = cmp.pageXofY().setStyle(stl.style(boldCenteredStyle).setTopBorder(stl.pen1Point()));
	}

	/**
	 * Creates custom component which is possible to add to any report band
	 * component
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(String label) {
		return cmp.horizontalList().add(dynamicReportsComponent, cmp.text(label).setStyle(bold18CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)).newRow()
				.add(cmp.line()).newRow().add(cmp.verticalGap(10));
	}

	public static CurrencyValueFormatter createCurrencyValueFormatter(String label) {
		return new CurrencyValueFormatter(label);
	}

	private static class CurrencyType extends BigDecimalType {
		private static final long serialVersionUID = 1L;

		@Override
		public String getPattern() {
			return "$ #,###.00";
		}
	}

	private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {
		private static final long serialVersionUID = 1L;
		private String label;

		public CurrencyValueFormatter(String label) {
			this.label = label;
		}

		@Override
		public String format(Number value, ReportParameters reportParameters) {
			return label + currencyType.valueToString(value, reportParameters.getLocale());
		}
	}
}
